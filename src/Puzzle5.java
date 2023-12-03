import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle5 {

    public int solve() {
        try (var lineStream = Files.lines(Paths.get("./data/day3.txt"))) {
            var lines = lineStream.toList();
            int sum = 0;

            for (int r = 0; r < lines.size(); ++r) {
                String row = lines.get(r);
                for (int c = 0; c < row.length(); ++c) {
                    int start = c;
                    StringBuilder builder = new StringBuilder();
                    while (c < row.length() && Character.isDigit(row.charAt(c))) {
                        builder.append(row.charAt(c));
                        c++;
                    }
                    String number = builder.toString();
                    if (number.isEmpty()) {
                        continue;
                    }
                    int end = c - 1;

                    boolean isPart = false;
                    if (r > 0) {
                        String prev = lines.get(r - 1);
                        for (int i = start - 1; !isPart && i <= end + 1 && i <= row.length() - 1; ++i) {
                            if (i < 0) continue;
                            isPart |= isSymbol(prev.charAt(i));
                        }
                    }
                    if (!isPart && r < lines.size() - 1) {
                        String next = lines.get(r + 1);
                        for (int i = start - 1; !isPart && i <= end + 1 && i <= row.length() - 1; ++i) {
                            if (i < 0) continue;
                            isPart |= isSymbol(next.charAt(i));
                        }
                    }
                    if (!isPart && start > 0) {
                        isPart |= isSymbol(row.charAt(start - 1));
                    }
                    if (!isPart && end < row.length() - 1) {
                        isPart |= isSymbol(row.charAt(end + 1));
                    }

                    if (isPart) {
                        sum += Integer.parseInt(number);
                    }
                }
            }

            return sum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSymbol(char c) {
        return !Character.isDigit(c) && c != '.';
    }

}
