import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Puzzle21 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day11.txt"));

        int numRows = lines.size();
        int numCols = lines.get(0).length();
        Set<Integer> emptyCols = new HashSet<>();

        for (int col = 0; col < numCols; ++col) {
            boolean empty = true;
            for (int row = 0; row < numRows; ++row) {
                if (lines.get(row).charAt(col) == '#') {
                    empty = false;
                }
            }
            if (empty) {
                emptyCols.add(col);
            }
        }

        int y = 0;

        List<Coordinate> galaxyPositions = new ArrayList<>();

        for (int row = 0; row < numRows; ++row) {
            int x = 0;
            boolean empty = true;
            for (int col = 0; col < numCols; ++col) {
                if (lines.get(row).charAt(col) == '#') {
                    empty = false;
                    galaxyPositions.add(new Coordinate(x, y));
                }
                x += emptyCols.contains(col) ? 2 : 1;
            }
            y += empty ? 2 : 1;
        }

        int sum = 0;
        for (int i = 0; i < galaxyPositions.size(); ++i) {
            var g = galaxyPositions.get(i);
            for (int j = i + 1; j < galaxyPositions.size(); ++j) {
                sum += g.distance(galaxyPositions.get(j));
            }
        }

        return sum;
    }

    record Coordinate(int x, int y) {
        int distance(Coordinate c) {
            return abs(x - c.x) + abs(y - c.y);
        }
    }

}
