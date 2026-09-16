from rapidfuzz import process, fuzz

docs = ["Alice Johnson", "Alicia Jonas", "Bob Robertson", "Dr. Alice J.", "Al Jon", "Allison Johnsen"]

q2 = "A. Jon"
# The call process.extract with scorer=fuzz.partial_ratio focuses on substring
# similarity so clipped forms like initials still match the right names.

# The argument limit=2 asks RapidFuzz to return only the top two candidates,
# balancing visibility with brevity.
top = process.extract(q2, docs, scorer=fuzz.partial_ratio, limit=3)

pairs = [(c, int(s)) for c, s,_ in top]

print(f"top_partial_matches={pairs}")