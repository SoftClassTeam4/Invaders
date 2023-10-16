package entity;

public class DeBuffItem extends Item {

    /**
     * Constructor, establishes the Item's properties.
     * and Set sprite dot image which can find what Item it ts.
     *
     * @param positionX Initial position of the Item in the X axis.
     * @param positionY Initial position of the Item in the Y axis.
     */
    public DeBuffItem(int positionX, int positionY) {

        super(positionX, positionY);
        // 버프 아이템의 수만큼 if문 늘리면 됨.
        if(Math.random() < 0.5)setSprite(2);
        else setSprite(3);
    }
}
