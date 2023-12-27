import util.Coordinate;
import util.Direction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Puzzle36 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day18.txt"));
        var regex = Pattern.compile("[UDRL] \\d+ \\(#(.{5})(.)\\)");

        var horizontals = new TreeMap<Integer, List<Trench>>();
        var verticals = new TreeMap<Integer, List<Trench>>();

        var start = new Coordinate(0, 0);

        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;

        var perimeter = new HashSet<Coordinate>();

        for (var line : lines) {
            var matcher = regex.matcher(line);
            if (!matcher.find()) {
                continue;
            }

            var direction = parseDirection(matcher.group(2));
            var distance = Integer.parseInt(matcher.group(1), 16);
            var end = start.move(direction, distance);

            var trench = switch (direction) {
                case UP, LT -> new Trench(end.row(), end.col(), start.row(), start.col());
                case DN, RT -> new Trench(start.row(), start.col(), end.row(), end.col());
            };

            minRow = Math.min(Math.min(minRow, start.row()), end.row());
            maxRow = Math.max(Math.max(maxRow, start.row()), end.row());
            minCol = Math.min(Math.min(minCol, start.col()), end.col());
            maxCol = Math.max(Math.max(maxCol, start.col()), end.col());

            // todo update perimeter

            start = end;
        }

        int inside = 0;



        return perimeter.size() + inside;
    }

    record Trench(int row1, int col1, int row2, int col2) {
    }

    Direction parseDirection(String s) {
        return switch (s) {
            case "3" -> Direction.UP;
            case "1" -> Direction.DN;
            case "2" -> Direction.LT;
            case "0" -> Direction.RT;
            default -> throw new RuntimeException();
        };
    }

}
