import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;
import static java.lang.Math.min;

@SuppressWarnings("unused")
public class Puzzle26 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day13.txt"));

        long summary = 0;

        int nextLine = 0;
        while (nextLine < lines.size()) {
            String line = null;
            while (nextLine < lines.size() && (line = lines.get(nextLine++)).isBlank()) {
            }

            List<String> pattern = new ArrayList<>();
            do {
                pattern.add(line);
            } while (nextLine < lines.size() && !(line = lines.get(nextLine++)).isBlank());

            int numRows = pattern.size();
            int numCols = pattern.get(0).length();

            Set<Rock> rocks = new HashSet<>();
            for (int y = 0; y < numRows; ++y) {
                String row = pattern.get(y);
                for (int x = 0; x < numCols; ++x) {
                    if (row.charAt(x) == '#') {
                        rocks.add(new Rock(x, y));
                    }
                }
            }

            int originalCol = findReflectionAxisColumn(rocks, numCols, -1);
            int originalRow = findReflectionAxisRow(rocks, numRows, -1);

            int alternativeCol = -1;
            int alternativeRow = -1;

            loops:
            for (int y = 0; y < numRows; ++y) {
                String row = pattern.get(y);
                for (int x = 0; x < numCols; ++x) {
                    Rock rock = new Rock(x, y);
                    if (row.charAt(x) == '.') {
                        rocks.add(rock);
                        alternativeRow = findReflectionAxisRow(rocks, numRows, originalRow);
                        alternativeCol = findReflectionAxisColumn(rocks, numCols, originalCol);
                        rocks.remove(rock);
                    } else {
                        rocks.remove(rock);
                        alternativeRow = findReflectionAxisRow(rocks, numRows, originalRow);
                        alternativeCol = findReflectionAxisColumn(rocks, numCols, originalCol);
                        rocks.add(rock);
                    }

                    if (alternativeCol != -1 || alternativeRow != -1) {
                        break loops;
                    }
                }
            }

            if (alternativeCol != -1) {
                summary += alternativeCol;
            } else if (alternativeRow != -1) {
                summary += alternativeRow * 100L;
            } else if (originalCol != -1) {
                summary += originalCol;
            } else if (originalRow != -1) {
                summary += originalRow * 100L;
            }
        }

        return summary;
    }

    int findReflectionAxisColumn(Set<Rock> rocks, int numCols, int originalCol) {
        for (int col = 1; col < numCols; ++col) {
            if (col == originalCol) {
                continue;
            }

            Set<Rock> trimmed = new HashSet<>();
            Set<Rock> folded = new HashSet<>();
            int halfWidth = min(col, numCols - col);

            for (Rock rock : rocks) {
                if (rock.x() >= col - halfWidth && rock.x() < col + halfWidth) {
                    trimmed.add(rock);
                    if (rock.x() < col) {
                        folded.add(new Rock(abs(rock.x() - col), rock.y()));
                    } else {
                        folded.add(new Rock(rock.x() - col + 1, rock.y()));
                    }
                }
            }

            if (trimmed.size() == folded.size() * 2) {
                return col;
            }
        }

        return -1;
    }

    int findReflectionAxisRow(Set<Rock> rocks, int numRows, int originalRow) {
        for (int row = 1; row < numRows; ++row) {
            if (row == originalRow) {
                continue;
            }

            Set<Rock> trimmed = new HashSet<>();
            Set<Rock> folded = new HashSet<>();
            int halfHeight = min(row, numRows - row);

            for (Rock rock : rocks) {
                if (rock.y() >= row - halfHeight && rock.y() < row + halfHeight) {
                    trimmed.add(rock);
                    if (rock.y() < row) {
                        folded.add(new Rock(rock.x(), abs(rock.y() - row)));
                    } else {
                        folded.add(new Rock(rock.x(), abs(rock.y() - row + 1)));
                    }
                }
            }

            if (trimmed.size() == folded.size() * 2) {
                return row;
            }
        }

        return -1;
    }

    record Rock(int x, int y) {
    }

}
