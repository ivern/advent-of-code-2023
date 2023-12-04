import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Puzzle7 {

    public int solve() {
        try (var lines = Files.lines(Paths.get("./data/day4.txt"))) {
            return lines.mapToInt(this::getCardPoints).sum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getCardPoints(String text) {
        String[] textParts = text.split(":");
        String[] numberSets = textParts[1].trim().split("\\|");

        Set<Integer> winners = getNumbers(numberSets[0]);
        Set<Integer> mine = getNumbers(numberSets[1]);
        int inCommon = (int) mine.stream().filter(winners::contains).count();

        int points = 0;

        if (inCommon-- > 0) {
            points = 1;
        }
        while (inCommon-- > 0) {
            points *= 2;
        }

        return points;
    }

    private Set<Integer> getNumbers(String numberSet) {
        return Arrays.stream(numberSet.trim().split(" "))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

}