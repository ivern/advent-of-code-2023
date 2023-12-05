import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Puzzle10 {

    public long solve() {
        try (var linesStream = Files.lines(Paths.get("./data/day5.txt"))) {
            var lines = linesStream.toList();
            List<Range> seeds = new ArrayList<>();
            Map<String, PuzzleMap> mapsBySourceType = new HashMap<>();

            for (int i = 0; i < lines.size(); ) {
                String next = lines.get(i++);
                if (next.startsWith("seeds:")) {
                    var nums = next.substring("seeds: ".length()).split(" ");
                    for (int j = 0; j < nums.length; j += 2) {
                        long start = Long.parseLong(nums[j].trim());
                        long length = Long.parseLong(nums[j + 1].trim());
                        seeds.add(Range.range(start, start + length));
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
                    .flatMap(range -> findEndRanges(
                            mapsBySourceType, "seed", List.of(range), "location").stream())
                    .mapToLong(Range::min)
                    .min()
                    .orElse(-1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Range> findEndRanges(
            Map<String, PuzzleMap> mapsBySourceType, String currentType, List<Range> currentValue, String endType) {

        if (currentType.equals(endType)) {
            return currentValue;
        }

        var map = mapsBySourceType.get(currentType);
        var nextValue = currentValue.stream().flatMap(range -> map.mapRange(range).stream()).toList();

        return findEndRanges(mapsBySourceType, map.destinationType, nextValue, endType);
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

        public List<Range> mapRange(Range range) {
            var mapped = new ArrayList<Range>();
            var current = Range.range(range.min(), range.max());

            while (!current.isEmpty()) {
                boolean addedMapping = false;

                for (int i = 0; i < sourceRangeStarts.size(); ++i) {
                    long min = -1;
                    long max = -1;

                    long minOffset = current.min() - sourceRangeStarts.get(i);
                    if (minOffset >= 0 && minOffset < rangeLengths.get(i)) {
                        min = destinationRangeStarts.get(i) + minOffset;
                    }
                    long maxOffset = current.max() - sourceRangeStarts.get(i);
                    if (maxOffset >= 0 && maxOffset < rangeLengths.get(i)) {
                        max = destinationRangeStarts.get(i) + maxOffset;
                    }

                    if (min != -1 && max != -1) {
                        mapped.add(Range.range(min, max));
                        current = Range.EMPTY;
                        addedMapping = true;
                        break;
                    } else if (min != -1) {
                        long cap = destinationRangeStarts.get(i) + rangeLengths.get(i) - 1;
                        mapped.add(Range.range(min, cap));
                        current = Range.range(sourceRangeStarts.get(i) + rangeLengths.get(i), current.max());
                        addedMapping = true;
                    } else if (max != -1) {
                        long cap = destinationRangeStarts.get(i);
                        mapped.add(Range.range(cap, max));
                        current = Range.range(current.min(), sourceRangeStarts.get(i) - 1);
                        addedMapping = true;
                    }
                }

                if (!addedMapping) {
                    if (!current.isEmpty()) {
                        mapped.add(current);
                    }
                    break;
                }
            }

            return mapped.isEmpty() ? List.of(range) : mapped;
        }
    }

    public record Range(long min, long max) {
        public static final Range EMPTY = new Range(Long.MAX_VALUE, Long.MIN_VALUE);

        public static Range range(long a, long b) {
            return a < b ? new Range(a, b) : new Range(b, a);
        }

        public boolean isEmpty() {
            return min > max;
        }
    }

}
