package test;

import ctgov.*;
import ctgov.SearchOptions.SearchOptionsBuilder;

public class TestClass {

	public static void main(String[] args) {
		SearchOptionsBuilder builder= new SearchOptions.SearchOptionsBuilder("smoker");
		SearchOptions options = builder
				.build();
		CTManager.downloadTrials(options);
		CTManager.downloadTrial("NCT02692924");
	}

}
