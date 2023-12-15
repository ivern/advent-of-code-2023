import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

@SuppressWarnings("unused")
public class Puzzle30 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day15.txt"));
        var steps = lines.stream().flatMap(s -> Arrays.stream(s.split(","))).map(Step::parse).toList();

        Box[] boxes = new Box[256];
        for (int i = 0; i < 256; ++i) {
            boxes[i] = new Box(i, new LinkedList<>());
        }

        for (var step : steps) {
            var box = boxes[step.labelHash()];
            if (step.operation() == '=') {
                var lenses = new ArrayList<>(box.lenses());
                box.lenses().clear();

                int i = 0;
                while (i < lenses.size() && !lenses.get(i).label().equals(step.label())) {
                    box.lenses().add(lenses.get(i));
                    ++i;
                }

                box.lenses().add(new Lens(step.focalLength(), step.label()));

                while (++i < lenses.size()) {
                    box.lenses().add(lenses.get(i));
                }
            } else {
                box.lenses().removeIf(lens -> lens.label().equals(step.label()));
            }
        }

        return Arrays.stream(boxes).mapToLong(Box::focusingPower).sum();
    }

    static int hash(String s) {
        byte[] ascii = StandardCharsets.US_ASCII.encode(s).array();
        int hash = 0;

        for (byte b : ascii) {
            hash = ((hash + b) * 17) % 256;
        }

        return hash;
    }

    record Box(int number, LinkedList<Lens> lenses) {
        long focusingPower() {
            long power = 0;

            for (int i = 0; i < lenses.size(); ++i) {
                power += (1 + number) * (i + 1) * (long) lenses.get(i).focalLength();
            }

            return power;
        }
    }

    record Lens(int focalLength, String label) {
    }

    record Step(String label, char operation, int focalLength, int labelHash) {
        static Step parse(String s) {
            if (s.contains("=")) {
                String[] parts = s.split("=");
                return new Step(parts[0], '=', Integer.parseInt(parts[1]), Puzzle30.hash(parts[0]));
            } else if (s.contains("-")) {
                String label = s.substring(0, s.length() - 1);
                return new Step(label, '-', -1, Puzzle30.hash(label));
            }
            throw new IllegalArgumentException();
        }
    }

}
