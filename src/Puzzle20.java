import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        var cycle = findCycle(lines, Pos.of(startRow, startCol, lines), numRows, numCols, new HashSet<>(), Pos.of(-1, -1, lines));
        char startPipe = findStartPipe(cycle);
        var start = cycle.removeLast();
        cycle.addLast(new Pos(start.row(), start.col(), startPipe));

        var cycleDirection = findCycleDirection(cycle);
        var insideTiles = findInsideTiles(cycle, numRows, numCols, cycleDirection);

        return insideTiles.size();
    }

    Set<Pos> findInsideTiles(List<Pos> cycle, int numRows, int numCols, CycleDirection cycleDirection) {
        var tiles = new HashSet<Pos>();

        for (int i = 0; i < cycle.size(); ++i) {
            var current = cycle.get(i);
            var next = cycle.get((i + 1) % cycle.size());
            var dir = current.directionTo(next);

            if (cycleDirection == CycleDirection.CLOCKWISE) {
                if (current.pipe() == '|') {
                    if (dir == Direction.UP) {
                        tiles.add(current.e());
                    } else {
                        tiles.add(current.w());
                    }
                } else if (current.pipe() == '-') {
                    if (dir == Direction.RT) {
                        tiles.add(current.s());
                    } else {
                        tiles.add(current.n());
                    }
                } else if (current.pipe() == 'L') {
                    if (dir == Direction.RT) {
                        tiles.add(current.w());
                        tiles.add(current.s());
                        tiles.add(current.sw());
                    } else {
                        tiles.add(current.ne());
                    }
                } else if (current.pipe() == 'J') {
                    if (dir == Direction.UP) {
                        tiles.add(current.e());
                        tiles.add(current.s());
                        tiles.add(current.se());
                    } else {
                        tiles.add(current.nw());
                    }
                } else if (current.pipe() == '7') {
                    if (dir == Direction.LT) {
                        tiles.add(current.n());
                        tiles.add(current.e());
                        tiles.add(current.ne());
                    } else {
                        tiles.add(current.sw());
                    }
                } else if (current.pipe() == 'F') {
                    if (dir == Direction.DN) {
                        tiles.add(current.w());
                        tiles.add(current.n());
                        tiles.add(current.nw());
                    } else {
                        tiles.add(current.se());
                    }
                }
            } else {
                if (current.pipe() == '|') {
                    if (dir == Direction.DN) {
                        tiles.add(current.e());
                    } else {
                        tiles.add(current.w());
                    }
                } else if (current.pipe() == '-') {
                    if (dir == Direction.LT) {
                        tiles.add(current.s());
                    } else {
                        tiles.add(current.n());
                    }
                } else if (current.pipe() == 'L') {
                    if (dir == Direction.UP) {
                        tiles.add(current.w());
                        tiles.add(current.s());
                        tiles.add(current.sw());
                    } else {
                        tiles.add(current.ne());
                    }
                } else if (current.pipe() == 'J') {
                    if (dir == Direction.LT) {
                        tiles.add(current.e());
                        tiles.add(current.s());
                        tiles.add(current.se());
                    } else {
                        tiles.add(current.nw());
                    }
                } else if (current.pipe() == '7') {
                    if (dir == Direction.DN) {
                        tiles.add(current.n());
                        tiles.add(current.e());
                        tiles.add(current.ne());
                    } else {
                        tiles.add(current.sw());
                    }
                } else if (current.pipe() == 'F') {
                    if (dir == Direction.RT) {
                        tiles.add(current.w());
                        tiles.add(current.n());
                        tiles.add(current.nw());
                    } else {
                        tiles.add(current.se());
                    }
                }
            }
        }

        var cyclePos = cycle.stream().map(p -> new Pos(p.row, p.col, (char) 0)).collect(Collectors.toSet());
        tiles.removeIf(pos -> cyclePos.contains(pos) || !pos.isValid(numRows, numCols));

        foodFill(cyclePos, numRows, numCols, tiles);

        return tiles;
    }

    private void foodFill(Set<Pos> cyclePos, int numRows, int numCols, Set<Pos> tiles) {
        var newTiles = new HashSet<Pos>();
        var seen = new HashSet<>(cyclePos);
        seen.addAll(tiles);

        for (var t : tiles) {
            newTiles.addAll(floodfill(tiles, t, numRows, numCols, seen));
        }

        tiles.addAll(newTiles);
    }

    private Set<Pos> floodfill(Set<Pos> tiles, Pos pos, int numRows, int numCols, Set<Pos> seen) {
        var newTiles = new HashSet<Pos>();

        for (var p : new Pos[]{pos.n(), pos.s(), pos.e(), pos.w()}) {
            if (!seen.contains(p) && p.isValid(numRows, numCols)) {
                seen.add(p);
                newTiles.add(p);
                newTiles.addAll(floodfill(tiles, p, numRows, numCols, seen));
            }
        }

        return newTiles;
    }

    CycleDirection findCycleDirection(List<Pos> cycle) {
        int count = 0;
        for (int i = 0; i < cycle.size(); ++i) {
            var current = cycle.get(i);
            var next = cycle.get((i + 1) % cycle.size());
            var dir = current.directionTo(next);

            if (current.pipe() == 'L') {
                count += (dir == Direction.UP) ? 1 : -1;
            } else if (current.pipe() == 'J') {
                count += (dir == Direction.LT) ? 1 : -1;
            } else if (current.pipe() == '7') {
                count += (dir == Direction.DN) ? 1 : -1;
            } else if (current.pipe() == 'F') {
                count += (dir == Direction.RT) ? 1 : -1;
            }
        }

        if (count == 4) {
            return CycleDirection.CLOCKWISE;
        } else if (count == -4) {
            return CycleDirection.COUNTER_CLOCKWISE;
        }

        throw new RuntimeException();
    }

    List<Pos> findCycle(List<String> map, Pos pos, int numRows, int numCols, Set<Pos> seen, Pos last) {
        if (seen.contains(pos)) {
            return new ArrayList<>();
        }

        char ch = charAt(map, pos.row(), pos.col());
        if (ch == '.' || ch == 0) {
            return null;
        }

        char up = (pos.row() > 0) ? map.get(pos.row() - 1).charAt(pos.col()) : '.';
        char dn = (pos.row() < numRows - 1) ? map.get(pos.row() + 1).charAt(pos.col()) : '.';
        char lt = (pos.col() > 0) ? map.get(pos.row()).charAt(pos.col() - 1) : '.';
        char rt = (pos.col() < numCols - 1) ? map.get(pos.row()).charAt(pos.col() + 1) : '.';

        var ways = new ArrayList<Pos>();

        if (ch == 'L') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(Pos.of(pos.row() - 1, pos.col(), map));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() + 1, map));
            }
        } else if (ch == 'J') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(Pos.of(pos.row() - 1, pos.col(), map));
            }
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() - 1, map));
            }
        } else if (ch == '7') {
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(Pos.of(pos.row() + 1, pos.col(), map));
            }
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() - 1, map));
            }
        } else if (ch == 'F') {
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(Pos.of(pos.row() + 1, pos.col(), map));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() + 1, map));
            }
        } else if (ch == '|') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(Pos.of(pos.row() - 1, pos.col(), map));
            }
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(Pos.of(pos.row() + 1, pos.col(), map));
            }
        } else if (ch == '-') {
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() - 1, map));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() + 1, map));
            }
        } else if (ch == 'S') {
            if (up == '7' || up == 'F' || up == '|' || up == 'S') {
                ways.add(Pos.of(pos.row() - 1, pos.col(), map));
            }
            if (dn == 'L' || dn == 'J' || dn == '|' || dn == 'S') {
                ways.add(Pos.of(pos.row() + 1, pos.col(), map));
            }
            if (lt == 'L' || lt == 'F' || lt == '-' || lt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() - 1, map));
            }
            if (rt == '7' || rt == 'J' || rt == '-' || rt == 'S') {
                ways.add(Pos.of(pos.row(), pos.col() + 1, map));
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

    char findStartPipe(List<Pos> cycle) {
        var start = cycle.get(cycle.size() - 1);
        var before = cycle.get(cycle.size() - 2);
        var after = cycle.get(0);

        var bdir = start.directionTo(before);
        var adir = start.directionTo(after);

        if (bdir == Direction.UP && adir == Direction.DN) {
            return '|';
        } else if (bdir == Direction.LT && adir == Direction.RT) {
            return '-';
        } else if (bdir == Direction.LT && adir == Direction.UP) {
            return 'J';
        } else if (bdir == Direction.LT && adir == Direction.DN) {
            return '7';
        } else if (bdir == Direction.RT && adir == Direction.UP) {
            return 'L';
        } else if (bdir == Direction.RT && adir == Direction.DN) {
            return 'F';
        } else if (adir == Direction.LT && bdir == Direction.UP) {
            return 'J';
        } else if (adir == Direction.LT && bdir == Direction.DN) {
            return '7';
        } else if (adir == Direction.RT && bdir == Direction.UP) {
            return 'L';
        } else if (adir == Direction.RT && bdir == Direction.DN) {
            return 'F';
        }

        throw new RuntimeException();
    }

    static char charAt(List<String> map, int row, int col) {
        if (col < 0 || row < 0 || col >= map.get(0).length() || row >= map.size()) {
            return 0;
        }

        return map.get(row).charAt(col);
    }

    record Pos(int row, int col, char pipe) {
        static Pos of(int row, int col, List<String> map) {
            return new Pos(row, col, charAt(map, row, col));
        }

        boolean isValid(int numRows, int numCols) {
            return row >= 0 && row < numRows && col >= 0 && col < numCols;
        }

        Direction directionTo(Pos p) {
            if (row < p.row) {
                return Direction.DN;
            } else if (row > p.row) {
                return Direction.UP;
            } else if (col < p.col) {
                return Direction.RT;
            } else {
                return Direction.LT;
            }
        }

        Pos n() {
            return new Pos(row - 1, col, (char) 0);
        }

        Pos s() {
            return new Pos(row + 1, col, (char) 0);
        }

        Pos e() {
            return new Pos(row, col + 1, (char) 0);
        }

        Pos w() {
            return new Pos(row, col - 1, (char) 0);
        }

        Pos ne() {
            return new Pos(row - 1, col + 1, (char) 0);
        }

        Pos se() {
            return new Pos(row + 1, col + 1, (char) 0);
        }

        Pos nw() {
            return new Pos(row - 1, col - 1, (char) 0);
        }

        Pos sw() {
            return new Pos(row + 1, col - 1, (char) 0);
        }

    }

    enum Direction {
        UP, DN, LT, RT;
    }

    enum CycleDirection {
        CLOCKWISE, COUNTER_CLOCKWISE;
    }

}
