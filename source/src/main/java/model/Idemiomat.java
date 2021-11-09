package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Idemiomat {

    private List<Cubicle> cubicles;
    private int n;
    private int m = 0;

    public Idemiomat() {
        cubicles = new ArrayList<>();
    }

    public void addRow(List<Cubicle> cubicles) {
        if (this.cubicles.isEmpty()) {
            m = cubicles.size();
        } else {
            if (m != cubicles.size()) {
                throw new IllegalArgumentException("Different length format of row");
            }
        }
        n++;
        this.cubicles.addAll(cubicles);
    }

    public void add(List<Package> packages) {
        if (packages.isEmpty()) {
            return;
        }
        for (int i = 0; i < cubicles.size(); i++) {
            if (cubicles.get(i).isFree()) {
                cubicles.set(i, new Cubicle(State.TAKEN));
                packages.remove(0);
                if (packages.isEmpty()) {
                    break;
                }
            }
        }
    }

    public List<List<Cubicle>> get() {
        return IntStream.range(0, n).mapToObj(i -> cubicles.subList(i * m, i * m + m)).collect(Collectors.toList());
    }
}
