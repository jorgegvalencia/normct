package model;

import java.util.ArrayList;
import java.util.List;

import nlp.Preprocessor;

public class CriteriaSet {
	private String criteriaTextBlock;
	private List<EligibilityCriteria> eclist;

	public CriteriaSet(String nctid, String criteria) {
		criteriaTextBlock = criteria;
		eclist = getEligibilityCriteriaFromText(nctid, criteria);
	}

	public String getCriteriaTextBlock() {
		return criteriaTextBlock;
	}

	public List<EligibilityCriteria> getECList() {
		return eclist;
	}

	private List<EligibilityCriteria> getEligibilityCriteriaFromText(String trial, String criteria) {
		// preprocess raw criteria and split text into sentences
		String processedCriteria = Preprocessor.processRawCriteriaText(criteria);
		List<String> uttList = Preprocessor.getSentencesFromText(processedCriteria);
		
		// create the elegibility criteria units with the utterances from the sentences
		List<EligibilityCriteria> ecList = new ArrayList<EligibilityCriteria>();
		int type = 0;
		for (int i = 0; i < uttList.size(); i++) { // for each utterance
			String utt = uttList.get(i);
			if (utt.toLowerCase().contains("inclusion criteria"))
				type = 1;
			else if (utt.toLowerCase().contains("exclusion criteria"))
				type = 2;
			EligibilityCriteria ec = new EligibilityCriteria(trial, i, utt, type);
			ecList.add(ec);
		}
		return ecList;
	}
}
