SELECT concept.cui AS CUI, cmatch.sctid AS SCTID, COUNT(cmatch.number) AS FRECUENCY, concept.fsn AS CONCEPT
FROM cmatch, concept WHERE 
concept.sctid = cmatch.sctid 
GROUP BY sctid ORDER BY FRECUENCY DESC
LIMIT 0,100;