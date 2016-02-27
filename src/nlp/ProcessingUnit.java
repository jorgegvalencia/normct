package nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import ctgov.CTManager;
import model.ClinicalTrial;
import model.ClinicalTrial.ClinicalTrialBuilder;

public class ProcessingUnit {

	private static ArrayList<String> attributes = new ArrayList<>(Arrays.asList("study_type","gender","url","overall_status","lastchanged_date","start_date"));
	private ClinicalTrialBuilder builder;
	private ClinicalTrial clinicalTrial;
	private double time;

	private ProcessingUnit(String nctid) {
		builder = new ClinicalTrialBuilder(nctid);
		time = 0.0;
	}

	public ClinicalTrialBuilder getCTBuilder() {
		return builder;
	}

	public ClinicalTrial getClinicalTrial() {
		return clinicalTrial;
	}

	public double getTime() {
		return time;
	}

	public void setTimeAndBuild(double time) {
		this.time = time;
		clinicalTrial = builder.build();
	}

	public boolean isProcessed() {
		return clinicalTrial != null;
	}

	private static boolean checkLocalFile(String nctid) {
		String filePath = "data/trials/" + nctid + ".xml";
		File f = new File(filePath);
		return f.exists();
	}

	public static ProcessingUnit buildProcessingUnit(String nctid) {
		ProcessingUnit pu = new ProcessingUnit(nctid);
		String filePath = "data/trials/" + nctid + ".xml";
		ArrayList<String> attr = new ArrayList<>(attributes);

		if (!checkLocalFile(nctid)) { // if the file is not already downloaded
			// download the trial
			CTManager.downloadTrial(nctid);
		}
		try {
			File file = new File(filePath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			// StAX components
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = factory.createXMLStreamReader(br);

			// iterate over each element of the XML
			String currentElement = null;
			while (streamReader.hasNext()) {
				String text = "";
				int event = streamReader.next();
				switch (event) {
				
				case XMLStreamConstants.START_ELEMENT: // element event
					currentElement = streamReader.getLocalName();
					// set title
					if (currentElement.equals("brief_title")){
						text = streamReader.getElementText();
						pu.getCTBuilder().setTitle(text);
						System.out.println("Attribute: "+ currentElement + "\t Value: "+ text);
					}
					// set the trial topic
					else if (currentElement.equals("condition")){
						text = streamReader.getElementText();
						pu.getCTBuilder().setTopic(text);
						System.out.println("Attribute: "+ currentElement + "\t Value: "+ text);
					}
					// set specified attributes
					else if (attributes.contains(currentElement)){
						text = streamReader.getElementText();
						pu.getCTBuilder().setAttribute(currentElement,text);
						System.out.println("Attribute: "+ currentElement + "\t Value: "+ text);
						attr.remove(currentElement);
					}
					break;
					
				case XMLStreamConstants.CHARACTERS:
					// sequence of elements criteria > textblock
					if (currentElement.equals("criteria")) {
						streamReader.next(); // step to element textblock
						if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
							currentElement = streamReader.getLocalName();
							text = streamReader.getElementText();
							pu.getCTBuilder().setCriteria((text));
							System.out.println("Attribute: "+ currentElement + "\t Value: "+ text);
						}
					// sequence of elements brief_summary > textblock
					} else if (currentElement.equals("brief_summary")) { 
						streamReader.next(); // step to element textblock
						if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
							currentElement = streamReader.getLocalName();
							text = streamReader.getElementText();
							pu.getCTBuilder().setAttribute("brief_summary", text);
							System.out.println("Attribute: "+ currentElement + "\t Value: "+ text);
						}
					} 
					break;
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pu;
	}

}
