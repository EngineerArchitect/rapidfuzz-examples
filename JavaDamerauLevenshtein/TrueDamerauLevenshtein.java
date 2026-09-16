package com.example.fuzzy;

import java.util.Arrays;

/**
 * Implements the true Damerau-Levenshtein distance algorithm (unrestricted variant)
 * with percentage-match scoring compatible with RapidFuzz-style semantics.
 *
 * Unlike the Optimal String Alignment (OSA) variant, this implementation allows
 * multiple edits on the same substring, producing correct distances for cases
 * such as "CA" -> "ABC" (distance 2, not 3).
 *
 * Time complexity:  O(n * m)
 * Space complexity: O(n * m)
 */
public final class TrueDamerauLevenshtein {

    private TrueDamerauLevenshtein() {
        // Utility class
    }

    // ---------------------------------------------------------------------
    // Distance
    // ---------------------------------------------------------------------

    /**
     * Computes the true Damerau-Levenshtein distance between two strings.
     */
    public static int distance(CharSequence a, CharSequence b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings must not be null");
        }

        int n = a.length();
        int m = b.length();

        // Fast paths
        if (n == 0) return m;
        if (m == 0) return n;
        if (a.equals(b)) return 0;

        int maxDist = n + m;
        int[][] d = new int[n + 2][m + 2];

        // Border of maxDist (virtual row/column at index 0)
        for (int i = 0; i <= n + 1; i++) {
            d[i][0] = maxDist;
        }
        for (int j = 0; j <= m + 1; j++) {
            d[0][j] = maxDist;
        }

        // Base rows/columns
        for (int i = 0; i <= n; i++) {
            d[i + 1][1] = i;
        }
        for (int j = 0; j <= m; j++) {
            d[1][j + 1] = j;
        }

        int[] lastRow = new int[Character.MAX_VALUE + 1];
        Arrays.fill(lastRow, 0);

        for (int i = 1; i <= n; i++) {
            int lastMatchCol = 0;
            char charA = a.charAt(i - 1);

            for (int j = 1; j <= m; j++) {
                char charB = b.charAt(j - 1);
                int i1 = lastRow[charB];
                int j1 = lastMatchCol;

                int cost = (charA == charB) ? 0 : 1;

                int substitution = d[i][j] + cost;
                int insertion    = d[i + 1][j] + 1;
                int deletion     = d[i][j + 1] + 1;
                int min = Math.min(substitution, Math.min(insertion, deletion));

                if (i1 > 0 && j1 > 0) {
                    int transposition = d[i1][j1]
                            + (i - i1 - 1)
                            + 1
                            + (j - j1 - 1);
                    min = Math.min(min, transposition);
                }

                d[i + 1][j + 1] = min;

                if (charA == charB) {
                    lastMatchCol = j;
                }
            }

            lastRow[charA] = i;
        }

        return d[n + 1][m + 1];
    }

    // ---------------------------------------------------------------------
    // Percentage match
    // ---------------------------------------------------------------------

    /**
     * Computes a normalized match percentage in the range [0.0, 100.0], where
     * 100.0 means the strings are identical and 0.0 means maximally different.
     *
     * The formula mirrors Python's RapidFuzz `fuzz.ratio`:
     *
     *     ratio = 100.0 * (1 - distance / (len(a) + len(b)))
     *
     * Because the true Damerau-Levenshtein distance is always ≤ max(len(a), len(b))
     * (substitutions alone suffice to transform one string into the other), the
     * maximum distance when using unit costs is `max(len(a), len(b))`, not the sum.
     * We therefore use the RapidFuzz convention of `len(a) + len(b)` as the
     * denominator, which guarantees a value in [0, 100] and matches RapidFuzz's
     * output exactly for unit-cost comparisons.
     *
     * @param a the source string (must not be null)
     * @param b the target string (must not be null)
     * @return match percentage between 0.0 and 100.0
     */
    public static double percentageMatch(CharSequence a, CharSequence b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings must not be null");
        }

        int lenA = a.length();
        int lenB = b.length();
        int totalLen = lenA + lenB;

        // Both empty: perfect match
        if (totalLen == 0) {
            return 100.0;
        }

        int dist = distance(a, b);
        double ratio = 1.0 - ((double) dist / totalLen);
        return ratio * 100.0;
    }

    /**
     * Convenience method: returns the integer percentage (0-100), rounded down.
     * Useful for threshold comparisons and reporting.
     */
    public static int percentageMatchInt(CharSequence a, CharSequence b) {
        return (int) Math.floor(percentageMatch(a, b));
    }

    // ---------------------------------------------------------------------
    // Threshold helpers
    // ---------------------------------------------------------------------

    /**
     * Returns true if the match percentage is at or above the given threshold (0-100).
     *
     * A cheap length-based pre-filter rejects obvious non-matches before
     * computing the full distance. If the maximum possible ratio given the
     * length difference is below the threshold, we short-circuit.
     */
    public static boolean meetsThreshold(CharSequence a, CharSequence b, double thresholdPercent) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings must not be null");
        }

        int lenA = a.length();
        int lenB = b.length();
        int totalLen = lenA + lenB;

        if (totalLen == 0) {
            return thresholdPercent <= 100.0;
        }

        // Best possible case: distance == |lenA - lenB| (only length-mismatch edits)
        int bestPossibleDist = Math.abs(lenA - lenB);
        double bestPossibleRatio = 1.0 - ((double) bestPossibleDist / totalLen);
        if (bestPossibleRatio * 100.0 < thresholdPercent) {
            return false; // Even a perfect match can't reach the threshold
        }

        return percentageMatch(a, b) >= thresholdPercent;
    }

    /**
     * Computes the distance only if the length difference permits it.
     * Returns -1 when the strings are too different in length to be within
     * the given edit-distance threshold.
     *
     * This is the fastest way to filter a large candidate set.
     */
    public static int distanceWithinThreshold(CharSequence a, CharSequence b, int threshold) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings must not be null");
        }
        if (Math.abs(a.length() - b.length()) > threshold) {
            return -1;
        }
        int dist = distance(a, b);
        return dist <= threshold ? dist : -1;
    }
}