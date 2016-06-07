package nlp.opennlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class NLPTagger {
	String modelRoute;

	public NLPTagger(String model){
		modelRoute = model;
	}
	
	public NLPTagger(){
		modelRoute = "resources/opennlp/en-pos-maxent.bin";
	}

	public List<String> posTag(List<String> tokenList){
		InputStream modelIn = null;
		List<String> tags = new ArrayList<String>();
		String[] tokensArray = new String[tokenList.size()];
		tokensArray = tokenList.toArray(tokensArray);
		try {
			modelIn = new FileInputStream(modelRoute);
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);
			for(String posTag : tagger.tag(tokensArray)){
				tags.add(posTag);
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
		return tags;

	}
}
