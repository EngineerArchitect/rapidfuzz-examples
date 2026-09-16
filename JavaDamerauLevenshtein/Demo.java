package com.example.fuzzy;

public class Demo {
    public static void main(String[] args) {
        // Classic true-DL case
        System.out.println(TrueDamerauLevenshtein.distance("CA", "ABC"));           // 2

        // Percentage match examples
        System.out.println(TrueDamerauLevenshtein.percentageMatch("hello", "hello"));    // 100.0
        System.out.println(TrueDamerauLevenshtein.percentageMatch("hello", "hallo"));    // 80.0
        System.out.println(TrueDamerauLevenshtein.percentageMatch("teh", "the"));        // 66.67
        System.out.println(TrueDamerauLevenshtein.percentageMatch("kitten", "sitting")); // 57.14
        System.out.println(TrueDamerauLevenshtein.percentageMatch("abc", "xyz"));        // 0.0

        // Threshold filtering
        System.out.println(TrueDamerauLevenshtein.meetsThreshold("hello", "hallo", 75.0)); // true
        System.out.println(TrueDamerauLevenshtein.meetsThreshold("hello", "world", 75.0)); // false

        // Fast candidate filtering by edit distance
        System.out.println(TrueDamerauLevenshtein.distanceWithinThreshold("hello", "hallo", 2)); // 1
        System.out.println(TrueDamerauLevenshtein.distanceWithinThreshold("hello", "world", 2)); // -1
    }
}