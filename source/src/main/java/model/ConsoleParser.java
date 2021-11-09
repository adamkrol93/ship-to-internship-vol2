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

    public static List<Cubicle> parseLine(String line) {
        return Arrays.stream(line.replace(WALL_DELIMITER, "").split(" "))
                .map(ConsoleParser::stringToCubicle)
                .collect(Collectors.toList());
    }

    private static Cubicle stringToCubicle(String s) {
        String[] splitted = s.split(":");
        Size cubicleSize = Size.valueOf(splitted[0]);
        if (splitted.length != 2) {
            throw new IllegalArgumentException("Chamuj się, &#%@^@");
        }
        if (State.FREE.getValue().equals(splitted[1])) {
            return new Cubicle(cubicleSize);
        } else {
            if (splitted[1].equals("X")) {
                return new Cubicle(cubicleSize, new Package(Size.valueOf(splitted[0])));
            }
            return new Cubicle(cubicleSize, new Package(Size.valueOf(splitted[1])));
        }
    }

    public static List<Package> parsePackages(String line) {
        return Arrays.stream(line.split(PACKAGE_DELIMITER))
                .map(ConsoleParser::stringToPackage)
                .filter(Optional::isPresent)
                .flatMap(Optional::get)
                .collect(Collectors.toList());

    }

    private static Optional<Stream<Package>> stringToPackage(String s) {
        if (s.startsWith("B")) {
            if (s.length() == 1) {
                return Optional.of(Stream.of(new Package(S)));
            } else {
                return Optional.of(s.replace("B", "").chars()
                        .mapToObj(ch -> Character.toString(ch))
                        .map(Size::valueOf)
                        .map(Package::new));
            }
        } else {
            return Optional.empty();
        }
    }
}
