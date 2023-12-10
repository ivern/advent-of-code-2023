import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Puzzle20 {

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

        var in = inside(lines, new Pos(startRow, startCol), numRows, numCols, new HashSet<>(), new Pos(-1, -1));

        var seen = new HashSet<Pos>();
        for (var p : in) {
            fill(p, lines, seen);
        }
        in.addAll(seen);

        return in.size();
    }

    void fill(Pos pos, List<String> map, Set<Pos> seen) {
        if (seen.contains(pos)) {
            return;
        }
        seen.add(pos);

        var up = pos.up();
        if (charAt(map, up) == '.') {
            fill(up, map, seen);
        }

        var dn = pos.dn();
        if (charAt(map, dn) == '.') {
            fill(dn, map, seen);
        }

        var lt = pos.lt();
        if (charAt(map, lt) == '.') {
            fill(lt, map, seen);
        }

        var rt = pos.rt();
        if (charAt(map, rt) == '.') {
            fill(rt, map, seen);
        }
    }

    Set<Pos> inside(List<String> map, Pos pos, int numRows, int numCols, Set<Pos> seen, Pos last) {
        if (seen.contains(pos)) {
            return new HashSet<>();
        }

        char ch = charAt(map, pos);
        if (ch == '.' || ch == 0) {
            return null;
        }

        var ways = new ArrayList<Way>();

        if (ch == 'L') {
            up(pos, ways, map);
            rt(pos, ways, map);
        } else if (ch == 'J') {
            up(pos, ways, map);
            lt(pos, ways, map);
        } else if (ch == '7') {
            dn(pos, ways, map);
            lt(pos, ways, map);
        } else if (ch == 'F') {
            dn(pos, ways, map);
            rt(pos, ways, map);
        } else if (ch == '|') {
            up(pos, ways, map);
            dn(pos, ways, map);
        } else if (ch == '-') {
            lt(pos, ways, map);
            rt(pos, ways, map);
        } else if (ch == 'S') {
            up(pos, ways, map);
            dn(pos, ways, map);
            lt(pos, ways, map);
            rt(pos, ways, map);
        }

        seen.add(pos);

        for (var way : ways) {
            if (!way.pos().equals(last)) {
                var in = inside(map, way.pos(), numRows, numCols, seen, pos);
                if (in != null) {
                    in.addAll(way.inside());
                    return in;
                }
            }
        }

        return null;
    }

    List<Pos> in(Pos pos, char c, Direction d) {
        switch (c) {
            case 'L':
                break;
            case 'L':
                break;
            case 'L':
                break;
            case 'L':
                break;
            case 'L':
                break;
        }
    }

    void up(Pos pos, List<Way> ways, List<String> map) {
        var p = pos.up();
        char up = charAt(map, p);
        if (up == '7' || up == 'F' || up == '|' || up == 'S') {
            var in = p.lt();
            ways.add(new Way(p, (charAt(map, in) == '.') ? in : null));
        }
    }

    void dn(Pos pos, List<Way> ways, List<String> map) {
        var p = pos.dn();
        char dn = charAt(map, p);
        if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
            var in = p.rt();
            ways.add(new Way(p, (charAt(map, in) == '.') ? in : null));
        }
    }

    void lt(Pos pos, List<Way> ways, List<String> map) {
        var p = pos.lt();
        char lt = charAt(map, p);
        if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
            var in = p.dn();
            ways.add(new Way(p, (charAt(map, in) == '.') ? in : null));
        }
    }

    void rt(Pos pos, List<Way> ways, List<String> map) {
        var p = pos.rt();
        char rt = charAt(map, p);
        if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
            var in = p.up();
            ways.add(new Way(p, (charAt(map, in) == '.') ? in : null));
        }
    }

    char charAt(List<String> map, Pos pos) {
        if (pos.row < 0 || pos.col < 0 || pos.row >= map.size() || pos.col >= map.get(0).length()) {
            return 0;
        }

        return map.get(pos.row).charAt(pos.col);
    }

    record Pos(int row, int col) {
        Pos up() {
            return new Pos(row - 1, col);
        }

        Pos dn() {
            return new Pos(row + 1, col);
        }

        Pos lt() {
            return new Pos(row, col - 1);
        }

        Pos rt() {
            return new Pos(row, col + 1);
        }
    }

    record Way(Pos pos, List<Pos> inside) {
    }

    enum Direction {
        UP, DN, LT, RT;
    }

}
