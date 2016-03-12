package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import ctgov.CTManager;
import main.Environment;

public class ClinicalTrial {

	private String nctid;
	private String title;
	private String topic;
	private String criteriaTextBlock;
	private Map<String, String> attributes;

	public ClinicalTrial(String nctid) {
		this.nctid = nctid;
		String filePath = "data/trials/" + nctid + ".xml";
		ArrayList<String> attr = new ArrayList<>(Environment.XML_ATTRIBUTES);

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
						this.title = text;
					}
					// set the trial topic
					else if (currentElement.equals("condition")){
						text = streamReader.getElementText();
						setTopic(text);
					}
					// set specified attributes
					else if (attr.contains(currentElement)){
						text = streamReader.getElementText();
						setAttribute(currentElement,text);
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
							//TODO this.criteria = new CriteriaSet(nctid, text);
							// Replace ASCII characters for their equivalents
							String criteria = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\x00-\\x7F]", "");
							this.criteriaTextBlock = criteria;
						}
						// sequence of elements brief_summary > textblock
					} else if (currentElement.equals("brief_summary")) { 
						streamReader.next(); // step to element textblock
						if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
							currentElement = streamReader.getLocalName();
							text = streamReader.getElementText();
							setAttribute("brief_summary", text);
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
	}
	
	public ClinicalTrial(String nctid, String title, String topic){
		this.nctid = nctid;
		this.title = title;
		this.topic = topic;
	}

	public String getNctid() {
		return nctid;
	}

	public String getTitle() {
		return title;
	}

	public String getTopic() {
		return topic;
	}

	public String getCriteriaTextBlock() {
		return criteriaTextBlock;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public void print(){
		System.out.println("Trial: "+ nctid);
		System.out.println("Title: "+ title);
		System.out.println("Topic: "+ topic);
		for(Entry<String,String> e: attributes.entrySet()){
			System.out.println(toTitleCase(e.getKey().replace("_", " ")) + ": " + e.getValue());
		}
	}
	
	private static String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder();
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}

	private void setTopic(String topic) {
		if (this.topic == null)
			this.topic = topic;
		else
			this.topic = this.topic + ", " + topic;
	}

	private void setAttribute(String attr, String value) {
		if (attributes == null)
			attributes = new HashMap<String, String>();
		attributes.put(attr, value);
	}

	private static boolean checkLocalFile(String nctid) {
		String filePath = "data/trials/" + nctid + ".xml";
		File f = new File(filePath);
		return f.exists();
	}
}
