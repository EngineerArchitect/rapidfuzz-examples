from rapidfuzz import process, fuzz

docs = ["Alice Johnson", "Alicia Jonas", "Bob Robertson", "Dr. Alice J.", "Al Jon", "Allison Johnsen"]

q3 = "Johnson, Alice"

# The call process.extractOne with scorer=fuzz.token_set_ratio ignores token
# order and duplicate terms, so variations like "Johnson, Alice"

# If queries are very short, token_set_ratio may overgeneralize; pairing it with 
# partial matching can help.
best_ts = process.extractOne(q3, docs, scorer=fuzz.token_set_ratio)

print(f"order_robust_match={best_ts[0]}, score={best_ts[1] :.0f}")