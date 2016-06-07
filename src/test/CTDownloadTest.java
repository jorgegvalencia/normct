package test;

import ctgov.*;
import nlp.ProcessingUnit;

public class CTDownloadTest {

	public static void main(String[] args) {
		String nctid = "NCT02692924";
//		SearchOptions options = new SearchOptions.SearchOptionsBuilder("breast cancer").setDateRange("04/24/2015", "04/24/2016", false).build();
		SearchOptions options = new SearchOptions.SearchOptionsBuilder("alzheimer").setDateRange("05/15/2015", "05/15/2016", false).build();
		CTManager.downloadTrials(options);
	}

}
