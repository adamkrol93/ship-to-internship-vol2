package model;

import lombok.Getter;

@Getter
public class Package {
    private Size size;
    private String owner;

    public Package(Size size, String owner) {
        this.size = size;
        this.owner = owner;
    }
}
