import util.DenseGrid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Puzzle28 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day14.txt"));

        var platform = DenseGrid.fromInput(lines).get(0);

//        System.out.println(platform);

        Map<Integer, Integer> lastSeen = new HashMap<>();

        for (int i = 0; i < 1_000_000_000; ++i) {
            cycle(platform);

            Integer cycleStart = lastSeen.put(platform.hashCode(), i);

            if (cycleStart != null) {
                int cycleLength = i - cycleStart + 1;
                if (((1_000_000_000 - cycleStart) % cycleLength) == 0) {
                    break;
                }
            }
        }

//        System.out.println(platform);

        return northLoad(platform);
    }

    void cycle(DenseGrid<Character> platform) {
        for (int row = 0; row < platform.numRows(); ++row) {
            for (int col = 0; col < platform.numCols(); ++col) {
                if (platform.get(row, col) == 'O') {
                    slide(platform, row, col, -1, 0);
                }
            }
        }

        for (int col = 0; col < platform.numCols(); ++col) {
            for (int row = 0; row < platform.numRows(); ++row) {
                if (platform.get(row, col) == 'O') {
                    slide(platform, row, col, 0, -1);
                }
            }
        }

        for (int row = platform.numRows() - 1; row >= 0; --row) {
            for (int col = 0; col < platform.numCols(); ++col) {
                if (platform.get(row, col) == 'O') {
                    slide(platform, row, col, 1, 0);
                }
            }
        }

        for (int col = platform.numCols() - 1; col >= 0; --col) {
            for (int row = 0; row < platform.numRows(); ++row) {
                if (platform.get(row, col) == 'O') {
                    slide(platform, row, col, 0, 1);
                }
            }
        }
    }

    void slide(DenseGrid<Character> platform, int row, int col, int drow, int dcol) {
        int nextRow = row + drow;
        int nextCol = col + dcol;

        while (platform.contains(nextRow, nextCol) && platform.get(nextRow, nextCol) == '.') {
            platform.put(nextRow, nextCol, platform.put(row, col, '.'));
            row = nextRow;
            col = nextCol;
            nextRow += drow;
            nextCol += dcol;
        }
    }

    long northLoad(DenseGrid<Character> platform) {
        long load = 0;

        for (int row = 0; row < platform.numRows(); ++row) {
            for (int col = 0; col < platform.numCols(); ++col) {
                if (platform.get(row, col) == 'O') {
                    load += platform.numRows() - row;
                }
            }
        }

        return load;
    }

}
