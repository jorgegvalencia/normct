package nlp.opennlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class NLPTokenizer {
	String modelRoute;

	public NLPTokenizer(String model){
		modelRoute = model;
	}
	
	public NLPTokenizer(){
		modelRoute = "resources/opennlp/en-token.bin";
	}
	
	public List<String> tokenize(String sentence){
		InputStream modelIn = null;
		List<String> tokensList = new ArrayList<String>();
		try {
			modelIn = new FileInputStream(modelRoute);
			TokenizerModel model = new TokenizerModel(modelIn);
			Tokenizer tokenizer = new TokenizerME(model);
			for(String token: tokenizer.tokenize(sentence)){
				tokensList.add(token);
			}
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}
		return tokensList;
	}
}
