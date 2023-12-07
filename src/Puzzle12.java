import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Puzzle12 {

    public long solve() {
        try (var linesStream = Files.lines(Paths.get("./data/day6.txt"))) {
            var lines = linesStream.toList();

            long time = Long.parseLong(
                    Arrays.stream(lines.get(0).substring("Time:".length()).trim().split(" "))
                            .filter(s -> !s.isBlank())
                            .collect(Collectors.joining()));
            BigInteger distance = new BigInteger(
                    Arrays.stream(lines.get(1).substring("Distance:".length()).trim().split(" "))
                            .filter(s -> !s.isBlank())
                            .collect(Collectors.joining()));

            return numWays(time, distance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long numWays(long time, BigInteger record) {
        long numWays = 0;

        for (long hold = 1; hold < time; ++hold) {
            BigInteger distance = BigInteger.valueOf(time - hold).multiply(BigInteger.valueOf(hold));
            if (distance.compareTo(record) > 0) {
                ++numWays;
            }
        }

        return numWays;
    }

}
