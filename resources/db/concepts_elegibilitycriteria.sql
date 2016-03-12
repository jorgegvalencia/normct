# Concepts by phrase
SELECT DISTINCT cmatch.trial, cmatch.number, cmatch.sctid, fsn FROM concept, cmatch, eligibility_criteria WHERE 
#phrase = "metastatic kidney cancer" AND
cmatch.sctid = concept.sctid AND
cmatch.number = 3  AND 
cmatch.trial = eligibility_criteria.trial
#AND cmatch.sctid = concept.sctid;