package model;

import lombok.Getter;

@Getter
public class Package {
    private Size size;

    public Package(Size size) {

        this.size = size;
    }
}
