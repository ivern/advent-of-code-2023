import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Puzzle1 {

    public int solve() throws IOException {
        try (var lines = Files.lines(Paths.get("./input/puzzle1.txt"))) {
            return lines.mapToInt(this::getOriginalNumber).sum();
        }
    }

    private int getOriginalNumber(String text) {
        int[] digits = text.chars().filter(Character::isDigit).map(Character::getNumericValue).toArray();
        return digits[0] * 10 + digits[digits.length - 1];
    }

}
