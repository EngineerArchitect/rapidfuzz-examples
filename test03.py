from rapidfuzz import process, fuzz

docs = ["Alice Johnson", "Alicia Jonas", "Bob Robertson", "Dr. Alice J.", "Al Jon", "Allison Johnsen"]
q = "Alyce Jonson"

filtered = process.extract(q, docs, scorer=fuzz.WRatio, score_cutoff=85, limit=None)

low = min(s for _, s, _ in filtered) if filtered else 0

print(f"kept_count={len(filtered)}, lowest_kept_score={low:.0f}")