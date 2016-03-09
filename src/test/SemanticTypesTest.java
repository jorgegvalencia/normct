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
import ctgov.SearchOptions;
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
		SearchOptions options = new SearchOptions.SearchOptionsBuilder("breast cancer").build();
		//CTManager.downloadTrials(options);
		HashMap<String,Integer> mpp = new HashMap<>();
		HashMap<String,Double> score = new HashMap<>();
		File[] files = new File("data/trials/").listFiles();
		for(File f: files){
			if(f.isFile()){
				ProcessingUnit pu = new ProcessingUnit(f.getName().replace(".xml", ""));
				pu.setTime(0);
				String criteria = pu.getCriteriaSet().getCriteriaTextBlock();
				criteria = Normalizer.normalize(criteria, Normalizer.Form.NFD);
				String resultString = criteria.replaceAll("[^\\x00-\\x7F]", "");
				List<Result> resultList =  mmapi.processCitationsFromString(resultString);
				print(resultList, mpp, score);
			}
		}
		for(Entry<String, Integer> e: mpp.entrySet()){
			String key = e.getKey();
			Integer value = e.getValue();
			//System.out.println(key + "|" + score.get(key));
			System.out.println(key + "|" + value + "|" + Math.round(score.get(key)/value));
		}

	}

	public static void print(List<Result> resultList, HashMap<String,Integer> mpp, HashMap<String,Double> scores){
		try{
			for (Result result: resultList) {
				for (Utterance utterance: result.getUtteranceList()) {
					for (PCM pcm: utterance.getPCMList()) {
						for (Mapping map: pcm.getMappingList()) {
							for (Ev mapEv: map.getEvList()) {
								String semt = mapEv.getSemanticTypes().toString();
								double score = mapEv.getScore();
								if(!mpp.containsKey(semt)){
									mpp.put(semt,1);
									scores.put(semt, score*-1);
								}
								else{
									int curr = mpp.get(semt);
									double currScore = scores.get(semt);
									mpp.put(semt,curr+1);
									scores.put(semt,currScore+score*-1);
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
