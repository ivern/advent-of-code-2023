import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Puzzle11 {

    public int solve() {
        try (var linesStream = Files.lines(Paths.get("./data/day6.txt"))) {
            var lines = linesStream.toList();

            List<Integer> times = Arrays.stream(lines.get(0).substring("Time:".length()).trim().split(" "))
                    .filter(s -> !s.isBlank())
                    .map(Integer::parseInt)
                    .toList();
            List<Integer> distances = Arrays.stream(lines.get(1).substring("Distance:".length()).trim().split(" "))
                    .filter(s -> !s.isBlank())
                    .map(Integer::parseInt)
                    .toList();

            int numWays = 1;
            for (int i = 0; i < times.size(); ++i) {
                numWays *= numWays(times.get(i), distances.get(i));
            }

            return numWays;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int numWays(int time, int record) {
        int numWays = 0;
        
        for (int hold = 1; hold < time; ++hold) {
            int distance = (time - hold) * hold;
            if (distance > record) {
                ++numWays;
            }
        }
        
        return numWays;
    }

}
