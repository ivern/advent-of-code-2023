package util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Encoder {

    private final Map<Character, Integer> encodingDictionary;

    public Encoder(@NotNull String symbols) {
        this.encodingDictionary = new HashMap<>();

        for (int i = 0; i < symbols.length(); ++i) {
            this.encodingDictionary.put(symbols.charAt(i), i);
        }
    }

    public long encode(@NotNull String value) {
        int radix = radix();
        long encoded = 0;

        for (int i = 0; i < value.length(); ++i) {
            Character c = value.charAt(i);
            if (!encodingDictionary.containsKey(c)) {
                throw new IllegalArgumentException(
                        "Encoder dictionary has no entry for symbol='" + c + "' in string=\"" + value + "\"");
            }
            encoded = encoded * radix + encodingDictionary.get(c);
        }

        return encoded;
    }

    public int radix() {
        return encodingDictionary.size();
    }

}
