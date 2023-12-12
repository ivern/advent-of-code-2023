import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class Puzzle24 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day12.txt"));
        return lines.stream().mapToLong(this::arrangements).sum();
    }

    long arrangements(String line) {
        String[] parts = line.split(" ");

        String springs = parts[0].trim();
        List<Integer> runs = Arrays.stream(parts[1].trim().split(",")).map(Integer::parseInt).toList();

        springs = springs + "?" + springs + "?" + springs + "?" + springs + "?" + springs;
        runs = Stream.of(runs, runs, runs, runs, runs).flatMap(Collection::stream).toList();

        return arrangements(springs, runs, 0, 0, new HashMap<>());
    }

    long arrangements(String springs, List<Integer> runs, int nextSpring, int nextRun, Map<Key, Long> cache) {
        Key key = new Key(nextSpring, nextRun);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (nextRun >= runs.size()) {
            return springs.substring(nextSpring).contains("#") ? 0 : 1;
        } else if (nextSpring >= springs.length()) {
            return 0;
        }

        if (nextSpring > 0) {
            if (springs.charAt(nextSpring) == '#') {
                return 0;
            } else {
                nextSpring++;
            }
        }
        while (nextSpring < springs.length() - 1 && springs.charAt(nextSpring) == '.') {
            nextSpring++;
        }

        long count = 0;

        int run = runs.get(nextRun);
        for (int s = nextSpring; s < springs.length(); ++s) {
            if (s + run > springs.length()) {
                break;
            }
            
            boolean fits = true;
            for (int i = s; i < s + run && fits; ++i) {
                if (springs.charAt(i) == '.') {
                    fits = false;
                }
            }

            if (fits) {
                count += arrangements(springs, runs, s + run, nextRun + 1, cache);
            }

            if (springs.charAt(s) == '#') {
                break;
            }
        }

        cache.put(key, count);

        return count;
    }

    record Key(int spring, int run) {
    }

}
