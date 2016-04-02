package test;

import db.reports.Report;
import db.reports.ReportGenerator;

public class ReportTest {

	public static void main(String[] args) {
		Report rp = ReportGenerator.getCFReport();
		rp.printReport();
		rp.buildExcel();
	}

}
