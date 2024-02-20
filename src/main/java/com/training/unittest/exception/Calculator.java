package com.training.unittest.exception;

public class Calculator {

    public int add(int a, int b) {
        System.out.println(a + b);
        return a + b;
    }

    public double add(double a, double b) {
        System.out.println(a + b);
        return a + b;
    }

    public String add(String a, String b) {
        String result = String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));
        System.out.println(result);
        return result;
    }
}
