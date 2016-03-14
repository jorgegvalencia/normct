package test;

import ctgov.*;
import nlp.ProcessingUnit;

public class CTDownloadTest {

	public static void main(String[] args) {
		String nctid = "NCT02692924";
		SearchOptions options = new SearchOptions.SearchOptionsBuilder("breast cancer").setDateRange("01/14/2016", "03/14/2016", false).build();
		CTManager.downloadTrials(options);
	}

}
