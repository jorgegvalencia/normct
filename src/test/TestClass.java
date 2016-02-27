package test;

import ctgov.*;
import ctgov.SearchOptions.SearchOptionsBuilder;

public class TestClass {

	public static void main(String[] args) {
		SearchOptionsBuilder builder= new SearchOptions.SearchOptionsBuilder();
		SearchOptions options = builder.setSearchTerms("breast cancer").build();
		CTManager.downloadTrials(options);
	}

}
