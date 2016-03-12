# Concepts by phrase
SELECT trial, cmatch.sctid, phrase, fsn FROM concept, cmatch WHERE 
phrase = "metastatic kidney cancer" AND
cmatch.sctid = concept.sctid;