package test;

import ctgov.*;
import nlp.ProcessingUnit;

public class CTDownloadTest {

	public static void main(String[] args) {
		String nctid = "NCT02692924";
		SearchOptions options = new SearchOptions.SearchOptionsBuilder("breast cancer").setDateRange("04/10/2015", "04/04/2016", false).build();
		CTManager.downloadTrials(options);
	}

}
