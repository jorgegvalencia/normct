package test;

import ctgov.*;
import ctgov.SearchOptions.RecruitmentStatus;
import ctgov.SearchOptions.SearchOptionsBuilder;

public class TestClass {

	public static void main(String[] args) {
		SearchOptionsBuilder builder= new SearchOptions.SearchOptionsBuilder("lung cancer");
		SearchOptions options = builder
				.setRecruitmentStatus(RecruitmentStatus.OPEN)
				.setDateRange("02/01/2016", "02/27/2016", false)
				.build();
		builder.showOptions();
		CTManager.downloadTrials(options);
		CTManager.downloadTrial("NCT02692924");
	}

}
