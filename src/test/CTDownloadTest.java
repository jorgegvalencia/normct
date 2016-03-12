package test;

import ctgov.*;
import nlp.ProcessingUnit;

public class CTDownloadTest {

	public static void main(String[] args) {
		String nctid = "NCT02692924";
		CTManager.downloadTrial(nctid);
		ProcessingUnit pu = new ProcessingUnit(nctid);
	}

}
