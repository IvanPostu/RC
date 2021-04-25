package com.ivan.rc.mcpi_lab;

import java.io.IOException;
import java.util.Scanner;

public abstract class Main {

    private static final int calculateExtendedEuclid(int a, int b, int[] mutableVal) {
        if (a == 0) {
            return b;
        }

        int q = b / a;

        int temp = mutableVal[1];
        mutableVal[1] = -1 * mutableVal[1] * q + mutableVal[0];
        mutableVal[0] = temp;

        int newA = b % a;

        String logMessage = String.format("y = [%d, %d], r = [%d, %d], q = %d", mutableVal[0], mutableVal[1], b, a, q);
        System.out.println(logMessage);

        int result = calculateExtendedEuclid(newA, a, mutableVal);

        return result;
    }

    static int power(int x, int y, int p) {
        int res = 1;

        x = x % p; // modifica x daca este egal sau mai mare ca p

        if (x == 0)
            return 0; // daca x este divisibil cu p

        while (y > 0) {

            // daca y este impar, * cu result
            if ((y & 1) != 0)
                res = (res * x) % p;

            y = y >> 1; // y = y/2
            x = (x * x) % p;
        }
        return res;
    }

    /**
     * Cel mai mare divizor comun ex: 35, 15 -> 5 Principiul de functionare:
     */
    private static final void calculateEuclid() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("A = ");
        int a = scanner.nextInt();
        System.out.print("B = ");

        int b = scanner.nextInt();

        int[] result = { 0, 1 };

        calculateExtendedEuclid(a, b, result);
        System.out.println(String.format("euclid(%d, %d) = %d", a, b, result[0] + result[1]));
    }

    /**
     * Exemplu din referat 65 pow 223 mod 713 === 369
     */
    private static final void calculateFastPowModulo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("X = ");
        int x = scanner.nextInt();
        System.out.print("Y = ".intern());
        int y = scanner.nextInt();
        System.out.print("P = ");
        int p = scanner.nextInt();

        System.out.println("Power is " + power(x, y, p));
    }

    public static void main(String[] args) {
        // Main main = new Main();
        calculateEuclid();
        String[] sA = new String[1] { "aaa"};
    }
}
