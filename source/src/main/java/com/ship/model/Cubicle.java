package com.ship.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

import java.util.Comparator;

import static java.util.function.Function.identity;

@Getter
public class Cubicle {

    private Size size;
    private List<Package> packages;
    private String owner;

    public Cubicle(Size size) {
        this.size = size;
        this.packages = new ArrayList<>();
    }

    public Cubicle(Size size, List<Package> packages, String owner) {
        this.size = size;
        this.packages = packages;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return size.name() + ":" + getPackagesToString() + getOwner().map(o -> "@" + o).orElse("");
    }

    private String getPackagesToString() {
        return packages.isEmpty() ? State.FREE.getValue() : formatNotEmptyCubicle();
    }

    private String formatNotEmptyCubicle() {
        List<String> packages = this.packages.stream()
                .map(Package::getSize)
                .sorted(Comparator.comparing(identity()))
                .map(Size::name)
                .collect(Collectors.toList());
        return packages.size() == 1 && packages.get(0).equals(this.size.name()) ? "X" : String.join("", packages);
    }

    public Cubicle insert(Package box) {
        if (canBeInserted(box)) {
            ArrayList<Package> newPackages = new ArrayList<>(this.packages);
            newPackages.add(box);
            return new Cubicle(this.size, newPackages, ofNullable(this.owner).orElse(box.getOwner()));
        } else {
            throw new IllegalArgumentException("This box has different owner. Chamu sie %&%&");
        }
    }

    private boolean canBeInserted(Package box) {
        return !this.getOwner().isPresent() || box.getOwner().equals(this.owner);
    }

    public int fit(Package boxToInsert) {
        if (canBeInserted(boxToInsert)) {
            Integer takenPlace = sumPackagesFitScore();
            int freePlace = this.size.getValue() - takenPlace;
            return freePlace / boxToInsert.getSize().getValue();
        } else {
            return 0;
        }
    }

    private Integer sumPackagesFitScore() {
        return this.packages.stream().map(Package::getSize)
                .collect(Collectors.summingInt(Size::getValue));
    }

    public Optional<String> getOwner() {
        return ofNullable(owner);
    }

    public Cubicle takePackages() {
        return new Cubicle(this.size);
    }

    public boolean isFree() {
        return this.owner == null;
    }
}
