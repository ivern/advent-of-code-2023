package util;

public record Coordinate(int row, int col) {

    public Coordinate move(Direction direction) {
        return move(direction, 1);
    }

    public Coordinate move(Direction direction, int distance) {
        return new Coordinate(row + direction.drow * distance, col + direction.dcol * distance);
    }

}
