package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static model.Size.MAX_POSSIBLE_FIT;

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
        packages.forEach(p -> {
            int bestFit = MAX_POSSIBLE_FIT + 1;
            int bestFitIndex = -1;
            for (int i = 0; i < cubicles.size(); i++) {
                Cubicle cubicleOfInterest = cubicles.get(i);
                int fit = cubicleOfInterest.fit(p);
                if (fit == 1) {
                    cubicles.set(i, cubicleOfInterest.insert(p));
                    return;
                } else if (fit != 0 && fit < bestFit) {
                    bestFit = fit;
                    bestFitIndex = i;
                }
            }
            if (bestFitIndex != -1) {
                cubicles.set(bestFitIndex, cubicles.get(bestFitIndex).insert(p));
            } else {
                //Full
            }
        });
    }

    public List<List<Cubicle>> get() {
        return IntStream.range(0, n).mapToObj(i -> cubicles.subList(i * m, i * m + m)).collect(Collectors.toList());
    }
}
