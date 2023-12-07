import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@SuppressWarnings("unused")
public class Puzzle14 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day7.txt"));

        PriorityQueue<Input> inputs = new PriorityQueue<>(Comparator.comparingLong(Input::score));
        for (String line : lines) {
            String[] parts = line.split(" ");

            String hand = parts[0].trim();
            int bid = Integer.parseInt(parts[1].trim());
            long score = getScore(hand);

            inputs.add(new Input(hand, bid, score));
        }

        int winnings = 0;
        int rank = 1;

        while (!inputs.isEmpty()) {
            winnings += inputs.poll().bid() * rank++;
        }

        return winnings;
    }

    long getScore(String hand) {
        long handType = Arrays.stream(new Character[]{'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'})
                .mapToLong(wildIs -> getHandType(hand, wildIs))
                .max()
                .orElseThrow();
        long tiebreak = getTiebreak(hand);

        return handType * 10_000_000_000L + tiebreak;
    }

    long getHandType(String hand, Character wildIs) {
        Map<Character, Integer> counts = new HashMap<>();
        for (Character c : hand.toCharArray()) {
            if (c == 'J') {
                c = wildIs;
            }
            if (!counts.containsKey(c)) {
                counts.put(c, 1);
            } else {
                counts.put(c, counts.get(c) + 1);
            }
        }

        if (counts.size() == 1) {
            return 7;
        } else if (counts.size() == 2) {
            return counts.values().stream().anyMatch(n -> n == 4) ? 6 : 5;
        } else if (counts.size() == 3) {
            return counts.values().stream().anyMatch(n -> n == 3) ? 4 : 3;
        } else if (counts.size() == 4) {
            return 2;
        } else if (counts.size() == 5) {
            return 1;
        }

        throw new RuntimeException("bad hand type");
    }

    long getTiebreak(String hand) {
        long value = 0;

        for (Character c : hand.toCharArray()) {
            value *= 16;

            if (Character.isDigit(c)) {
                value += Character.getNumericValue(c);
            } else {
                value += switch (c) {
                    case 'T' -> 10;
                    case 'J' -> 1;
                    case 'Q' -> 12;
                    case 'K' -> 13;
                    case 'A' -> 14;
                    default -> throw new RuntimeException("bad card");
                };
            }
        }

        return value;
    }

    public record Input(String hand, int bid, long score) {
    }

}
