import model.ConsoleParser;
import model.Cubicle;
import model.Idemiomat;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static model.ConsoleParser.WALL_DELIMITER;

public class App {
    public static void main(String... args) {
        testableMethod(System.in, System.out);
    }

    protected static void testableMethod(InputStream inputStream, PrintStream outputStream) {
        Scanner sc = new Scanner(inputStream);
        Idemiomat idemiomat = new Idemiomat();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith(WALL_DELIMITER)) {
                idemiomat.addRow(ConsoleParser.parseLine(line));
            } else if (line.startsWith("T")) {
                ConsoleParser.parseTaker(line).forEach(idemiomat::take);
                print(outputStream, idemiomat);
            } else if (line.startsWith("S")) {
                print(outputStream, idemiomat);
            } else if (line.startsWith("B")) {
                idemiomat.add(ConsoleParser.parsePackages(line));
                print(outputStream, idemiomat);
            }
        }
    }

    private static void print(PrintStream outputStream, Idemiomat idemiomat) {
        List<List<Cubicle>> result = idemiomat.get();
        result.forEach(r -> {
            String row = String.join(" ", r.stream().map(Cubicle::toString).collect(Collectors.toList()));
            outputStream.println("|" + row + "|");
        });
    }

}
