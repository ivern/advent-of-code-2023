package util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Coordinate(int row, int col) {

    @Contract("_ -> new")
    public @NotNull Coordinate move(Direction direction) {
        return move(direction, 1);
    }

    @Contract("_, _ -> new")
    public @NotNull Coordinate move(@NotNull Direction direction, int distance) {
        return new Coordinate(row + direction.drow * distance, col + direction.dcol * distance);
    }

}
