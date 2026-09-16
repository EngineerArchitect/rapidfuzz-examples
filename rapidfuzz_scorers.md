# RapidFuzz Scorers Guide

RapidFuzz provides a rich set of scorers, primarily located under the `rapidfuzz.fuzz` module, used to calculate the similarity between strings.

## 📋 List of Core Scorers

| Scorer Name | Core Logic |
| :--- | :--- |
| `fuzz.ratio` | Basic normalized Indel (insertion/deletion) similarity; compares the strings as a whole. |
| `fuzz.partial_ratio` | Searches for the best alignment of the shorter string within the longer string and returns the `fuzz.ratio` score for that alignment. |
| `fuzz.token_sort_ratio` | Splits strings into words, sorts them, and then calculates `fuzz.ratio`; insensitive to word order. |
| `fuzz.token_set_ratio` | Similarity based on sets of words; handles duplicate words and containment relationships (subsets) very well. |
| `fuzz.partial_token_sort_ratio` | Similar to `token_sort_ratio`, but uses `fuzz.partial_ratio` for comparison. |
| `fuzz.partial_token_set_ratio` | Similar to `token_set_ratio`, but uses `fuzz.partial_ratio` for comparison. |
| `fuzz.WRatio` | **Weighted Ratio**; combines multiple strategies into a weighted result. It is the default scorer for the `process.extract` series. |
| `fuzz.QRatio` | Quick Ratio; a faster version of `fuzz.ratio`, with the main difference being how it handles empty strings. |
| `fuzz.token_ratio` | Returns the maximum value from `token_set_ratio` and `token_sort_ratio`; a combined strategy. |

## 💡 Special Notes

*   **Weighting Logic**: When calculating, `WRatio` applies a weight of 0.9 to partial match results (like `partial_ratio`). This means that even if a substring matches perfectly, the final score might be 90 instead of 100.
*   **Version Differences**: The `UWRatio` and `UQRatio` found in older versions of `fuzzywuzzy` no longer exist in RapidFuzz, because handling Unicode through the `processor` parameter is now more flexible.

## 🛠️ Distance Measurement Module

In addition to the `fuzz` module, RapidFuzz also provides the `rapidfuzz.distance` module, which contains lower-level edit distance algorithms such as `Levenshtein`, `Hamming`, etc. These typically return distance values rather than a similarity score from 0-100.

When choosing a scorer, you can select the appropriate algorithm based on your specific matching needs (e.g., whether you need to ignore word order, whether you can tolerate partial matches, etc.).