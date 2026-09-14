package utils;

import algorithms.KruskalSolver;

import java.util.Scanner;

public class InputParser {
    public String[] tokenize (String input){
        return input.split("\\s+");
    }
}
