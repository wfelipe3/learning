package com.algoriths;

import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RomanArabicTest {

    @Test
    public void testOneFromArabicToRoman() {
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
    }

    private static String toRoman(int n) {
        List<Integer> digits = toDigits(n, List.empty());
        return digits.foldRight(Tuple.of(1, ""), (i, t) -> {
            Tuple3<String, String, String> units = getUnits(t._1);
            String roman = toRoman(i, units._1, units._2, units._3) + t._2;
            return Tuple.of(t._1 + 1, roman);
        })._2;
    }

    private static Tuple3<String, String, String> getUnits(int degree) {
        if (degree == 1)
            return Tuple.of("I", "V", "X");
        if (degree == 2)
            return Tuple.of("X", "L", "C");
        if (degree == 3)
            return Tuple.of("C", "D", "M");
        else
            throw new AssertionError("no degree found for " + degree);
    }

    private static List<Integer> toDigits(int n, List<Integer> values) {
        if (n < 10)
            return values.prepend(n);

        int div = n / 10;
        int mod = n % 10;

        if (div < 10)
            return values.prepend(mod).prepend(div);
        else
            return toDigits(div, values.append(mod));
    }

    private static String toRoman(int n, String one, String five, String ten) {
        if (n == 0)
            return "";
        if (n <= 3)
            return List.range(0, n)
                    .foldLeft("", (s, i) -> s + one);
        else if (n == 4)
            return toRoman(1, one, five, ten) + toRoman(5, one, five, ten);
        else if (n == 5)
            return five;
        else if (n < 9)
            return toRoman(5, one, five, ten) + toRoman(n - 5, one, five, ten);
        else if (n == 9)
            return toRoman(1, one, five, ten) + ten;
        else
            throw new AssertionError("should not be " + n);
    }

}
