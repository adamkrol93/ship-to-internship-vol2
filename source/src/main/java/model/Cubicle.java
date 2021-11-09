package model;

import lombok.Getter;

@Getter
public class Cubicle {

    private Size size;
    private Package box;

    public Cubicle(Size size) {
        this.size = size;
    }

    public Cubicle(Size size, Package box) {
        this.size = size;
        this.box = box;
    }

    @Override
    public String toString() {
        return size.name() + ":" + getTakenMark();
    }

    private String getTakenMark() {
        if (box == null) {
            return State.FREE.getValue();
        }
        return box.getSize() == size ? State.TAKEN.getValue() : box.getSize().name();
    }

    public Cubicle insert(Package box) {
        return new Cubicle(this.size, box);
    }

    public int fit(Package boxToInsert) {
        if (this.box != null) {
            return 0;
        }
        return this.size.getValue() / boxToInsert.getSize().getValue();
    }
}
