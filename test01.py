from rapidfuzz import process, fuzz

docs = ["Alice Johnson", "Alicia Jonas", "Bob Robertson", "Dr. Alice J.", "Al Jon", "Allison Johnsen"]

q = "Alyce Jonson"
# Return highest scoring candidate with its similarity
# The scorer fuzz WRatio is a weighted composite that balances partial and
# token-aware comparisons, making it a good default for names.
best = process.extractOne(q, docs, scorer=fuzz.WRatio)

print(f"best_match={best[0]}, score={best[1]:.0f}")