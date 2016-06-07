package model;

import java.util.ArrayList;
import java.util.List;

import nlp.Preprocessor;

public class CriteriaSet {
	private String criteriaTextBlock;
	private String nctid;
	private List<EligibilityCriteria> eclist;

	public CriteriaSet(String nctid, String criteria) {
		criteriaTextBlock = criteria;
		this.nctid = nctid;
		eclist = getEligibilityCriteriaFromText(nctid, criteria);
	}

	public String getCriteriaTextBlock() {
		return criteriaTextBlock;
	}
	
	public String getTrialId(){
		return nctid;
	}

	public List<EligibilityCriteria> getEligibilityCriteriaList() {
		return eclist;
	}

	private List<EligibilityCriteria> getEligibilityCriteriaFromText(String trial, String criteria) {
		// preprocess raw criteria and split the text into sentences
		List<String> utterancesList = Preprocessor.getSentencesFromText(criteria);
		
		// create the elegibility criteria units with the utterances from the sentences
		List<EligibilityCriteria> ecList = new ArrayList<EligibilityCriteria>();
		int type = 0; // inclusion, exclusion or n/a
		for (int uttNumber = 0; uttNumber < utterancesList.size(); uttNumber++) { // for each utterance
			String utterance = utterancesList.get(uttNumber);
			// criteria text is divided in two blocks, inclusion criteria and exclusion criteria
			if (utterance.toLowerCase().contains("inclusion criteria"))
				type = 1;
			else if (utterance.toLowerCase().contains("exclusion criteria"))
				type = 2;
			EligibilityCriteria ec = new EligibilityCriteria(trial, uttNumber, utterance, type);
			ecList.add(ec);
		}
		return ecList;
	}
}
