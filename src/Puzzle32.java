import util.DenseGrid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Puzzle32 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day16.txt"));
        var grid = DenseGrid.fromInput(lines).get(0);

        var rays = new LinkedList<Ray>();
        for (int row = 0; row < grid.numRows(); ++row) {
            rays.add(new Ray(row, 0, Direction.RT));
            rays.add(new Ray(row, grid.numCols() - 1, Direction.LT));
        }
        for (int col = 0; col < grid.numCols(); ++col) {
            rays.add(new Ray(0, col, Direction.DN));
            rays.add(new Ray(grid.numRows() - 1, col, Direction.UP));
        }

        return rays.stream().mapToInt(ray -> solve(grid, ray)).max().orElse(-1);
    }

    int solve(DenseGrid<Character> grid, Ray initialRay) {
        var seen = new HashSet<Ray>();
        var rays = new LinkedList<Ray>();
        rays.addLast(initialRay);

        while (!rays.isEmpty()) {
            var ray = rays.removeFirst();

            if (seen.contains(ray) || !grid.contains(ray.row(), ray.col())) {
                continue;
            }
            seen.add(ray);

            switch (grid.get(ray.row(), ray.col())) {
                case '.':
                    rays.add(ray.advance());
                    break;

                case '/':
                    rays.add(ray.redirect(switch (ray.dir()) {
                        case UP -> Direction.RT;
                        case DN -> Direction.LT;
                        case LT -> Direction.DN;
                        case RT -> Direction.UP;
                    }));
                    break;

                case '\\':
                    rays.add(ray.redirect(switch (ray.dir()) {
                        case UP -> Direction.LT;
                        case DN -> Direction.RT;
                        case LT -> Direction.UP;
                        case RT -> Direction.DN;
                    }));
                    break;

                case '|':
                    if (ray.dir() == Direction.UP || ray.dir() == Direction.DN) {
                        rays.add(ray.advance());
                    } else {
                        rays.add(ray.redirect(Direction.UP));
                        rays.add(ray.redirect(Direction.DN));
                    }
                    break;

                case '-':
                    if (ray.dir() == Direction.RT || ray.dir() == Direction.LT) {
                        rays.add(ray.advance());
                    } else {
                        rays.add(ray.redirect(Direction.RT));
                        rays.add(ray.redirect(Direction.LT));
                    }
                    break;

                default:
                    throw new RuntimeException();
            }
        }

        return seen.stream().map(ray -> ray.row() + "_" + ray.col()).collect(Collectors.toSet()).size();
    }

    record Ray(int row, int col, Direction dir) {
        Ray advance() {
            return redirect(dir);
        }

        Ray redirect(Direction d) {
            return new Ray(row + d.drow, col + d.dcol, d);
        }
    }

    enum Direction {
        UP(-1, 0),
        DN(1, 0),
        RT(0, 1),
        LT(0, -1);

        final int drow;
        final int dcol;

        Direction(int drow, int dcol) {
            this.drow = drow;
            this.dcol = dcol;
        }
    }

}
