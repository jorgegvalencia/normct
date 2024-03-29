package nlp.opennlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class NLPSentenceDetector {
	String modelRoute;

	public NLPSentenceDetector(String model) {
		modelRoute = model;
	}
	
	public NLPSentenceDetector() {
		modelRoute = "resources/opennlp/en-sent.bin";
	}

	public List<String> detectSentences(String text) {
		InputStream modelIn = null;
		List<String> sentences = new ArrayList<String>();
		try {
			modelIn = new FileInputStream(modelRoute);
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
			for (String sentence : sentenceDetector.sentDetect(text)) {
				sentences.add(sentence);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
		return sentences;
	}
}
