package screen;

import engine.*;
import entity.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

import screen.GameScreen;
/**
 * Implements the game screen, where the action happens.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class GameScreen_2P extends Screen {
    /** Sound status on/off. */
    private boolean isSoundOn = true;

    /** Milliseconds until the screen accepts user input. */
    private static final int INPUT_DELAY = 6000;
    /** Bonus score for each life remaining at the end of the level. */
    private static final int LIFE_SCORE = 100;
    /** Minimum time between bonus ship's appearances. */
    private static final int BONUS_SHIP_INTERVAL = 20000;
    /** Maximum variance in the time between bonus ship's appearances. */
    private static final int BONUS_SHIP_VARIANCE = 10000;
    /** Time until bonus ship explosion disappears. */
    private static final int BONUS_SHIP_EXPLOSION = 500;
    /** Maximum variance in the time between laser's appearances. */
    private static int LASER_INTERVAL = 5000;
    /** Maximum variance in the time between Laser's appearances. */
    private static int LASER_VARIANCE = 1000;
    /** Maximum variance in the time between Laser's appearances. */
    private static int LASER_LOAD = 2000;
    /** Time until laser disappears. */
    private static final int LASER_ACTIVATE = 1000;
    /** Time from finishing the level to screen change. */
    private static final int SCREEN_CHANGE_INTERVAL = 3000;
    /** Height of the interface separation line. */
    private static final int SEPARATION_LINE_HEIGHT = 40;
    private static final int INIT_BOMB_COUNT = 5;
    private static final int BOMB_INTERVAL = 1000;
    private static final int[] DX = new int[] {1, 0, -1, 0, 1, 1, -1, -1};
    private static final int[] DY = new int[] {0, 1, 0, -1, 1, -1, 1, -1};
    private static final int DRILL_SPEED = -5;
    private static final int INIT_DRILL_COUNT = 1;


    /** Current game difficulty settings. */
    private GameSettings gameSettings;
    /** Current difficulty level number. */
    private int level;
    /** Formation of enemy ships. */
    private EnemyShipFormation enemyShipFormation;
    /** Player's ship. */
    private Ship ship_1P;
    private Ship ship_2P;
    /** Bonus enemy ship that appears sometimes. */
    private BulletLine bulletLine_1P;
    private BulletLine bulletLine_2P;
    private EnemyShip enemyShipSpecial;
    /** Minimum time between bonus ship appearances. */
    private Cooldown enemyShipSpecialCooldown;
    /** Time until bonus ship explosion disappears. */
    private Cooldown enemyShipSpecialExplosionCooldown;
    /** Time from finishing the level to screen change. */
    private Cooldown screenFinishedCooldown;
    /** Laser */
    private Laser laser;
    /** Laserline */
    private LaserLine laserline;
    /** Location of next Laser */
    private int nextLaserX;
    /** Minimum time between laser launch */
    private Cooldown laserCooldown;
    /** Load time of laser */
    private Cooldown laserLoadCooldown;
    /** Maintaining time of laser*/
    private Cooldown laserLaunchCooldown;
    /** Laser on/off (difficulty normal, upper than 4level or difficulty hard, hardcore */
    private boolean laserActivate;
    /** Set of all bullets fired by on screen ships. */
    private Set<Bullet> bullets;
    private Set<Bullet> bullets_1P;
    private Set<Bullet> bullets_2P;
    /** Set of "BulletY" fired by player ships. */
    private Set<BulletY> bulletsY;
    private Set<BulletY> bulletsY_1P;
    private Set<BulletY> bulletsY_2P;
    /** Sound Effects for player's ship and enemy. */
    private SoundEffect soundEffect;
    /** Add and Modify BGM */
    private BGM bgm;
    /** Current score. */
    private int score_1P;
    private int score_2P;
    /** Current coin. */
    private Coin coin;
    /** Player lives left. */
    private double lives_1p;
    private double lives_2p;
    /** Total bullets shot by the player. */
    private int bulletsShot_1P;
    private int bulletsShot_2P;
    /** Total ships destroyed by the player. */
    private int shipsDestroyed;
    /** Moment the game starts. */
    private long gameStartTime;
    /** Checks if the level is finished. */
    private boolean levelFinished;
    /** Checks if a bonus life is received. */
    private boolean bonusLife;
    /** Checks if the game is hardcore. */
    private boolean hardcore;
    /** Checks if the game is paused. */
    private boolean pause;
    /** Set of all items.*/
    private Set<Item> items;
    /** is none exist dropped item?*/
    private boolean isItemAllEat;
    /** Check what color will be displayed*/
    private int colorVariable;
    /** Current Value of Enhancement  Area. */
    private int attackDamage;
    /** Current Value of Enhancement  Attack. */
    private int areaDamage;
    private boolean isboss;

    private CountUpTimer timer;

    private int BulletsCount_1p=50;
    private int BulletsCount_2p=50;
    /** Set of Bombs fired by ships on screen */
    private Set<Bomb> player1Bombs;
    private Set<Bomb> player2Bombs;
    /** the number of bomb*/
    private int player1BombCount;
    private int player2BombCount;
    /** minimum time between bomb launch */

    /** Player Drill */
    private Drill player1Drill;
    private Drill player2Drill;
    private int player1DrillCount;
    private int player2DrillCount;
    private Cooldown player1BombCooldown;
    private Cooldown player2BombCooldown;
    private GameScreen gamescreen;
    private String clearCoin;
    private ItemManager itemManager;
    private EnhanceManager enhanceManager;
    private int BulletsRemaining_1p;
    private int BulletsRemaining_2p;
    /** Special Bullet **/
    private SpecialBullet SpBullet;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param gameState
     *            Current game state.
     * @param gameSettings
     *            Current game settings.
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public GameScreen_2P(final GameState_2P gameState,
                         final GameSettings gameSettings,
                         final EnhanceManager enhanceManager, final ItemManager itemManager,
                         final int width, final int height, final int fps) {
        super(width, height, fps);

        this.gameSettings = gameSettings;
        this.enhanceManager = enhanceManager;
        this.itemManager = itemManager;
        this.level = gameState.getLevel();
        this.score_1P = gameState.getScore_1P();
        this.score_2P = gameState.getScore_2P();
        this.coin = gameState.getCoin();
        this.lives_1p = gameState.getLivesRemaining_1P();
        this.lives_2p = gameState.getLivesRemaining_2P();
        //if (this.bonusLife)
        //this.lives++;
        this.bulletsShot_1P = gameState.getBulletsShot_1P();
        this.bulletsShot_2P = gameState.getBulletsShot_2P();
        this.shipsDestroyed = gameState.getShipsDestroyed();
        this.hardcore = gameState.getHardCore();
        this.pause = false;
        this.attackDamage = gameSettings.getBaseAttackDamage();
        this.areaDamage = gameSettings.getBaseAreaDamage();
        timer = new CountUpTimer();
        this.player1BombCount = INIT_BOMB_COUNT;
        this.player2BombCount = INIT_BOMB_COUNT;
        this.player1DrillCount = INIT_DRILL_COUNT;
        this.player2DrillCount = INIT_DRILL_COUNT;
        this.BulletsRemaining_1p = gameState.getBulletsRemaining_1p();
        this.BulletsRemaining_2p = gameState.getBulletsRemaining_2p();

        this.laserActivate = (gameSettings.getDifficulty() == 1 && getGameState().getLevel() >= 4) || (gameSettings.getDifficulty() > 1);
        if (gameSettings.getDifficulty() > 1) {
            LASER_INTERVAL = 3000;
            LASER_VARIANCE = 500;
            LASER_LOAD = 1500;
        }
    }

    /**
     * Initializes basic screen properties, and adds necessary elements.
     */
    public final void initialize() {
        super.initialize();

        enemyShipFormation = new EnemyShipFormation(this.gameSettings, 1);
        enemyShipFormation.attach(this);
        this.ship_1P = new Ship(this.width / 4, this.height - 30, "a", Color.WHITE);
        this.bulletLine_1P = new BulletLine(this.width / 4 , this.height + 120);
        this.ship_2P = new Ship((3 * this.width / 4), this.height - 30, "b", Color.RED);
        this.bulletLine_2P = new BulletLine(3 * this.width / 4 , this.height + 120);
        // Appears each 10-30 seconds.
        this.enemyShipSpecialCooldown = Core.getVariableCooldown(
                BONUS_SHIP_INTERVAL, BONUS_SHIP_VARIANCE);
        this.enemyShipSpecialCooldown.reset();
        this.enemyShipSpecialExplosionCooldown = Core
                .getCooldown(BONUS_SHIP_EXPLOSION);
        // Laser appears each (4~6 or 2.5~3.5) seconds, be loaded for 2 or 1.5 seconds and takes a second for launch)
        this.nextLaserX = -1;
        this.laser = null;
        this.laserCooldown = Core.getVariableCooldown(
                LASER_INTERVAL, LASER_VARIANCE);
        this.laserCooldown.reset();
        this.laserLoadCooldown = Core
                .getCooldown(LASER_LOAD);
        this.laserLoadCooldown.reset();
        this.laserLaunchCooldown = Core
                .getCooldown(LASER_ACTIVATE);
        this.laserLaunchCooldown.reset();
        this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
        this.player1BombCooldown = Core.getCooldown(BOMB_INTERVAL);
        this.player2BombCooldown = Core.getCooldown(BOMB_INTERVAL);
        this.player1Bombs = new HashSet<Bomb>();
        this.player2Bombs = new HashSet<Bomb>();
        this.bullets = new HashSet<Bullet>();
        this.bullets_1P = new HashSet<Bullet>();
        this.bullets_2P = new HashSet<Bullet>();
        this.bulletsY = new HashSet<BulletY>();
        this.bulletsY_1P = new HashSet<BulletY>();
        this.bulletsY_2P = new HashSet<BulletY>();
        this.items = new HashSet<Item>();
        this.isItemAllEat = false;

        // Special input delay / countdown.
        this.gameStartTime = System.currentTimeMillis();
        this.inputDelay = Core.getCooldown(INPUT_DELAY);
        this.inputDelay.reset();

        soundEffect = new SoundEffect();
        bgm = new BGM();

        bgm.InGame_bgm_play();

        drawManager.initBackgroundTimer(this, SEPARATION_LINE_HEIGHT); // Initializes timer for background animation.
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        this.score_1P += LIFE_SCORE * (this.lives_1p - 1);
        this.score_2P += LIFE_SCORE * (this.lives_2p - 1);
        this.logger.info("Screen cleared with a score of " + this.score_1P + " " + this.score_2P);

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {

        if (pause) { // Game Pause, press ENTER to continue or BackSpace to quit
            pause = !inputManager.isKeyDown(KeyEvent.VK_ENTER);
            boolean exit = inputManager.isKeyDown(KeyEvent.VK_BACK_SPACE);
            if (exit) {
                this.returnCode = 1;
                this.lives_1p = 0;
                this.lives_2p = 0;
                this.isRunning = false;
                bgm.InGame_bgm_stop();
            }
        }
        else {
            super.update();
            if (this.inputDelay.checkFinished() && !this.levelFinished) {
                pause = inputManager.isKeyDown(KeyEvent.VK_ESCAPE);
                if (!this.ship_1P.isDestroyed()) {
                    boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_D);
                    boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_A);

                    boolean isRightBorder = this.ship_1P.getPositionX()
                            + this.ship_1P.getWidth() + this.ship_1P.getSpeed() > this.width - 1;
                    boolean isLeftBorder = this.ship_1P.getPositionX()
                            - this.ship_1P.getSpeed() < 1;

                    if (this.ship_1P.getSpeed() >= 0)
                    {
                        if (moveRight && !isRightBorder) {
                            this.ship_1P.moveRight();
                        }
                        if (moveLeft && !isLeftBorder) {
                            this.ship_1P.moveLeft();
                        }
                    } else {
                        if (moveRight && !isLeftBorder) {
                            this.ship_1P.moveRight();
                        }
                        if (moveLeft && !isRightBorder) {
                            this.ship_1P.moveLeft();
                        }
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_SHIFT)) {
                        if(bulletsShot_1P % 6 == 0 && !(bulletsShot_1P == 0)) {
                            if (this.ship_1P.shootBulletY(this.bulletsY_1P, this.attackDamage)) {
                                soundEffect.playShipShootingSound();
                                this.bulletsShot_1P++;
                                this.BulletsCount_1p--;
                                this.BulletsRemaining_1p--;
                            }
                        }
                        else {
                            if (this.ship_1P.shoot(this.bullets_1P, this.attackDamage)) {
                                soundEffect.playShipShootingSound();
                                this.bulletsShot_1P++;
                                this.BulletsCount_1p--;
                                this.BulletsRemaining_1p--;
                            }
                        }
                    }

                    if(!isboss && inputManager.isKeyDown(KeyEvent.VK_V)
                            && player1BombCount > 0 && this.ship_1P.shootBomb(this.player1Bombs)) {
                        this.player1BombCount--;

                    }

                    if(!isboss && inputManager.isKeyDown(KeyEvent.VK_R)
                            && this.player1DrillCount > 0 && this.player1Drill == null) {
                        this.player1Drill = new Drill(this.ship_1P.getPositionX() +
                                this.ship_1P.getWidth() / 2, this.ship_1P.getPositionY(), DRILL_SPEED);
                        this.player1DrillCount--;
                    }

                }
                if (!this.ship_2P.isDestroyed()) {
                    boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT);
                    boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT);

                    boolean isRightBorder = this.ship_2P.getPositionX()
                            + this.ship_2P.getWidth() + this.ship_2P.getSpeed() > this.width - 1;
                    boolean isLeftBorder = this.ship_2P.getPositionX()
                            - this.ship_2P.getSpeed() < 1;

                    if (this.ship_2P.getSpeed() >= 0)
                    {
                        if (moveRight && !isRightBorder) {
                            this.ship_2P.moveRight();
                        }
                        if (moveLeft && !isLeftBorder) {
                            this.ship_2P.moveLeft();
                        }
                    } else {
                        if (moveRight && !isLeftBorder) {
                            this.ship_2P.moveRight();
                        }
                        if (moveLeft && !isRightBorder) {
                            this.ship_2P.moveLeft();
                        }
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_SPACE)) {
                        if(bulletsShot_2P % 6 == 0 && !(bulletsShot_2P == 0)) {
                            if (this.ship_2P.shootBulletY(this.bulletsY_2P, this.attackDamage)) {
                                soundEffect.playShipShootingSound();
                                this.bulletsShot_2P++;
                                this.BulletsCount_2p--;
                                this.BulletsRemaining_2p--;
                            }
                        }
                        else {
                            if (this.ship_2P.shoot(this.bullets_2P, this.attackDamage)) {
                                soundEffect.playShipShootingSound();
                                this.bulletsShot_2P++;
                                this.BulletsCount_2p--;
                                this.BulletsRemaining_2p--;
                            }
                        }
                    }
                    if(!isboss && inputManager.isKeyDown(KeyEvent.VK_B)
                            && player2BombCount > 0 && this.ship_2P.shootBomb(this.player2Bombs)) {
                        this.player2BombCount--;

                    }

                    if(!isboss && inputManager.isKeyDown(KeyEvent.VK_N)
                            && this.player2DrillCount > 0 && this.player2Drill == null) {
                        this.player2Drill = new Drill(this.ship_2P.getPositionX() +
                                this.ship_2P.getWidth() / 2, this.ship_2P.getPositionY(), DRILL_SPEED);
                        this.player2DrillCount--;
                    }
                }
                if (this.laserActivate) {
                    if (this.laser != null) {
                        if (this.laserLaunchCooldown.checkFinished()) {
                            this.laser = null;
                            this.laserCooldown.reset();
                            this.nextLaserX = -1;
                            this.logger.info("Laser has disappeared.");
                        }
                    }
                    if (this.laser == null) {
                        if (this.laserLoadCooldown.checkFinished() && this.nextLaserX != -1) {
                            this.laserLaunchCooldown.reset();
                            this.laserline = null;
                            this.laser = new Laser(this.nextLaserX, SEPARATION_LINE_HEIGHT, true);
                            this.logger.info("Laser has been launched.");
                        } else {
                            if (this.nextLaserX == -1 && laserCooldown.checkFinished()) {
                                this.logger.info("Laser will be launched.");
                                this.nextLaserX = (int) (Math.random() * 448);
                                this.laserline = new LaserLine(this.nextLaserX, SEPARATION_LINE_HEIGHT);
                                this.laserLoadCooldown.reset();
                            }
                        }
                    }
                }
                if (this.enemyShipSpecial != null) {
                    if (!this.enemyShipSpecial.isDestroyed()) {
                        this.enemyShipSpecial.move(2, 0);
                        if (this.enemyShipSpecial.getPositionX() > this.enemyShipSpecial.getLaunchPos()
                                && !this.enemyShipSpecial.getSpBulletLoaded()) {
                            this.enemyShipSpecial.LaunchSpecialBullet();
                            this.SpBullet = new SpecialBullet(this.enemyShipSpecial.getPositionX(),
                                    this.enemyShipSpecial.getPositionY(), 3, 1,
                                    this.enemyShipSpecial.getColor(), this.enemyShipSpecial.getBulletType());
                        }
                    }
                    else if (this.enemyShipSpecialExplosionCooldown.checkFinished())
                        this.enemyShipSpecial = null;
                }
                if (this.enemyShipSpecial == null
                        && this.enemyShipSpecialCooldown.checkFinished()) {
                    bgm.enemyShipSpecialbgm_play();
                    colorVariable = (int)(Math.random()*4);
                    int launchPos = (int)(this.getWidth() * Math.random());
                    switch (colorVariable) {
                        case 0:
                            this.enemyShipSpecial = new EnemyShip(Color.RED, colorVariable, launchPos);
                            break;
                        case 1:
                            this.enemyShipSpecial = new EnemyShip(Color.YELLOW, colorVariable, launchPos);
                            break;
                        case 2:
                            this.enemyShipSpecial = new EnemyShip(Color.BLUE, colorVariable, launchPos);
                            break;
                        case 3:
                            this.enemyShipSpecial = new EnemyShip(Color.WHITE, colorVariable, launchPos);
                            break;
                        default:
                            break;
                    }
                    this.enemyShipSpecialCooldown.reset();
                    this.logger.info("A special ship appears");
                }
                if (this.enemyShipSpecial != null
                        && this.enemyShipSpecial.getPositionX() > this.width) {
                    bgm.enemyShipSpecialbgm_stop();
                    this.enemyShipSpecial = null;
                    this.logger.info("The special ship has escaped");
                }
                if (this.SpBullet != null) {
                    if (this.SpBullet.getPositionY() < getHeight()){
                        this.SpBullet.update();
                    }
                    else {
                        this.SpBullet = null;
                    }
                }

                this.ship_1P.update();
                this.ship_2P.update();
                this.enemyShipFormation.update();
                this.enemyShipFormation.shoot(this.bullets);
            }

            manageCollisions();
            manageCollisionsY();
            manageBombColisions();
            cleanBombs_1P();
            cleanBombs_2P();
            cleanBullets();
            cleanBullets_1P();
            cleanBullets_2P();
            cleanBulletsY();
            cleanBulletsY_1P();
            cleanBulletsY_2P();
            cleanItems();

            if(this.player1Drill != null){
                managePlayer1DrillColisions();
                updatePlayer1Drill();
            }

            if(this.player2Drill != null){
                managePlayer2DrillColisions();
                updatePlayer2Drill();
            }

            draw();
        }
        if (this.enemyShipFormation.isEmpty() && !this.levelFinished) {
            endStageAllEat();
            bgm.enemyShipSpecialbgm_stop();
            this.levelFinished = true;
            this.screenFinishedCooldown.reset();
            timer.stop();
        }
        if(this.lives_2p<=0){
            ship_2P.destroy();
        }
        if(this.lives_1p<=0){
            ship_1P.destroy();
        }
        if (this.lives_1p <= 0 && !this.levelFinished && this.lives_2p<=0) {
            bgm.enemyShipSpecialbgm_stop();
            this.levelFinished = true;
            //drawManager.ghost1PostionX = this.ship_1P.getPositionX();
            //drawManager.ghost1PostionY = this.ship_1P.getPositionY() - 25;
            //drawManager.ghost2PostionX = this.ship_2P.getPositionX();
            //drawManager.ghost2PostionY = this.ship_2P.getPositionY() - 25;
            //drawManager.endTimer.reset();
            //drawManager.ghostTImer = System.currentTimeMillis();
            soundEffect.playShipDestructionSound();
            this.screenFinishedCooldown.reset();
            timer.stop();
        }


        if ((isItemAllEat || this.levelFinished) && this.screenFinishedCooldown.checkFinished()){
            bgm.InGame_bgm_stop();
            this.isRunning = false;
            timer.stop();
            if ((int)(timer.getElapsedTime() / 1000) > 0 && (int)(timer.getElapsedTime() / 1000) < 30) {
                this.coin.addCoin(20);
            }
            else if ((int)(timer.getElapsedTime() / 1000) >= 30 && (int)(timer.getElapsedTime() / 1000) < 40) {
                this.coin.addCoin(15);
            }
            else if ((int)(timer.getElapsedTime() / 1000) >= 40 && (int)(timer.getElapsedTime() / 1000) < 50) {
                this.coin.addCoin(10);
            }
            else{
                this.coin.addCoin(5);
            }
        }
        if (this.BulletsCount_1p <= 0){
            this.ship_1P.destroy();
            this.BulletsCount_1p = 0;
        }
        if (this.BulletsCount_2p <= 0){
            this.ship_2P.destroy();
            this.BulletsCount_2p = 0;
        }
        if (this.BulletsCount_1p == 0 && this.BulletsCount_2p == 0 && !this.levelFinished){
            bgm.enemyShipSpecialbgm_stop();
            this.levelFinished = true;
            soundEffect.playShipDestructionSound();
            this.screenFinishedCooldown.reset();
        }
        if((this.lives_1p <= 0 && this.lives_2p <= 0) && !this.levelFinished
        &&  (this.BulletsCount_1p == 0 && this.BulletsCount_2p == 0)) {
            bgm.enemyShipSpecialbgm_stop();
            this.levelFinished = true;
            soundEffect.playShipDestructionSound();
            this.screenFinishedCooldown.reset();
        }

        timer.update();

    }
    /**
     * when the stage end, eat all dropped item.
     */
    private void endStageAllEat(){
        Cooldown a = Core.getCooldown(25);
//        bgm.InGame_bgm_stop();
        a.reset();
        while(!this.items.isEmpty()){
            if(a.checkFinished()) {
                manageCollisions();
                for (Item item : this.items) {
                    item.resetItem(this.ship_1P);
                }
                a.reset();
            }
            draw();
        }
        isItemAllEat = true;
    }


    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);
        drawManager.drawBackground(this, SEPARATION_LINE_HEIGHT, (int)this.lives_1p);
        drawManager.drawBackground(this, SEPARATION_LINE_HEIGHT, (int)this.lives_2p);
        if (this.enemyShipSpecial != null) drawManager.drawBackgroundSpecialEnemy(this, SEPARATION_LINE_HEIGHT);
        drawManager.drawBackgroundLines(this, SEPARATION_LINE_HEIGHT);
        drawManager.drawBackgroundPlayer(this, SEPARATION_LINE_HEIGHT, this.ship_1P.getPositionX(), this.ship_1P.getPositionY(), this.ship_1P.getWidth(), this.ship_1P.getHeight());
        drawManager.drawBackgroundPlayer(this, SEPARATION_LINE_HEIGHT, this.ship_2P.getPositionX(), this.ship_2P.getPositionY(), this.ship_2P.getWidth(), this.ship_2P.getHeight());
        drawManager.BulletsCount_1p(this,this.BulletsCount_1p);
        drawManager.BulletsCount_2p(this, this.BulletsCount_2p);
        if(!isboss){
            drawManager.bombsCount_1p(this, this.player1BombCount);
            drawManager.bombsCount_2p(this, this.player2BombCount);
        }

        drawManager.drawEntity(this.ship_1P, this.ship_1P.getPositionX(),
                this.ship_1P.getPositionY());
        drawManager.drawEntity(this.bulletLine_1P, this.ship_1P.getPositionX() + 12,
                this.ship_1P.getPositionY() - 320);

        drawManager.drawEntity(this.ship_2P, this.ship_2P.getPositionX(),
                this.ship_2P.getPositionY());
        drawManager.drawEntity(this.bulletLine_2P, this.ship_2P.getPositionX() + 12,
                this.ship_2P.getPositionY() - 320);

        if (this.enemyShipSpecial != null)
            drawManager.drawEntity(this.enemyShipSpecial,
                    this.enemyShipSpecial.getPositionX(),
                    this.enemyShipSpecial.getPositionY());
        if (this.laser != null)
            drawManager.drawEntity(this.laser,
                    this.laser.getPositionX(),
                    this.laser.getPositionY());
        if (this.laserline != null)
            drawManager.drawEntity(this.laserline,
                    this.laserline.getPositionX(),
                    this.laserline.getPositionY());
        for (Item item : this.items)
            drawManager.drawEntity(item, item.getPositionX(),
                    item.getPositionY());
        enemyShipFormation.draw();

        for (Bomb bomb : this.player1Bombs)
            drawManager.drawEntity(bomb, bomb.getPositionX(), bomb.getPositionY());

        for (Bomb bomb : this.player2Bombs)
            drawManager.drawEntity(bomb, bomb.getPositionX(), bomb.getPositionY());

        for (Bullet bullet : this.bullets)
            drawManager.drawEntity(bullet, bullet.getPositionX(),
                    bullet.getPositionY());

        for (Bullet bullet : this.bullets_1P)
            drawManager.drawEntity(bullet, bullet.getPositionX(),
                    bullet.getPositionY());

        for (Bullet bullet : this.bullets_2P)
            drawManager.drawEntity(bullet, bullet.getPositionX(),
                    bullet.getPositionY());

        for (BulletY bulletY : this.bulletsY)
            drawManager.drawEntity(bulletY, bulletY.getPositionX(),
                    bulletY.getPositionY());

        for (BulletY bulletY : this.bulletsY_1P)
            drawManager.drawEntity(bulletY, bulletY.getPositionX(),
                    bulletY.getPositionY());

        for (BulletY bulletY : this.bulletsY_2P)
            drawManager.drawEntity(bulletY, bulletY.getPositionX(),
                    bulletY.getPositionY());

        if(this.player1Drill != null)
            drawManager.drawEntity(this.player1Drill, this.player1Drill.getPositionX(),
                    this.player1Drill.getPositionY());

        if(this.player2Drill != null)
            drawManager.drawEntity(this.player2Drill, this.player2Drill.getPositionX(),
                    this.player2Drill.getPositionY());

        if (this.SpBullet != null)
            drawManager.drawEntity(this.SpBullet,
                    this.SpBullet.getPositionX(),
                    this.SpBullet.getPositionY());

        // Interface.
        drawManager.drawScore_2p(this, this.score_1P,"p1", 105);
        drawManager.drawScore_2p(this, this.score_1P + this.score_2P,"total", 183);
        drawManager.drawScore_2p(this, this.score_2P,"p2",260);
        drawManager.drawLivesbar_2p(this, this.lives_1p, 8, "1P lives");
        drawManager.drawLivesbar_2p(this, this.lives_2p, 330, "2P lives");
        drawManager.drawCoin(this, this.coin, 0);
        drawManager.drawitemcircle(this,itemManager.getShieldCount(),itemManager.getBombCount());
        isboss = gameSettings.checkIsBossStage();

        if (inputManager.isKeyPressedOnce(KeyEvent.VK_1)) {
            if (itemManager.getShieldCount() > 0 && timer.getElapsedTime() != 0 && ship_1P.getShieldState() != true && ship_2P.getShieldState() != true && !levelFinished)
            {
                logger.info("Key number 1 press");
                itemManager.PlusShieldCount(-1);
                ship_1P .setShieldState(true);
                ship_1P.update();
                ship_2P .setShieldState(true);
                ship_2P.update();
            }


        }
        else if (inputManager.isKeyPressedOnce(KeyEvent.VK_2) & timer.getElapsedTime() != 0 && enemyShipFormation.isEmpty() == false)
        {
            if (itemManager.getBombCount() > 0)
            {
                logger.info("Key number 2 press");
                itemManager.PlusBombCount(-1);
                this.enemyShipFormation.bombDestroy(items);
            }

        }

        if (isboss) {
            for (EnemyShip enemyShip : this.enemyShipFormation)
                drawManager.drawBossLivesbar(this, enemyShip.getEnemyLife());
        }
        drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
        //drawManager.scoreEmoji(this, this.score_1P);
        drawManager.drawSoundButton2(this);
        if (inputManager.isKeyDown(KeyEvent.VK_C)) {
            isSoundOn = !isSoundOn;
            if (isSoundOn) {
                bgm.InGame_bgm_play();
            } else {
                bgm.InGame_bgm_stop();
                soundEffect.SoundEffect_stop();
                bgm.enemyShipSpecialbgm_stop();
            }
        }

        drawManager.drawSoundStatus2(this, isSoundOn);
        drawManager.drawTimer(this, timer.getElapsedTime());

        //GameOver
        drawManager.gameOver_2p(this, this.levelFinished, this.lives_1p, this.lives_2p, this.BulletsCount_1p, this.BulletsCount_2p, this.timer, this.coin, this.clearCoin);
        drawManager.changeGhostColor_2p(this.levelFinished, this.lives_1p, this.lives_2p);
        drawManager.drawGhost_2p(this.levelFinished, this.lives_1p, this.lives_2p);
        this.ship_1P.gameEndShipMotion(this.levelFinished, this.lives_1p);

        this.ship_2P.gameEndShipMotion(this.levelFinished, this.lives_2p);


        // Countdown to game start.
        if (!this.inputDelay.checkFinished()) {
            int countdown = (int) ((INPUT_DELAY
                    - (System.currentTimeMillis()
                    - this.gameStartTime)) / 1000);
            drawManager.drawCountDown(this, this.level, countdown,
                    this.bonusLife);

            // Fade from white at game start.
            drawManager.drawBackgroundStart(this, SEPARATION_LINE_HEIGHT);

            /* this code is modified with Clean Code (dodo_kdy)  */
            //drawManager.drawHorizontalLine(this, this.height / 2 - this.height / 12);
            //drawManager.drawHorizontalLine(this, this.height / 2 + this.height / 12);
        }


        // If Game has been paused
        if (this.pause) {
            drawManager.drawPaused(this);
        }

        drawManager.completeDrawing(this);


    }

    /**
     * Cleans bullets that go off screen.
     */
    private void cleanBullets() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets) {
            bullet.update();
            if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bullet.getPositionY() > this.height)
                recyclable.add(bullet);
        }

        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    private void cleanBullets_1P() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets_1P) {
            bullet.update();
            if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bullet.getPositionY() > this.height)
                recyclable.add(bullet);
        }

        this.bullets_1P.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    private void cleanBullets_2P() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets_2P) {
            bullet.update();
            if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bullet.getPositionY() > this.height)
                recyclable.add(bullet);
        }

        this.bullets_2P.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    private void cleanBombs_1P() {
        Set<Bomb> recyclable = new HashSet<>();
        for(Bomb bomb : this.player1Bombs) {
            bomb.update();
            if(bomb.getPositionY() < SEPARATION_LINE_HEIGHT || bomb.getPositionY() > this.height)
                recyclable.add(bomb);
        }
        this.player1Bombs.removeAll(recyclable);
        BombPool.recycle(recyclable);
    }

    private void cleanBombs_2P() {
        Set<Bomb> recyclable = new HashSet<>();
        for(Bomb bomb : this.player2Bombs) {
            bomb.update();
            if(bomb.getPositionY() < SEPARATION_LINE_HEIGHT || bomb.getPositionY() > this.height)
                recyclable.add(bomb);
        }
        this.player2Bombs.removeAll(recyclable);
        BombPool.recycle(recyclable);
    }

    private void updatePlayer1Drill() {
        this.player1Drill.update();
        if(this.player1Drill.getPositionY() < SEPARATION_LINE_HEIGHT || this.player1Drill.getPositionY() > this.height)
            this.player1Drill = null;
    }

    private void updatePlayer2Drill() {
        this.player2Drill.update();
        if(this.player2Drill.getPositionY() < SEPARATION_LINE_HEIGHT || this.player2Drill.getPositionY() > this.height)
            this.player2Drill = null;
    }

    private void cleanBulletsY() {
        Set<BulletY> recyclable = new HashSet<BulletY>();
        for (BulletY bulletY : this.bulletsY) {
            bulletY.update();
            if (bulletY.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bulletY.getPositionY() > this.height)
                recyclable.add(bulletY);
        }
        this.bulletsY.removeAll(recyclable);
        BulletPool.recycleBulletY(recyclable);
    }

    private void cleanBulletsY_1P() {
        Set<BulletY> recyclable = new HashSet<BulletY>();
        for (BulletY bulletY : this.bulletsY_1P) {
            bulletY.update();
            if (bulletY.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bulletY.getPositionY() > this.height)
                recyclable.add(bulletY);
        }
        this.bulletsY_1P.removeAll(recyclable);
        BulletPool.recycleBulletY(recyclable);
    }

    private void cleanBulletsY_2P() {
        Set<BulletY> recyclable = new HashSet<BulletY>();
        for (BulletY bulletY : this.bulletsY_2P) {
            bulletY.update();
            if (bulletY.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bulletY.getPositionY() > this.height)
                recyclable.add(bulletY);
        }
        this.bulletsY_2P.removeAll(recyclable);
        BulletPool.recycleBulletY(recyclable);
    }

    /**
     * update and Cleans items that end the Living-Time
     */
    private void cleanItems() {
        Set<Item> recyclable = new HashSet<Item>();
        for (Item item : this.items) {
            item.update(this.getWidth(), this.getHeight(), SEPARATION_LINE_HEIGHT);
            if (item.islivingTimeEnd()){
                recyclable.add(item);
            }
        }
        this.items.removeAll(recyclable);
        ItemPool.recycle(recyclable);
    }

    /**
     * Manages collisions between bullets and ships.
     */
    private void manageCollisions() {
        Set<Bullet> recyclableBullet = new HashSet<Bullet>();
        Set<Item> recyclableItem = new HashSet<Item>();
        for (Bullet bullet : this.bullets)
            if (bullet.getSpeed() > 0) {
                if (checkCollision(bullet, this.ship_1P) && !this.levelFinished) {
                    recyclableBullet.add(bullet);
                    if (!this.ship_1P.isDestroyed()) {
                        this.ship_1P.destroy();
                        if (this.lives_1p != 1) soundEffect.playShipCollisionSound();
                        this.lives_1p--;
                        this.logger.info("Hit on player ship_1p, " + this.lives_1p
                                + " lives remaining.");
                    }
                }
                else if (checkCollision(bullet, this.ship_2P) && !this.levelFinished) {
                    recyclableBullet.add(bullet);
                    if (!this.ship_2P.isDestroyed()) {
                        this.ship_2P.destroy();
                        if (this.lives_2p != 1) soundEffect.playShipCollisionSound();
                        this.lives_2p--;
                        this.logger.info("Hit on player ship_2p, " + this.lives_2p
                                + " lives remaining.");
                    }
                }
            }
        for(Bullet bullet_1P : this.bullets_1P)
            if (bullet_1P.getSpeed() > 0) {
            } else {
                for (EnemyShip enemyShip : this.enemyShipFormation)
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bullet_1P, enemyShip)) {
                        enemyShip.reduceEnemyLife(this.attackDamage);
                        soundEffect.playEnemyDestructionSound();
                        if(enemyShip.getEnemyLife() < 1) {
                            this.score_1P += enemyShip.getPointValue();
                            this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
                            this.shipsDestroyed++;
                            this.shipsDestroyed += this.enemyShipFormation.getShipsDestroyed();
                        }
                        recyclableBullet.add(bullet_1P);
                    }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bullet_1P, this.enemyShipSpecial)) {
                    enemyShipSpecial.reduceEnemyLife(this.attackDamage);
                    if (enemyShipSpecial.getEnemyLife() < 1) {
                        this.score_1P += this.enemyShipSpecial.getPointValue();
                        this.shipsDestroyed++;
                        this.enemyShipSpecial.destroy(this.items);
                        soundEffect.enemyshipspecialDestructionSound();
                        bgm.enemyShipSpecialbgm_stop();
                        if (this.lives_1p < 2.9) this.lives_1p = this.lives_1p + 0.1;
                        if (this.lives_2p < 2.9) this.lives_2p = this.lives_2p + 0.1;
                        this.enemyShipSpecialExplosionCooldown.reset();
                    }
                    recyclableBullet.add(bullet_1P);
                }
            }

        for(Bullet bullet_2P : this.bullets_2P)
            if (bullet_2P.getSpeed() > 0) {
            } else {
                for (EnemyShip enemyShip : this.enemyShipFormation)
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bullet_2P, enemyShip)) {
                        enemyShip.reduceEnemyLife(this.attackDamage);
                        soundEffect.playEnemyDestructionSound();
                        if(enemyShip.getEnemyLife() < 1) {
                            this.score_2P += enemyShip.getPointValue();
                            this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
                            this.shipsDestroyed++;
                            this.shipsDestroyed += this.enemyShipFormation.getShipsDestroyed();
                        }
                        recyclableBullet.add(bullet_2P);
                    }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bullet_2P, this.enemyShipSpecial)) {
                    enemyShipSpecial.reduceEnemyLife(this.attackDamage);
                    if (enemyShipSpecial.getEnemyLife() < 1) {
                        this.score_2P += this.enemyShipSpecial.getPointValue();
                        this.shipsDestroyed++;
                        this.enemyShipSpecial.destroy(this.items);
                        soundEffect.enemyshipspecialDestructionSound();
                        bgm.enemyShipSpecialbgm_stop();
                        if (this.lives_1p < 2.9) this.lives_1p = this.lives_1p + 0.1;
                        if (this.lives_2p < 2.9) this.lives_2p = this.lives_2p + 0.1;
                        this.enemyShipSpecialExplosionCooldown.reset();
                    }
                    recyclableBullet.add(bullet_2P);
                }
            }

        if (this.laser != null) {
            if (checkCollision(this.laser, this.ship_1P) && !this.levelFinished) {
                if (!this.ship_1P.isDestroyed()) {
                    this.ship_1P.destroy();
                    if (this.lives_1p != 1) soundEffect.playShipCollisionSound();
                    this.lives_1p--;
                    if (gameSettings.getDifficulty() == 3) this.lives_1p = 0;
                    this.logger.info("Hit on ship_1 " + this.lives_1p
                            + " lives remaining.");
                }
            }
            if (checkCollision(this.laser, this.ship_2P) && !this.levelFinished) {
                if (!this.ship_2P.isDestroyed()) {
                    this.ship_2P.destroy();
                    if (this.lives_2p != 1) soundEffect.playShipCollisionSound();
                    this.lives_2p--;
                    if (gameSettings.getDifficulty() == 3) this.lives_2p = 0;
                    this.logger.info("Hit on ship_2 " + this.lives_2p
                            + " lives remaining.");
                }
            }
        }
        if (this.SpBullet != null) {
            if (checkCollision(this.SpBullet, this.ship_1P) && !this.levelFinished) {
                if (!this.ship_1P.isDestroyed()) {
                    this.ship_1P.destroy();
                    if (this.lives_1p != 1) soundEffect.playShipCollisionSound();
                    this.lives_1p--;
                    if (gameSettings.getDifficulty() == 3) this.lives_1p = 0;
                    this.logger.info("Hit on player ship, " + this.lives_1p
                            + " lives remaining.");
                }
            }
            if (checkCollision(this.SpBullet, this.ship_2P) && !this.levelFinished) {
                if (!this.ship_2P.isDestroyed()) {
                    this.ship_2P.destroy();
                    if (this.lives_2p != 1) soundEffect.playShipCollisionSound();
                    this.lives_2p--;
                    if (gameSettings.getDifficulty() == 3) this.lives_2p = 0;
                    this.logger.info("Hit on player ship, " + this.lives_2p
                            + " lives remaining.");
                }
            }
        }
        for (Item item : this.items){
            if(checkCollision(item, this.ship_1P) && !this.levelFinished && !item.isDestroyed()){
                recyclableItem.add(item);
                this.logger.info("Get Item Ship_1");

                //* settings of coins randomly got when killing monsters
                ArrayList<Integer> coinProbability = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 2, 3, 4));
                Random random = new Random();
                int randomIndex = random.nextInt(coinProbability.size());

                if(item.getSpriteType() == DrawManager.SpriteType.Coin){
                    this.coin.addCoin(coinProbability.get(randomIndex));

                }
                if(item.getSpriteType() == DrawManager.SpriteType.BlueEnhanceStone){
                    this.enhanceManager.PlusNumEnhanceStoneArea(1);
                }
                if(item.getSpriteType() == DrawManager.SpriteType.PerpleEnhanceStone){
                    this.enhanceManager.PlusNumEnhanceStoneAttack(1);
                }
                this.ship_1P.checkGetItem(item);
            }
            if(checkCollision(item, this.ship_2P) && !this.levelFinished && !item.isDestroyed()){
                recyclableItem.add(item);
                this.logger.info("Get Item Ship_2");
                //* settings of coins randomly got when killing monsters
                ArrayList<Integer> coinProbability = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 1, 1, 1, 2, 3, 4));
                Random random = new Random();
                int randomIndex = random.nextInt(coinProbability.size());

                if(item.getSpriteType() == DrawManager.SpriteType.Coin){
                    this.coin.addCoin(coinProbability.get(randomIndex));

                }
                if(item.getSpriteType() == DrawManager.SpriteType.BlueEnhanceStone){
                    this.enhanceManager.PlusNumEnhanceStoneArea(1);
                }
                if(item.getSpriteType() == DrawManager.SpriteType.PerpleEnhanceStone){
                    this.enhanceManager.PlusNumEnhanceStoneAttack(1);
                }

                this.ship_2P.checkGetItem(item);
            }
        }
        for (Bullet bullet : recyclableBullet) {
            if (bullet.getSpeed() < 0 && bullet.isEffectBullet() == 0) {
                bullet.splash(this.bullets);
            }
        }

        this.items.removeAll(recyclableItem);
        this.bullets.removeAll(recyclableBullet);
        this.bullets_1P.removeAll(recyclableBullet);
        this.bullets_2P.removeAll(recyclableBullet);
        ItemPool.recycle(recyclableItem);
        BulletPool.recycle(recyclableBullet);
    }

    /**
     * Manages collisions between bulletsY and ships.
     */
    private void manageCollisionsY() {
        Set<BulletY> recyclableBulletY = new HashSet<BulletY>();
        Set<Item> recyclableItem = new HashSet<Item>();
        for (BulletY bulletY : this.bulletsY)
            if (bulletY.getSpeed() > 0) {
                if (checkCollision(bulletY, this.ship_1P) && !this.levelFinished) {
                    recyclableBulletY.add(bulletY);
                    if (!this.ship_1P.isDestroyed()) {
                        this.ship_1P.destroy();
                        if (this.lives_1p != 1) soundEffect.playShipCollisionSound();
                        this.lives_1p--;
                        this.logger.info("Hit on player ship, " + this.lives_1p
                                + " lives remaining.");
                    }
                }
                else if (checkCollision(bulletY, this.ship_2P) && !this.levelFinished) {
                    recyclableBulletY.add(bulletY);
                    if (!this.ship_2P.isDestroyed()) {
                        this.ship_2P.destroy();
                        if (this.lives_2p != 1) soundEffect.playShipCollisionSound();
                        this.lives_2p--;
                        this.logger.info("Hit on player ship, " + this.lives_2p
                                + " lives remaining.");
                    }
                }
            }
        for (BulletY bulletY_1P : this.bulletsY_1P)
            if (bulletY_1P.getSpeed() > 0) {
            } else {
                for (EnemyShip enemyShip : this.enemyShipFormation)
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bulletY_1P, enemyShip)) {
                        enemyShip.reduceEnemyLife(bulletY_1P.getDamage());
                        soundEffect.playEnemyDestructionSound();
                        if(enemyShip.getEnemyLife() < 1) {
                            this.score_1P += enemyShip.getPointValue();
                            this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
                            this.shipsDestroyed++;
                            this.shipsDestroyed += this.enemyShipFormation.getShipsDestroyed();
                        }
                        recyclableBulletY.add(bulletY_1P);
                    }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bulletY_1P, this.enemyShipSpecial)) {
                    enemyShipSpecial.reduceEnemyLife(bulletY_1P.getDamage());
                    if(enemyShipSpecial.getEnemyLife() < 1) {
                        this.score_1P += this.enemyShipSpecial.getPointValue();
                        this.shipsDestroyed++;
                        this.enemyShipSpecial.destroy(this.items);
                        soundEffect.enemyshipspecialDestructionSound();
                        bgm.enemyShipSpecialbgm_stop();
                        if (this.lives_1p < 2.9) this.lives_1p = this.lives_1p + 0.1;
                        if (this.lives_2p < 2.9) this.lives_2p = this.lives_2p + 0.1;
                        this.enemyShipSpecialExplosionCooldown.reset();
                    }
                    recyclableBulletY.add(bulletY_1P);
                }
            }
        for (BulletY bulletY_2P : this.bulletsY_2P)
            if (bulletY_2P.getSpeed() > 0) {
            } else {
                for (EnemyShip enemyShip : this.enemyShipFormation)
                    if (!enemyShip.isDestroyed()
                            && checkCollision(bulletY_2P, enemyShip)) {
                        enemyShip.reduceEnemyLife(bulletY_2P.getDamage());
                        soundEffect.playEnemyDestructionSound();
                        if(enemyShip.getEnemyLife() < 1) {
                            this.score_2P += enemyShip.getPointValue();
                            this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
                            this.shipsDestroyed++;
                            this.shipsDestroyed += this.enemyShipFormation.getShipsDestroyed();
                        }
                        recyclableBulletY.add(bulletY_2P);
                    }
                if (this.enemyShipSpecial != null
                        && !this.enemyShipSpecial.isDestroyed()
                        && checkCollision(bulletY_2P, this.enemyShipSpecial)) {
                    enemyShipSpecial.reduceEnemyLife(bulletY_2P.getDamage());
                    if(enemyShipSpecial.getEnemyLife() < 1) {
                        this.score_2P += this.enemyShipSpecial.getPointValue();
                        this.shipsDestroyed++;
                        this.enemyShipSpecial.destroy(this.items);
                        soundEffect.enemyshipspecialDestructionSound();
                        bgm.enemyShipSpecialbgm_stop();
                        if (this.lives_1p < 2.9) this.lives_1p = this.lives_1p + 0.1;
                        if (this.lives_2p < 2.9) this.lives_2p = this.lives_2p + 0.1;
                        this.enemyShipSpecialExplosionCooldown.reset();
                    }
                    recyclableBulletY.add(bulletY_2P);
                }
            }
        this.items.removeAll(recyclableItem);
        this.bulletsY.removeAll(recyclableBulletY);
        this.bulletsY_1P.removeAll(recyclableBulletY);
        this.bulletsY_2P.removeAll(recyclableBulletY);
        ItemPool.recycle(recyclableItem);
        BulletPool.recycleBulletY(recyclableBulletY);
    }

    private void manageBombColisions() {
        Set<Bomb> recyclableBomb1P = new HashSet<>();
        Set<Bomb> recyclableBomb2P = new HashSet<>();
        for(Bomb bomb : this.player1Bombs) {
            for(EnemyShip enemyShip : this.enemyShipFormation) {
                if(!enemyShip.isDestroyed() && checkCollision(bomb, enemyShip)) {
                    areaDestroy1P(enemyShip);
                    recyclableBomb1P.add(bomb);
                }
            }

            if (this.enemyShipSpecial != null
                    && !this.enemyShipSpecial.isDestroyed()
                    && checkCollision(bomb, this.enemyShipSpecial)) {

                this.score_1P += this.enemyShipSpecial.getPointValue();
                this.shipsDestroyed++;
                this.enemyShipSpecial.destroy(this.items);
                soundEffect.enemyshipspecialDestructionSound();
                bgm.enemyShipSpecialbgm_stop();
                if (this.lives_1p < 2.9) this.lives_1p = this.lives_1p + 0.1;
                this.enemyShipSpecialExplosionCooldown.reset();

                recyclableBomb1P.add(bomb);
            }
        }

        for(Bomb bomb : this.player2Bombs) {
            for(EnemyShip enemyShip : this.enemyShipFormation) {
                if(!enemyShip.isDestroyed() && checkCollision(bomb, enemyShip)) {
                    areaDestroy2P(enemyShip);
                    recyclableBomb2P.add(bomb);
                }
            }

            if (this.enemyShipSpecial != null
                    && !this.enemyShipSpecial.isDestroyed()
                    && checkCollision(bomb, this.enemyShipSpecial)) {

                this.score_2P += this.enemyShipSpecial.getPointValue();
                this.shipsDestroyed++;
                this.enemyShipSpecial.destroy(this.items);
                soundEffect.enemyshipspecialDestructionSound();
                bgm.enemyShipSpecialbgm_stop();
                if (this.lives_2p < 2.9) this.lives_2p = this.lives_2p + 0.1;
                this.enemyShipSpecialExplosionCooldown.reset();

                recyclableBomb2P.add(bomb);
            }
        }
        this.player1Bombs.removeAll(recyclableBomb1P);
        BombPool.recycle(recyclableBomb1P);
        this.player2Bombs.removeAll(recyclableBomb2P);
        BombPool.recycle(recyclableBomb2P);
    }

    private void areaDestroy1P(EnemyShip enemyShip) {
        int col = -1, row = -1;
        List<List<EnemyShip>> enemyShips = this.enemyShipFormation.getEnemyShips();
        for(List<EnemyShip> column : enemyShips) {
            if(column.contains(enemyShip)) {
                col = enemyShips.indexOf(column);
            }
        }
        List<EnemyShip> column = enemyShips.get(col);
        row = column.indexOf(enemyShip);
        this.score_1P += enemyShip.getPointValue();
        this.shipsDestroyed++;
        this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
        for(int dir = 0; dir < 8; dir++) {
            int nRow = row + DX[dir];
            int nCol = col + DY[dir];
            if(nRow < 0 || nCol < 0 ||  nCol >= enemyShips.size() || nRow >= enemyShips.get(nCol).size()) {
                continue;
            }
            EnemyShip enemy = enemyShips.get(nCol).get(nRow);
            if(!enemy.isDestroyed()) {
                this.score_1P += enemyShip.getPointValue();
                this.shipsDestroyed++;
                this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemy, this.items);
            }
        }
    }

    private void areaDestroy2P(EnemyShip enemyShip) {
        int col = -1, row = -1;
        List<List<EnemyShip>> enemyShips = this.enemyShipFormation.getEnemyShips();
        for(List<EnemyShip> column : enemyShips) {
            if(column.contains(enemyShip)) {
                col = enemyShips.indexOf(column);
            }
        }
        List<EnemyShip> column = enemyShips.get(col);
        row = column.indexOf(enemyShip);
        this.score_2P += enemyShip.getPointValue();
        this.shipsDestroyed++;
        this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
        for(int dir = 0; dir < 8; dir++) {
            int nRow = row + DX[dir];
            int nCol = col + DY[dir];
            if(nRow < 0 || nCol < 0 ||  nCol >= enemyShips.size() || nRow >= enemyShips.get(nCol).size()) {
                continue;
            }
            EnemyShip enemy = enemyShips.get(nCol).get(nRow);
            if(!enemy.isDestroyed()) {
                this.score_2P += enemyShip.getPointValue();
                this.shipsDestroyed++;
                this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemy, this.items);
            }
        }
    }

    private void managePlayer1DrillColisions() {
        for(EnemyShip enemyShip : this.enemyShipFormation) {
            if(!enemyShip.isDestroyed() && checkCollision(this.player1Drill, enemyShip)) {
                this.score_1P += enemyShip.getPointValue();
                this.shipsDestroyed++;
                this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
            }
        }

        if (this.enemyShipSpecial != null
                && !this.enemyShipSpecial.isDestroyed()
                && checkCollision(this.player1Drill, this.enemyShipSpecial)) {
            this.score_1P += this.enemyShipSpecial.getPointValue();
            this.shipsDestroyed++;
            this.enemyShipSpecial.destroy(this.items);
            soundEffect.enemyshipspecialDestructionSound();
            bgm.enemyShipSpecialbgm_stop();
            if (this.lives_1p < 2.9) this.lives_1p = this.lives_1p + 0.1;
            this.enemyShipSpecialExplosionCooldown.reset();
        }
    }

    private void managePlayer2DrillColisions() {
        for(EnemyShip enemyShip : this.enemyShipFormation) {
            if(!enemyShip.isDestroyed() && checkCollision(this.player2Drill, enemyShip)) {
                this.score_2P += enemyShip.getPointValue();
                this.shipsDestroyed++;
                this.enemyShipFormation.destroy(enhanceManager.getlvEnhanceArea(), enemyShip, this.items);
            }
        }

        if (this.enemyShipSpecial != null
                && !this.enemyShipSpecial.isDestroyed()
                && checkCollision(this.player2Drill, this.enemyShipSpecial)) {
            this.score_2P += this.enemyShipSpecial.getPointValue();
            this.shipsDestroyed++;
            this.enemyShipSpecial.destroy(this.items);
            soundEffect.enemyshipspecialDestructionSound();
            bgm.enemyShipSpecialbgm_stop();
            if (this.lives_2p < 2.9) this.lives_2p = this.lives_2p + 0.1;
            this.enemyShipSpecialExplosionCooldown.reset();
        }
    }

    /**
     * Checks if two entities are colliding.
     *
     * @param a
     *            First entity, the bullet.
     * @param b
     *            Second entity, the ship.
     * @return Result of the collision test.
     */
    private boolean checkCollision(final Entity a, final Entity b) {
        // Calculate center point of the entities in both axis.
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        // Calculate maximum distance without collision.
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        // Calculates distance.
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);

        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }

    /**
     * Returns a GameState object representing the status of the game.
     *
     * @return Current game state.
     */
    public final GameState_2P getGameState() {
        return new GameState_2P(this.level, this.score_1P, this.score_2P, this.coin, this.lives_1p, this.lives_2p,
                this.bulletsShot_1P, this.bulletsShot_2P, this.shipsDestroyed, this.hardcore,this.BulletsRemaining_1p,this.BulletsRemaining_2p);
    }
}