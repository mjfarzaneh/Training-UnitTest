package com.training.unittest.exception;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.add(4,5);
        calculator.add(45,5);
        calculator.add("76","31");
    }
}
