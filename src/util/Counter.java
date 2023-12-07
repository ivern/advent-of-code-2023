package util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Counter<T> {

    private final Comparator<Map.Entry<T, Integer>> COMPARATOR = Map.Entry.comparingByValue();

    private final HashMap<T, Integer> counts;

    public Counter() {
        this.counts = new HashMap<>();
    }

    public static @NotNull Counter<Character> countCharacters(@NotNull String string) {
        Counter<Character> counter = new Counter<>();
        for (Character c : string.toCharArray()) {
            counter.increment(c);
        }
        return counter;
    }

    public void increment(T key) {
        increment(key, 1);
    }

    public void increment(T key, int step) {
        counts.merge(key, step, Integer::sum);
    }

    public void decrement(T key) {
        decrement(key, 1);
    }

    public void decrement(T key, int step) {
        counts.merge(key, -step, Integer::sum);
    }

    public int get(T key) {
        return counts.getOrDefault(key, 0);
    }

    public boolean contains(T key) {
        return counts.containsKey(key);
    }

    public void remove(T key) {
        counts.remove(key);
    }

    public void clear() {
        counts.clear();
    }

    public int size() {
        return counts.size();
    }

    public List<Map.Entry<T, Integer>> ascending() {
        var entries = new ArrayList<>(counts.entrySet());
        entries.sort(COMPARATOR);
        return entries;
    }

    public List<Map.Entry<T, Integer>> descending() {
        var entries = new ArrayList<>(counts.entrySet());
        entries.sort(COMPARATOR.reversed());
        return entries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counter<?> counter = (Counter<?>) o;
        return Objects.equals(counts, counter.counts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(counts);
    }

    @Override
    public String toString() {
        return "Counter{" +
                "counts=" + counts +
                '}';
    }

}
