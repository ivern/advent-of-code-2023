import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Puzzle8 {

    public int solve() {
        try (var linesStream = Files.lines(Paths.get("./data/day4.txt"))) {
            var lines = linesStream.toList();

            int[] counts = new int[lines.size()];
            Arrays.fill(counts, 1);

            int scratchcards = 0;

            for (int i = 0; i < lines.size(); ++i) {
                int matches = getCardMatches(lines.get(i));
                for (int j = i + 1; j < i + matches + 1 && j < counts.length; ++j) {
                    counts[j] += counts[i];
                }
                scratchcards += counts[i];
            }

            return scratchcards;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getCardMatches(String text) {
        String[] textParts = text.split(":");
        String[] numberSets = textParts[1].trim().split("\\|");

        Set<Integer> winners = getNumbers(numberSets[0]);
        Set<Integer> mine = getNumbers(numberSets[1]);

        return (int) mine.stream().filter(winners::contains).count();
    }

    private Set<Integer> getNumbers(String numberSet) {
        return Arrays.stream(numberSet.trim().split(" "))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

}
