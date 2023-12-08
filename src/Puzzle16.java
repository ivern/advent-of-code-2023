import util.MathUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Puzzle16 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day8.txt"));

        String route = lines.get(0);
        Map<String, Branch> network = new HashMap<>();

        for (int i = 2; i < lines.size(); ++i) {
            String[] parts = lines.get(i).split(" = ");
            String[] next = parts[1].substring(1, parts[1].length() - 1).split(", ");

            network.put(parts[0].trim(), new Branch(next[0].trim(), next[1].trim()));
        }

        String[] current = network.keySet().stream().filter(s -> s.endsWith("A")).toArray(String[]::new);
        long[] cycleLength = new long[current.length];
        Arrays.fill(cycleLength, 0);

        for (int i = 0; i < current.length; ++i) {
            while (!current[i].endsWith("Z")) {
                char move = route.charAt((int) (cycleLength[i]++ % route.length()));
                current[i] = (move == 'L') ? network.get(current[i]).left() : network.get(current[i]).right();
            }
        }

        return MathUtil.lcm(cycleLength);
    }

    private record Branch(String left, String right) {
    }

}
