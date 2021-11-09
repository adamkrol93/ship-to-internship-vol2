package model;

import io.vavr.Tuple;
import io.vavr.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConsoleParser {

    public static final String LINE_DELIMITER = "|";
    public static final String PACKAGE_DELIMITER = ";";

    public static List<Cubicle> parseLine(String line) {
        return Arrays.stream(line.replace(LINE_DELIMITER, "").split(" "))
                .map(ConsoleParser::stringToCubicle)
                .collect(Collectors.toList());
    }

    private static Cubicle stringToCubicle(String s) {

        return new Cubicle(State.valueOfLabel(s.split(":")[1]));
    }

    public static List<Package> parsePackages(String line) {
        return Arrays.stream(line.split(PACKAGE_DELIMITER))
                .map(s -> new Package()).collect(Collectors.toList());

    }
}
