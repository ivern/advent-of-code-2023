package util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public class MathUtil {

    private MathUtil() {
    }

    public static int gcd(int a, int b) {
        return (int) gcd(a, (long) b);
    }

    public static long gcd(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        long remainder;

        do {
            remainder = a % b;
            a = b;
            b = remainder;
        } while (remainder > 0);

        return a;
    }

    public static int lcm(int a, int b) {
        long result = lcm(a, (long) b);
        if (result > Integer.MAX_VALUE) {
            throw new RuntimeException("lcm(" + a + ", " + b + ") does not fit in an int, use longs instead");
        }
        return (int) result;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }

        return abs(a * b) / gcd(a, b);
    }

    public static int lcm(int[] ns) {
        long[] longNs = new long[ns.length];
        for (int i = 0; i < ns.length; ++i) {
            longNs[i] = ns[i];
        }

        long result = lcm(longNs);
        if (result > Integer.MAX_VALUE) {
            throw new RuntimeException("lcm(" + Arrays.toString(ns) + ") does not fit in an int, use longs instead");
        }

        return (int) result;
    }

    public static long lcm(long[] ns) {
        long result = ns[0];
        for (int i = 1; i < ns.length; ++i) {
            result = lcm(ns[i], result);
        }
        return result;
    }

    public static @NotNull Map<Integer, Integer> factors(int n) {
        var result = new HashMap<Integer, Integer>();
        var max = ceil(sqrt(n));

        for (int i = 2; i <= max; ++i) {
            int power = 0;

            while (n % i == 0) {
                ++power;
                n /= i;
            }

            if (power > 0) {
                result.put(i, power);
            }
        }

        return result;
    }

}
