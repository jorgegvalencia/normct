package test;

import java.util.regex.Pattern;

import db.reports.Report;
import db.reports.ReportGenerator;
import nlp.ConceptExtractor;
import nlp.ProcessingUnit;

public class ProcessingTest {

	public static void main(String[] args) {
		String cadena = "Mi cadena con números 2.5.2 y HER-2 hormone receptor";
		String refinedText;
		Pattern ASCII_PATTERN = Pattern.compile("[^\\p{ASCII}]+");
		refinedText = ASCII_PATTERN.matcher(cadena).replaceAll(" ");
		// Elimina los puntos de contenido numericos
		refinedText = refinedText.replaceAll("([0-9]+\\.(?=\\s))+\\s", " - ");
		// Elimina los guiones de puntos de contenido
		refinedText = refinedText.replaceAll("-\\s+(?=[A-Z])", "");
		// A las oraciones sin punto final y con salto de linea se le anade un
		// punto final
		refinedText = refinedText.replaceAll("(?<=.)\n\\s+(?=[A-Z]{1}[a-z])", ". ");
		refinedText = refinedText.replaceAll("\\.{2}", ". ");
		refinedText = refinedText.replaceAll("\\s+", " ");
		refinedText = refinedText.replaceAll("\\-", " ");
		refinedText = refinedText.replaceAll("\\\"", "");
		
		System.out.println(refinedText);
//		String nctid = "NCT02692924";
//		ProcessingUnit pu = new ProcessingUnit(nctid);
//		ConceptExtractor ce = new ConceptExtractor("192.168.33.10");
//		ce.process(pu, false);
//		pu.printResults();
	}

}
