package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;
import static model.Size.MAX_POSSIBLE_FIT;

public class Idemiomat {

    private List<Cubicle> cubicles;
    private Map<String, List<Integer>> ownerPackages = new HashMap<>();
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
        this.cubicles.addAll(cubicles);
        for (int i = 0; i < cubicles.size(); i++) {
            int j = i;
            cubicles.get(i).getOwner()
                    .ifPresent(o -> this.ownerPackages.merge(o, Arrays.asList(m * n + j), CollectionUtils::merge));
        }
        n++;
    }

    public void add(List<Package> packages) {
        if (packages.isEmpty()) {
            return;
        }
        packages.forEach(p -> {
            Optional<Integer> availableCubicle = putToCurrentOwnerCubicles(p.getOwner(), p);
            int bestFit = MAX_POSSIBLE_FIT + 1;
            int bestFitIndex = -1;
            if (availableCubicle.isPresent()) {
                bestFitIndex = availableCubicle.get();
            } else {
                for (int i = 0; i < cubicles.size(); i++) {
                    Cubicle cubicleOfInterest = cubicles.get(i);
                    int fit = cubicleOfInterest.fit(p);
                    if (fit == 1) {
                        bestFitIndex = i;
                        break;
                    } else if (fit != 0 && fit < bestFit) {
                        bestFit = fit;
                        bestFitIndex = i;
                    }
                }
            }

            if (bestFitIndex != -1) {
                cubicles.set(bestFitIndex, cubicles.get(bestFitIndex).insert(p));
                ownerPackages.merge(p.getOwner(), Arrays.asList(bestFitIndex), CollectionUtils::merge);
            } else {
                //Full
            }
        });
    }

    private Optional<Integer> putToCurrentOwnerCubicles(String owner, Package p) {
        return ofNullable(this.ownerPackages.get(owner))
                .flatMap(ownerCubicles -> ownerCubicles.stream().filter(i -> this.cubicles.get(i).fit(p) > 0).findFirst());
    }

    public List<List<Cubicle>> get() {
        return IntStream.range(0, n).mapToObj(i -> cubicles.subList(i * m, i * m + m)).collect(Collectors.toList());
    }

    public void take(String taker) {
        ofNullable(this.ownerPackages.get(taker))
                .ifPresent(takerPackages -> {
                    takerPackages.forEach(i -> this.cubicles.set(i, this.cubicles.get(i).takePackages()));
                    ownerPackages.remove(taker);
                });
    }
}
