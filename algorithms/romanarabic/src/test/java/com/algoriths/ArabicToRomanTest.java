package com.algoriths;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ArabicToRomanTest {

    @Test
    public void testArabicToRomanNumbers() {
        assertThat(toRoman(1)).isEqualTo("I");
        assertThat(toRoman(2)).isEqualTo("II");
        assertThat(toRoman(3)).isEqualTo("III");
        assertThat(toRoman(4)).isEqualTo("IV");
        assertThat(toRoman(5)).isEqualTo("V");
        assertThat(toRoman(6)).isEqualTo("VI");
        assertThat(toRoman(7)).isEqualTo("VII");
        assertThat(toRoman(8)).isEqualTo("VIII");
        assertThat(toRoman(9)).isEqualTo("IX");
        assertThat(toRoman(10)).isEqualTo("X");
        assertThat(toRoman(11)).isEqualTo("XI");
        assertThat(toRoman(25)).isEqualTo("XXV");
        assertThat(toRoman(34)).isEqualTo("XXXIV");
        assertThat(toRoman(39)).isEqualTo("XXXIX");
        assertThat(toRoman(40)).isEqualTo("XL");
        assertThat(toRoman(99)).isEqualTo("XCIX");
        assertThat(toRoman(999)).isEqualTo("CMXCIX");
        assertThat(toRoman(3999)).isEqualTo("MMMCMXCIX");
        assertThat(toRoman(1668)).isEqualTo("MDCLXVIII");
    }

    @Test
    public void testRomanToArabic() {
        Arrays.stream(new Object[][]{
                {"I", 1},
                {"II", 2},
                {"III", 3},
                {"IV", 4},
                {"V", 5},
                {"VI", 6},
                {"VII", 7},
                {"VIII", 8},
                {"IX", 9},
                {"X", 10},
                {"XI", 11},
                {"XII", 12},
                {"XIII", 13},
                {"XIV", 14},
                {"XV", 15},
                {"XVI", 16},
                {"XVII", 17},
                {"XVIII", 18},
                {"XIX", 19},
                {"XX", 20},
                {"XXXIX", 39},
                {"XL", 40},
                {"XLIX", 49},
                {"LXXXIX", 89},
                {"XC", 90},
                {"CD", 400},
                {"M", 1000},
                {"MDCLXVIII", 1668},
                {"MMMCMXCIX", 3999}
        })
                .forEach(n -> assertThat(toArabic((String) n[0])).isEqualTo((Integer) n[1]));
    }

    private static int toArabic(String roman) {
        return List.ofAll(roman.toCharArray())
                .map(ArabicToRomanTest::toNumber)
                .foldLeft(List.of(0), ArabicToRomanTest::reduceLastIflessThanActual)
                .sum()
                .intValue();
    }

    private static List<Integer> reduceLastIflessThanActual(List<Integer> l, Integer actual) {
        if (l.last() < actual)
            return l.init().append(actual - l.last());
        else
            return l.append(actual);
    }

    private static int toNumber(char c) {
        switch (c) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                throw new AssertionError("value " + c + " can not be converted");
        }
    }

    private static String toRoman(int n) {
        return toRoman(n, 0);
    }

    private static String toRoman(int n, int level) {
        int div = n / 10;
        int mod = n % 10;
        if (n < 10)
            return toRoman(mod, level, romanTable());
        else
            return toRoman(div, level + 1) + toRoman(mod, level, romanTable());
    }

    private static String toRoman(int n, int level, Map<Integer, Tuple2<String, String>> table) {
        if (n == 0)
            return "";
        if (n <= 3)
            return List.range(0, n)
                    .foldLeft("", (s, i) -> s + one(table.get(level)));
        else if (n == 4)
            return one(table.get(level)) + five(table.get(level));
        else if (n == 5)
            return five(table.get(level));
        else if (n < 9)
            return five(table.get(level)) + toRoman(n - 5, level, table);
        else if (n == 9)
            return one(table.get(level)) + one(table.get(level + 1));
        else
            throw new AssertionError("should not be " + n);
    }

    private static String one(Tuple2<String, String> tuple) {
        return tuple._1;
    }

    private static String five(Tuple2<String, String> tuple) {
        return tuple._2;
    }

    static private Map<Integer, Tuple2<String, String>> romanTable() {
        return HashMap.<Integer, Tuple2<String, String>>empty()
                .put(0, Tuple.of("I", "V"))
                .put(1, Tuple.of("X", "L"))
                .put(2, Tuple.of("C", "D"))
                .put(3, Tuple.of("M", "V^"))
                .toJavaMap();
    }

}
