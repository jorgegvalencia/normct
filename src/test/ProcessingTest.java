package test;

import db.reports.Report;
import db.reports.ReportGenerator;

public class ProcessingTest {

	public static void main(String[] args) {
/*		String nctid = "NCT02692924";
		ProcessingUnit pu = new ProcessingUnit(nctid);
		ConceptExtractor ce = new ConceptExtractor("192.168.33.10");
		ce.process(pu, false);
		pu.printResults();*/
		Report rp = ReportGenerator.getCFReport();
		rp.printReport();
		rp.buildExcel();
	}

}
