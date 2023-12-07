import util.Counter;
import util.Encoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.PriorityQueue;

@SuppressWarnings("unused")
public class Puzzle13 {

    private static final Encoder ENCODER = new Encoder("23456789TJQKA");

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day7.txt"));
        var inputs = new PriorityQueue<>(Comparator.comparingInt(Input::hand).thenComparingLong(Input::tiebreak));

        for (String line : lines) {
            String[] parts = line.split(" ");

            String hand = parts[0].trim();
            int bid = Integer.parseInt(parts[1].trim());

            inputs.add(new Input(getHandType(hand), ENCODER.encode(hand), bid));
        }

        int winnings = 0;
        int rank = 1;

        while (!inputs.isEmpty()) {
            winnings += inputs.poll().bid() * rank++;
        }

        return winnings;
    }

    int getHandType(String hand) {
        var counts = Counter.countCharacters(hand).descending();

        if (counts.size() == 1) {
            return 7;
        } else if (counts.size() == 2) {
            return counts.get(0).getValue() == 4 ? 6 : 5;
        } else if (counts.size() == 3) {
            return counts.get(0).getValue() == 3 ? 4 : 3;
        } else if (counts.size() == 4) {
            return 2;
        } else if (counts.size() == 5) {
            return 1;
        }

        throw new RuntimeException("bad hand type");
    }

    private record Input(int hand, long tiebreak, int bid) {
    }

}
