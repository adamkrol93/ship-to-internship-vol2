package model;

import java.util.ArrayList;
import java.util.List;

public final class CollectionUtils {
    public static <T> List<T> merge(List<T> listA, List<T> listB) {
        ArrayList<T> newList = new ArrayList<>(listA);
        newList.addAll(listB);
        return newList;
    }
}
