package model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static model.Size.S;

public class ConsoleParser {

    public static final String WALL_DELIMITER = "|";
    public static final String PACKAGE_DELIMITER = ";";

    public static final int SIZE_INDEX = 0;
    public static final int PACKAGE_STATE_INDEX = 1;

    public static final int PACKAGES_INDEX = 0;
    public static final int OWNER_INDEX = 1;
    public static final String PUT_OPERATION_CHAR = "B";
    public static final String TAKE_OPERATION_CHAR = "T";
    public static final String OWNER_DELIMITER = "@";

    public static List<Cubicle> parseLine(String line) {
        return Arrays.stream(line.replace(WALL_DELIMITER, "").split(" "))
                .map(ConsoleParser::stringToCubicle)
                .collect(Collectors.toList());
    }

    private static Cubicle stringToCubicle(String s) {
        String[] stateSplitted = s.split(":");
        Size cubicleSize = Size.valueOf(stateSplitted[SIZE_INDEX]);
        if (stateSplitted.length != 2) {
            throw new IllegalArgumentException("Chamuj się, &#%@^@");
        }
        if (State.FREE.getValue().equals(stateSplitted[PACKAGE_STATE_INDEX])) {
            return new Cubicle(cubicleSize);
        } else {
            String[] ownerSplitted = stateSplitted[PACKAGE_STATE_INDEX].split(OWNER_DELIMITER);
            List<Package> packages = ownerSplitted[PACKAGES_INDEX].chars()
                    .mapToObj(ch -> new Package(Size.valueOf(Character.toString((char) ch)), ownerSplitted[OWNER_INDEX]))
                    .collect(Collectors.toList());
            return new Cubicle(cubicleSize, packages, ownerSplitted[OWNER_INDEX]);
        }
    }

    public static List<Package> parsePackages(String line) {
        return Arrays.stream(line.split(PACKAGE_DELIMITER))
                .map(ConsoleParser::stringToPackage)
                .filter(Optional::isPresent)
                .flatMap(Optional::get)
                .collect(Collectors.toList());

    private static Optional<Stream<Package>> stringToPackage(String s) {
        if (s.startsWith(PUT_OPERATION_CHAR)) {
            return parsePackage(s.replace(PUT_OPERATION_CHAR, ""));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Stream<Package>> stringToPackage(String s) {
        if (s.startsWith(PUT_OPERATION_CHAR)) {
            return parsePackage(s.replace(PUT_OPERATION_CHAR, ""));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<Stream<Package>> parsePackage(String s) {
        String[] splitted = s.split(OWNER_DELIMITER);
        if (splitted.length == 1) {
            return Optional.of(Stream.of(new Package(S, splitted[0])));
        } else {
            if (splitted[0].length() == 0) {
                return Optional.of(Stream.of(new Package(S, splitted[1])));
            }
            return Optional.of(splitted[0].chars()
                    .mapToObj(ch -> Character.toString(ch))
                    .map(Size::valueOf)
                    .map(size -> new Package(size, splitted[1])));
        }
    }

    public static List<String> parseTaker(String line) {
        return Arrays.stream(line.split(";"))
                .map(s -> s.substring(1))
                .collect(Collectors.toList());
    }
}
