package test;

import java.io.File;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ctgov.CTManager;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import nlp.ProcessingUnit;

public class SemanticTypesTest {
	
	// Prueba con 5 ensayos de frecuencia de tipos semanticos del UMLS

	public static void main (String args[]){

		MetaMapApiImpl mmapi = new MetaMapApiImpl();
		mmapi.setHost("192.168.33.10");
		mmapi.setOptions("-R SNOMEDCT_US -y");
		String[] trials = {"NCT02701023","NCT02701010","NCT02700984","NCT02700971","NCT02700945"};
		CTManager.downloadTrials(trials);
		HashMap<String,Integer> mpp = new HashMap<>();
		for(String nctid: trials){
			ProcessingUnit pu = ProcessingUnit.buildProcessingUnit(nctid);
			pu.setTimeAndBuild(0);
			String criteria = pu.getClinicalTrial().getCriteria().getCriteriaTextBlock();
			criteria = Normalizer.normalize(criteria, Normalizer.Form.NFD);
			String resultString = criteria.replaceAll("[^\\x00-\\x7F]", "");
			List<Result> resultList =  mmapi.processCitationsFromString(resultString);
			print(resultList, mpp);
		}
		for(Entry<String, Integer> e: mpp.entrySet()){
			System.out.println(e.getKey() + "|" + e.getValue());
		}

	}

	public static void print(List<Result> resultList, HashMap<String,Integer> mpp){
		try{
			for (Result result: resultList) {
				for (Utterance utterance: result.getUtteranceList()) {
					for (PCM pcm: utterance.getPCMList()) {
						for (Mapping map: pcm.getMappingList()) {
							for (Ev mapEv: map.getEvList()) {
								String semt = mapEv.getSemanticTypes().toString();
								if(!mpp.containsKey(semt)){
									mpp.put(semt,1);
								}
								else{
									int curr = mpp.get(semt);
									mpp.put(semt,curr+1);
								}
							}
						}
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
