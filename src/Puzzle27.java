import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle27 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day14.txt"));

        int numRows = lines.size();
        int numCols = lines.get(0).length();

        Rock[][] platform = new Rock[numRows][];
        for (int row = 0; row < numRows; ++row) {
            platform[row] = new Rock[numCols];
            for (int col = 0; col < numCols; ++col) {
                platform[row][col] = switch (lines.get(row).charAt(col)) {
                    case 'O' -> Rock.SPHERE;
                    case '#' -> Rock.CUBE;
                    case '.' -> Rock.EMPTY;
                    default -> throw new RuntimeException();
                };
            }
        }

//        print(platform, numRows, numCols);
        tiltNorth(platform, numRows, numCols);
//        print(platform, numRows, numCols);

        return northLoad(platform, numRows, numCols);
    }

    void tiltNorth(Rock[][] platform, int numRows, int numCols) {
        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (platform[row][col] == Rock.SPHERE) {
                    slide(platform, numRows, numCols, row, col, -1, 0);
                }
            }
        }
    }

    void slide(Rock[][] platform, int numRows, int numCols, int row, int col, int drow, int dcol) {
        int nextRow = row + drow;
        int nextCol = col + dcol;

        while (nextRow >= 0 && nextRow < numRows && nextCol >= 0 && nextCol < numCols && platform[nextRow][nextCol] == Rock.EMPTY) {
            platform[nextRow][nextCol] = platform[row][col];
            platform[row][col] = Rock.EMPTY;
            nextRow += drow;
            nextCol += dcol;
            row += drow;
            col += dcol;
        }
    }

    long northLoad(Rock[][] platform, int numRows, int numCols) {
        long load = 0;

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                if (platform[row][col] == Rock.SPHERE) {
                    load += numRows - row;
                }
            }
        }

        return load;
    }

    void print(Rock[][] platform, int numRows, int numCols) {
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                builder.append(switch (platform[row][col]) {
                    case SPHERE -> 'O';
                    case CUBE -> '#';
                    case EMPTY -> '.';
                });
            }
            builder.append("\n");
        }

        System.out.println(builder.toString());
    }

    enum Rock {
        EMPTY,
        SPHERE,
        CUBE
    }

}
