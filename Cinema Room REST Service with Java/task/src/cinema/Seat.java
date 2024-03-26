package cinema;


import java.util.UUID;

public class Seat {
    private  int row;
    private  int column;
    private  int price;
    private boolean purchased;
    private UUID UUID;
    public Seat() {
    }
    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.UUID = UUID.randomUUID();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }



    public boolean Purchased() {
        return purchased;
    }

    public void setPrice(){
        if (row <= 4) {
            this.price = 10;
        } else this.price = 8;
    }

    public UUID UUID() {
        return UUID;
    }

    public int getPrice() {
        return price;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

}
