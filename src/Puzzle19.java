import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Puzzle19 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day10.txt"));

        int numRows = lines.size();
        int numCols = lines.get(0).length();

        int startRow = -1;
        int startCol = -1;

        findStart:
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (lines.get(row).charAt(col) == 'S') {
                    startRow = row;
                    startCol = col;
                    break findStart;
                }
            }
        }

        var cycle = findCycle(lines, new Pos(startRow, startCol), numRows, numCols, new HashSet<>(), new Pos(-1, -1));

        return cycle.size() / 2;
    }

    List<Pos> findCycle(List<String> map, Pos pos, int numRows, int numCols, Set<Pos> seen, Pos last) {
        if (seen.contains(pos)) {
            List<Pos> cycle = new ArrayList<>();
            cycle.add(pos);
            return cycle;
        }

        if (pos.row() < 0 || pos.col() < 0 || pos.row() >= numRows || pos.col() >= numCols) {
            return null;
        }

        char ch = map.get(pos.row()).charAt(pos.col());
        if (ch == '.') {
            return null;
        }

        char up = (pos.row() > 0) ? map.get(pos.row() - 1).charAt(pos.col()) : '.';
        char dn = (pos.row() < numRows - 1) ? map.get(pos.row() + 1).charAt(pos.col()) : '.';
        char lt = (pos.col() > 0) ? map.get(pos.row()).charAt(pos.col() - 1) : '.';
        char rt = (pos.col() < numCols - 1) ? map.get(pos.row()).charAt(pos.col() + 1) : '.';

        var ways = new ArrayList<Pos>();

        if (ch == 'L') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(new Pos(pos.row() - 1, pos.col()));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() + 1));
            }
        } else if (ch == 'J') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(new Pos(pos.row() - 1, pos.col()));
            }
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() - 1));
            }
        } else if (ch == '7') {
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(new Pos(pos.row() + 1, pos.col()));
            }
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() - 1));
            }
        } else if (ch == 'F') {
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(new Pos(pos.row() + 1, pos.col()));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() + 1));
            }
        } else if (ch == '|') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(new Pos(pos.row() - 1, pos.col()));
            }
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(new Pos(pos.row() + 1, pos.col()));
            }
        } else if (ch == '-') {
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() - 1));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() + 1));
            }
        } else if (ch == 'S') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(new Pos(pos.row() - 1, pos.col()));
            }
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(new Pos(pos.row() + 1, pos.col()));
            }
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() - 1));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(new Pos(pos.row(), pos.col() + 1));
            }
        }

        seen.add(pos);

        for (var way : ways) {
            if (!way.equals(last)) {
                var cycle = findCycle(map, way, numRows, numCols, seen, pos);
                if (cycle != null) {
                    cycle.add(pos);
                    return cycle;
                }
            }
        }

        return null;
    }

    record Pos(int row, int col) {
    }

}
