import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Puzzle6 {

    public int solve() {
        try (var lineStream = Files.lines(Paths.get("./data/day3.txt"))) {
            var lines = lineStream.toList();
            var gears = new HashMap<String, List<Integer>>();

            for (int r = 0; r < lines.size(); ++r) {
                String row = lines.get(r);
                for (int c = 0; c < row.length(); ++c) {
                    int start = c;
                    StringBuilder builder = new StringBuilder();
                    while (c < row.length() && Character.isDigit(row.charAt(c))) {
                        builder.append(row.charAt(c));
                        c++;
                    }
                    String numString = builder.toString();
                    if (numString.isEmpty()) {
                        continue;
                    }
                    int number = Integer.parseInt(numString);
                    int end = c - 1;

                    if (r > 0) {
                        String prev = lines.get(r - 1);
                        for (int i = start - 1; i <= end + 1 && i <= row.length() - 1; ++i) {
                            if (i < 0) continue;
                            if (isGear(prev.charAt(i))) {
                                gears.computeIfAbsent(key(r - 1, i), _k -> new ArrayList<>()).add(number);
                            }
                        }
                    }
                    if (r < lines.size() - 1) {
                        String next = lines.get(r + 1);
                        for (int i = start - 1; i <= end + 1 && i <= row.length() - 1; ++i) {
                            if (i < 0) continue;
                            if (isGear(next.charAt(i))) {
                                gears.computeIfAbsent(key(r + 1, i), _k -> new ArrayList<>()).add(number);
                            }
                        }
                    }
                    if (start > 0) {
                        if (isGear(row.charAt(start - 1))) {
                            gears.computeIfAbsent(key(r, start - 1), _k -> new ArrayList<>()).add(number);
                        }
                    }
                    if (end < row.length() - 1) {
                        if (isGear(row.charAt(end + 1))) {
                            gears.computeIfAbsent(key(r, end + 1), _k -> new ArrayList<>()).add(number);
                        }
                    }
                }
            }

            return gears.values().stream()
                    .filter(v -> v.size() == 2)
                    .mapToInt(v -> v.get(0) * v.get(1))
                    .sum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String key(int r, int c) {
        return r + "_" + c;
    }

    private boolean isGear(char c) {
        return c == '*';
    }

}
