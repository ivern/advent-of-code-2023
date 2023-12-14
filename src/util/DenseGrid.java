package util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DenseGrid<T> {

    private final Class<T> klass;
    private final T[][] grid;
    private final int numRows;
    private final int numCols;

    @SuppressWarnings("unchecked")
    public DenseGrid(@NotNull Class<T> klass, int numRows, int numCols) {
        this.klass = klass;
        this.numRows = numRows;
        this.numCols = numCols;

        this.grid = (T[][]) Array.newInstance(klass.arrayType(), numRows);
        for (int i = 0; i < numRows; ++i) {
            this.grid[i] = (T[]) Array.newInstance(klass, numCols);
        }
    }

    public DenseGrid(Class<T> klass, int numRows, int numCols, T defaultValue) {
        this(klass, numRows, numCols);

        for (int i = 0; i < numRows; ++i) {
            Arrays.fill(this.grid[i], defaultValue);
        }
    }

    public static @NotNull List<DenseGrid<Character>> fromInput(List<String> data) {
        return fromInput(data, Character.class, Function.identity());
    }

    public static <T> @NotNull List<DenseGrid<T>> fromInput(@NotNull List<String> data, Class<T> klass, Function<Character, T> valueFn) {
        List<DenseGrid<T>> grids = new ArrayList<>();
        int nextLine = 0;

        while (nextLine < data.size()) {
            while (nextLine < data.size() && data.get(nextLine).isBlank()) {
                ++nextLine;
            }
            int startLine = nextLine;

            while (nextLine < data.size() && !data.get(nextLine).isBlank()) {
                ++nextLine;
            }

            int numRows = nextLine - startLine;
            int numCols = data.get(startLine).length();
            DenseGrid<T> grid = new DenseGrid<>(klass, numRows, numCols);

            for (int row = startLine; row < nextLine; ++row) {
                String line = data.get(row);
                for (int col = 0; col < numCols; ++col) {
                    grid.put(row, col, valueFn.apply(line.charAt(col)));
                }
            }

            grids.add(grid);
        }

        return grids;
    }

    public int numRows() {
        return numRows;
    }

    public int numCols() {
        return numCols;
    }

    public T put(int row, int col, T value) {
        T oldValue = grid[row][col];
        grid[row][col] = value;
        return oldValue;
    }

    public T get(int row, int col) {
        return grid[row][col];
    }

    public boolean contains(int row, int col) {
        return col >= 0 && col < numCols && row >= 0 && row < numRows;
    }

    public DenseGrid<T> transpose() {
        DenseGrid<T> transposed = new DenseGrid<>(klass, numCols, numRows);

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                transposed.grid[row][col] = grid[col][row];
            }
        }

        return transposed;
    }

    public <U> U mapReduce(Function<T, U> map, BiFunction<U, U, U> reduce, U initialValue) {
        return mapReduce((grid, row, col) -> map.apply(grid.get(row, col)), reduce, initialValue);
    }

    public <U> U mapReduce(CellMapper<T, U> map, BiFunction<U, U, U> reduce, U initialValue) {
        U result = initialValue;

        for (int row = 0; row < numRows; ++row) {
            for (int col = 0; col < numCols; ++col) {
                result = reduce.apply(result, map.map(this, row, col));
            }
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenseGrid<?> denseGrid = (DenseGrid<?>) o;
        return numRows == denseGrid.numRows && numCols == denseGrid.numCols && Objects.equals(klass, denseGrid.klass)
                && Arrays.deepEquals(grid, denseGrid.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(klass, numRows, numCols);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
    }

    @Override
    public String toString() {
        boolean first = true;
        StringBuilder builder = new StringBuilder();

        for (int row = 0; row < numRows; ++row) {
            if (first) {
                first = false;
            } else {
                builder.append("\n");
            }

            for (int col = 0; col < numCols; ++col) {
                builder.append(grid[row][col]);
            }
        }

        return builder.toString();
    }

    @FunctionalInterface
    public interface CellMapper<T, U> {
        U map(DenseGrid<T> grid, int row, int col);
    }

}
