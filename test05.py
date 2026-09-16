from rapidfuzz import process, fuzz

docs = ["Alice Johnson", "Alicia Jonas", "Bob Robertson", "Dr. Alice J.", "Al Jon", "Allison Johnsen"]

def search(q, k=3):
    t = 80 if len(q) > 6 else 70
    scored = []
    for d in docs:
        fw = fuzz.WRatio(q, d)
        tsr = fuzz.token_set_ratio(q, d)
        s = max(fw, tsr)
        if len(q) <= 6:
            s = max(s, fuzz.partial_ratio(q, d))
        if s >= t:
            scored.append((d, s))
    scored.sort(key=lambda x: x[1], reverse=True)
    return scored[:k]

hits = search("al jon", k=3)

print(f"final_top3={[(d, int(s)) for d, s in hits]}")