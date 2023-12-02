import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class Puzzle3 {

    public int solve() {
        try (var lines = Files.lines(Paths.get("./input/puzzle3.txt"))) {
            return lines.mapToInt(this::getGameNumberOrZero).sum();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getGameNumberOrZero(String text) {
        String[] textParts = text.split(":");
        String[] reveals = textParts[1].trim().split(";");

        for (String reveal : reveals) {
            String[] cubeCounts = reveal.trim().split(",");

            for (String cubeCount : cubeCounts) {
                String[] cubeCountParts = cubeCount.trim().split(" ");

                int count = Integer.parseInt(cubeCountParts[0].trim());
                String color = cubeCountParts[1].trim();

                if (color.equals("blue") && count > 14
                        || color.equals("green") && count > 13
                        || color.equals("red") && count > 12) {
                    return 0;
                }
            }
        }

        return Integer.parseInt(textParts[0].split(" ")[1]);
    }

}
