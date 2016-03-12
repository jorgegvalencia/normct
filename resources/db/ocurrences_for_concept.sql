# Retrieve all the eligibility criteria with its associated concepts
SELECT DISTINCT eligibility_criteria.trial AS TRIAL, eligibility_criteria.number AS EC, utterance AS UTT, phrase AS PHRASE, concept.fsn AS TERM, concept.sctid AS SCTID
FROM cmatch, eligibility_criteria, concept WHERE
cmatch.number = eligibility_criteria.number AND cmatch.trial = eligibility_criteria.trial AND
cmatch.sctid = concept.sctid 
AND concept.sctid = "414916001" AND NOT eligibility_criteria.inc_exc = 0
# LIMIT 0,20;