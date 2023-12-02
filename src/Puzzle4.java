import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle4 {

    public int solve() {
        try (var lines = Files.lines(Paths.get("./input/puzzle4.txt"))) {
            return lines.mapToInt(this::getGamePower).sum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getGamePower(String text) {
        String[] textParts = text.split(":");
        String[] reveals = textParts[1].trim().split(";");

        int minRed = 0;
        int minGreen = 0;
        int minBlue = 0;

        for (String reveal : reveals) {
            String[] cubeCounts = reveal.trim().split(",");

            for (String cubeCount : cubeCounts) {
                String[] cubeCountParts = cubeCount.trim().split(" ");

                int count = Integer.parseInt(cubeCountParts[0].trim());
                String color = cubeCountParts[1].trim();

                if (color.equals("blue") && count > minBlue) {
                    minBlue = count;
                } else if (color.equals("green") && count > minGreen) {
                    minGreen = count;
                } else if (color.equals("red") && count > minRed) {
                    minRed = count;
                }
            }
        }

        return minRed * minGreen * minBlue;
    }

}
