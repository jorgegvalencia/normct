package test;

import java.io.File;

import ctgov.CTManager;
import ctgov.SearchOptions;
import main.Environment;
import nlp.ConceptExtractor;
import nlp.ProcessingUnit;

public class BatchProcessingTest {
	
	public static void main (String[] args){
		SearchOptions options = new SearchOptions.SearchOptionsBuilder("breast cancer").build();
		CTManager.downloadTrials(options);
		ConceptExtractor ce = new ConceptExtractor(Environment.METAMAP_HOST);
		File[] files = new File(Environment.TRIALS_PATH).listFiles();
		for(File f: files){
			if(f.isFile()){
				ProcessingUnit pu = new ProcessingUnit(f.getName().replace(".xml", ""));
				ce.process(pu, true);
			}
		}
	}
}
