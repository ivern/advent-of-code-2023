import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Puzzle15 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day8.txt"));

        String route = lines.get(0);
        Map<String, Branch> network = new HashMap<>();

        for (int i = 2; i < lines.size(); ++i) {
            String[] parts = lines.get(i).split(" = ");
            String[] next = parts[1].substring(1, parts[1].length() - 1).split(", ");

            network.put(parts[0].trim(), new Branch(next[0].trim(), next[1].trim()));
        }

        int steps = 0;

        String current = "AAA";
        while (!current.equals("ZZZ")) {
            char move = route.charAt(steps++ % route.length());
            current = (move == 'L') ? network.get(current).left() : network.get(current).right();
        }

        return steps;
    }

    private record Branch(String left, String right) {
    }

}
