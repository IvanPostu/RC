package com.ivan.rc;

import java.time.temporal.TemporalAdjuster;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.ivan.rc.configuration.Log4jConfiguration;
import com.ivan.rc.lab4.client.MainWindow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class Main {

    static {
        Log4jConfiguration.configure();
    }

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        long bigInt = 1_000_000_000_000l;
        int z = (int) bigInt;

        System.out.println(z);
        // TemporalAdjuster tj = a -> return a;

        Comparator<Integer> c = (Integer a, Integer b) -> 1;

        // Comparator<String, String> tb = (String s1, String s2) -> s1.length();
        BiFunction<String, String, Integer> tb = (String s1, String s2) -> s1.length();
        ToLongBiFunction<String, String> tb1 = (String s1, String s2) -> s1.length();
    }

}

class Z {

    @Deprecated

    public void z() {

    }
}