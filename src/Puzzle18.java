import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("unused")
public class Puzzle18 {

    public long solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day9.txt"));
        return lines.stream().mapToLong(this::extrapolate).sum();
    }

    private long extrapolate(String sequence) {
        return extrapolate(new ArrayList<>(
                Arrays.stream(sequence.split(" "))
                        .map(String::trim)
                        .map(Long::parseLong)
                        .toList()));
    }

    private long extrapolate(List<Long> sequence) {
        Stack<List<Long>> stack = new Stack<>();
        stack.push(sequence);

        while (!sequence.stream().allMatch(n -> n == 0)) {
            List<Long> next = new ArrayList<>();
            for (int i = 1; i < sequence.size(); ++i) {
                next.add(sequence.get(i) - sequence.get(i - 1));
            }
            stack.push(next);
            sequence = next;
        }

        sequence = stack.pop();
        while (!stack.isEmpty()) {
            long step = sequence.getFirst();
            sequence = stack.pop();
            sequence.addFirst(sequence.getFirst() - step);
        }

        return sequence.getFirst();
    }

}
