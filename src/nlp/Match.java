package nlp;

import java.util.List;

import model.Concept;

public class Match {
	private Concept concept;
	private String phrase;
	private String term;
	private String prefered;
	private List<String> matchedWords;
	private List<String> semanticTypes;

	public Match(Concept c, String phrase, String t, String pref, List<String> mw, List<String> semt) {
		concept = c;
		this.phrase = phrase;
		term = t;
		prefered = pref;
		matchedWords = mw;
		semanticTypes = semt;
	}

	public Concept getConcept() {
		return concept;
	}

	public String getPhrase() {
		return phrase;
	}

	public String getTerm() {
		return term;
	}

	public String getPrefered() {
		return prefered;
	}

	public List<String> getMatchedWords() {
		return matchedWords;
	}
	
	public List<String> getSemanticTypes() {
		return semanticTypes;
	}
	
	public void print(){
		System.out.println("Concept: " + concept.getCui());
		System.out.println("Term: " + term);
		System.out.println("Phrase: " + phrase);
	}

}
