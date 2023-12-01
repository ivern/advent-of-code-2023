import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Puzzle2 {

    public int solve() throws IOException {
        try (var lines = Files.lines(Paths.get("./input/puzzle2.txt"))) {
            return lines.mapToInt(this::getOriginalNumber).sum();
        }
    }

    private int getOriginalNumber(String text) {
        var digits = new ArrayList<Integer>();

        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                digits.add(Character.getNumericValue(c));
            } else {
                var rest = text.substring(i);
                if (rest.startsWith("one")) {
                    digits.add(1);
                } else if (rest.startsWith("two")) {
                    digits.add(2);
                } else if (rest.startsWith("three")) {
                    digits.add(3);
                } else if (rest.startsWith("four")) {
                    digits.add(4);
                } else if (rest.startsWith("five")) {
                    digits.add(5);
                } else if (rest.startsWith("six")) {
                    digits.add(6);
                } else if (rest.startsWith("seven")) {
                    digits.add(7);
                } else if (rest.startsWith("eight")) {
                    digits.add(8);
                } else if (rest.startsWith("nine")) {
                    digits.add(9);
                }
            }
        }

        return digits.getFirst() * 10 + digits.getLast();
    }

}
