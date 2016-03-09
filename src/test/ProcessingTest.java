package test;

import nlp.ConceptExtractor;
import nlp.ProcessingUnit;

public class ProcessingTest {

	public static void main(String[] args) {
		String nctid = "NCT02692924";
		ProcessingUnit pu = new ProcessingUnit(nctid);
		ConceptExtractor ce = new ConceptExtractor("192.168.33.10");
		ce.process(pu, false);
		pu.printResults();
	}

}
