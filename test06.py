from rapidfuzz import fuzz

docs = ["Alice Johnson", "Alicia Jonas", "Bob Robertson", "Dr. Alice J.", "Al Jon", "Allison Johnsen"]

def search(q, k=3):
    t = 80 if len(q) > 6 else 70
    scored = []
    
    # 1. Convert query to lowercase
    q_lower = q.lower() 
    
    for d in docs:
        # 2. Convert document to lowercase
        d_lower = d.lower()
        
        # 3. Use the lowercased strings for matching
        s = max(fuzz.WRatio(q_lower, d_lower), fuzz.token_set_ratio(q_lower, d_lower))
        
        if len(q_lower) <= 6:
            s = max(s, fuzz.partial_ratio(q_lower, d_lower))
            
        if s >= t:
            # Keep original document 'd' for the final output, but store the score 's'
            scored.append((d, s))
            
    scored.sort(key=lambda x: x[1], reverse=True)
    return scored[:k]

hits = search("al jon", k=3)
print(f"final_top3={[(d, int(s)) for d, s in hits]}")