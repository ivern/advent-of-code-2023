import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class Puzzle9 {

    public long solve() {
        try (var linesStream = Files.lines(Paths.get("./data/day5.txt"))) {
            var lines = linesStream.toList();
            Set<Long> seeds = new HashSet<>();
            Map<String, PuzzleMap> mapsBySourceType = new HashMap<>();

            for (int i = 0; i < lines.size(); ) {
                String next = lines.get(i++);
                if (next.startsWith("seeds:")) {
                    for (var s : next.substring("seeds: ".length()).split(" ")) {
                        seeds.add(Long.parseLong(s.trim()));
                    }
                } else if (next.contains("map")) {
                    String[] sides = next.substring(0, next.length() - " map:".length()).split("-to-");
                    var map = new PuzzleMap(sides[0], sides[1]);
                    while (i < lines.size() && !(next = lines.get(i++)).isBlank()) {
                        map.addRange(next);
                    }
                    mapsBySourceType.put(map.sourceType, map);
                } else if (next.isBlank()) {
                    continue;
                }
            }

            return seeds.stream()
                    .mapToLong(seed -> findEndValue(mapsBySourceType, "seed", seed, "location"))
                    .min()
                    .orElse(-1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long findEndValue(Map<String, PuzzleMap> mapsBySourceType, String currentType, long currentValue, String endType) {
        if (currentType.equals(endType)) {
            return currentValue;
        }

        var map = mapsBySourceType.get(currentType);
        var nextValue = map.get(currentValue);

        return findEndValue(mapsBySourceType, map.destinationType, nextValue, endType);
    }

    public static class PuzzleMap {
        public final String sourceType;
        public final String destinationType;

        private final ArrayList<Long> sourceRangeStarts;
        private final ArrayList<Long> destinationRangeStarts;
        private final ArrayList<Long> rangeLengths;

        public PuzzleMap(String sourceType, String destinationType) {
            this.sourceType = sourceType;
            this.destinationType = destinationType;
            this.sourceRangeStarts = new ArrayList<>();
            this.destinationRangeStarts = new ArrayList<>();
            this.rangeLengths = new ArrayList<>();
        }

        public void addRange(String range) {
            String[] parts = range.trim().split(" ");

            destinationRangeStarts.add(Long.parseLong(parts[0].trim()));
            sourceRangeStarts.add(Long.parseLong(parts[1].trim()));
            rangeLengths.add(Long.parseLong(parts[2].trim()));
        }

        public long get(long key) {
            for (int i = 0; i < sourceRangeStarts.size(); ++i) {
                long offset = key - sourceRangeStarts.get(i);
                if (offset >= 0 && offset < rangeLengths.get(i)) {
                    return destinationRangeStarts.get(i) + offset;
                }
            }
            return key;
        }
    }

}
