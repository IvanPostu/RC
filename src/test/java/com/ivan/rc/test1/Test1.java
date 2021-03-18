package com.ivan.rc.test1;

import java.util.Scanner;

interface Base {
}

public class Test1 {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String inStr = scanner.nextLine();
            int alphPffset = 1;
            int aASCIIOffset = (int) 'a';
            int AASCIIOffset = (int) 'A';

            inStr.replace(" ", "").chars().forEach(c -> {
                int alphOrder = Character.isUpperCase(c) ? c - AASCIIOffset : c - aASCIIOffset;
                alphOrder += alphPffset;
                String alphOrderStr = alphOrder > 9 ? String.format("%d", alphOrder) : String.format("%d ", alphOrder);
                System.out.print(alphOrderStr);
            });

            System.out.println();
        }
    }
}
