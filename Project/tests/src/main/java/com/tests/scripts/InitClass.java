package com.tests.scripts;

public class InitClass {

    public static void main(String[] args) {
        System.out.println("Success.");
        System.out.println();
        for (int i = 0; i < 50; i++) {
            System.out.print(randomNumber(10) + ", ");
        }
        System.out.println(randomNumber(10));
    }


    private static int randomNumber(long maxNumber) {
        return (int) ((Math.random() * maxNumber) * (Math.random() < 0.75 ? 1 : -1));
    }
}