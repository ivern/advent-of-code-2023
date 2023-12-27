import util.DenseGrid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

@SuppressWarnings("unused")
public class Puzzle33 {

    public int solve() throws IOException {
        var lines = Files.readAllLines(Paths.get("./data/day17.txt"));
        var map = DenseGrid.fromInput(lines, Integer.class, Character::getNumericValue).get(0);
        return dijkstra(map);
    }

    int dijkstra(DenseGrid<Integer> map) {
        var seen = new HashSet<Node>();
        var heatLoss = new HashMap<Node, Integer>();

        var fringe = new PriorityQueue<>(Comparator.comparingInt(State::heatLoss));
        fringe.add(new State(new Node(new Coordinate(0, 0), Direction.RT, 0), 0));
        fringe.add(new State(new Node(new Coordinate(0, 0), Direction.DN, 0), 0));

        while (!fringe.isEmpty()) {
            var current = fringe.poll();
            if (seen.contains(current.node()) || current.heatLoss() == -1) {
                continue;
            }
            seen.add(current.node());

            if (!heatLoss.containsKey(current.node()) || current.heatLoss() < heatLoss.get(current.node())) {
                heatLoss.put(current.node(), current.heatLoss());
            }

            if (current.node().runLength() < 3) {
                fringe.add(current.advance(map));
            }

            if (current.node().direction() == Direction.UP || current.node().direction() == Direction.DN) {
                fringe.add(current.redirect(map, Direction.LT));
                fringe.add(current.redirect(map, Direction.RT));
            } else {
                fringe.add(current.redirect(map, Direction.UP));
                fringe.add(current.redirect(map, Direction.DN));
            }
        }

        var end = new Coordinate(map.numRows() - 1, map.numCols() - 1);
        return heatLoss.entrySet().stream()
                .filter(e -> e.getKey().position().equals(end))
                .mapToInt(Map.Entry::getValue)
                .min()
                .orElse(0);
    }

    record State(Node node, int heatLoss) {
        State advance(DenseGrid<Integer> map) {
            var newNode = node.advance(map);
            if (!map.contains(newNode.position().row(), newNode.position().col())) {
                return new State(newNode, -1);
            }
            var newHeatLoss = heatLoss + map.get(newNode.position().row(), newNode.position().col());
            return new State(newNode, newHeatLoss);
        }

        State redirect(DenseGrid<Integer> map, Direction newDirection) {
            var newNode = node.redirect(map, newDirection);
            if (!map.contains(newNode.position().row(), newNode.position().col())) {
                return new State(newNode, -1);
            }
            var newHeatLoss = heatLoss + map.get(newNode.position().row(), newNode.position().col());
            return new State(newNode, newHeatLoss);
        }
    }

    record Node(Coordinate position, Direction direction, int runLength) {
        Node advance(DenseGrid<Integer> map) {
            var newPosition = position.move(direction);
            return new Node(newPosition, direction, runLength + 1);
        }

        Node redirect(DenseGrid<Integer> map, Direction newDirection) {
            var newPosition = position.move(newDirection);
            return new Node(newPosition, newDirection, 1);
        }
    }

    record Coordinate(int row, int col) {
        Coordinate move(Direction d) {
            return new Coordinate(row + d.drow, col + d.dcol);
        }
    }

    enum Direction {
        UP(-1, 0),
        DN(1, 0),
        RT(0, 1),
        LT(0, -1);

        final int drow;
        final int dcol;

        Direction(int drow, int dcol) {
            this.drow = drow;
            this.dcol = dcol;
        }
    }

}
