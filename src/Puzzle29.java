import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Puzzle29 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day15.txt"));
        var steps = lines.stream().flatMap(s -> Arrays.stream(s.split(","))).toList();
        return steps.stream().mapToLong(this::hash).sum();
    }

    long hash(String s) {
        byte[] ascii = StandardCharsets.US_ASCII.encode(s).array();
        long hash = 0;

        for (byte b : ascii) {
            hash = ((hash + b) * 17) % 256;
        }

        return hash;
    }

}
