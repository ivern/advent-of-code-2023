import util.Coordinate;
import util.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Puzzle35 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day18.txt"));
        var regex = Pattern.compile("([UDRL]) (\\d+) \\(#(.{6})\\)");
        var trenches = new ArrayList<Trench>();

        for (var line : lines) {
            var matcher = regex.matcher(line);
            if (!matcher.find()) {
                continue;
            }

            trenches.add(new Trench(parseDirection(matcher.group(1)), Integer.parseInt(matcher.group(2)), matcher.group(3)));
        }

        final int size = 1000;
        int[][] map = new int[size][];
        for (int i = 0; i < size; ++i) {
            map[i] = new int[size];
            Arrays.fill(map[i], 0);
        }

        var position = new Coordinate(size / 2, size / 2);
        for (var trench : trenches) {
            for (int i = 0; i < trench.distance(); ++i) {
                position = position.move(trench.direction());
                map[position.row()][position.col()] = 1;
            }
        }

        var fringe = new LinkedList<Coordinate>();
        fringe.add(new Coordinate(0, 0));

        while (!fringe.isEmpty()) {
            var next = fringe.remove();
            int r = next.row();
            int c = next.col();
            if (r < 0 || c < 0 || r >= size || c >= size || map[r][c] > 0) {
                continue;
            }
            map[r][c] = 2;
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (i != 0 || j != 0) {
                        fringe.add(new Coordinate(r + i, c + j));
                    }
                }
            }
        }

        int count = 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                if (map[i][j] < 2) {
                    ++count;
                }
            }
        }

        return count;
    }

    record Trench(Direction direction, int distance, String color) {
    }

    Direction parseDirection(String s) {
        return switch (s) {
            case "U" -> Direction.UP;
            case "D" -> Direction.DN;
            case "L" -> Direction.LT;
            case "R" -> Direction.RT;
            default -> throw new RuntimeException();
        };
    }

}
