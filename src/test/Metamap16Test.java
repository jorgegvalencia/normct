package test;

import java.util.List;

import gov.nih.nlm.nls.metamap.AcronymsAbbrevs;
import gov.nih.nlm.nls.metamap.ConceptPair;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Negation;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Position;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class Metamap16Test {
	public static void main (String[] args){
		MetaMapApiImpl mmapi = new MetaMapApiImpl();
		mmapi.setHost("192.168.33.10");
		mmapi.setOptions("-R SNOMEDCT_US -y --UDA UDAfile --nomap Exclusions");
		List<Result> res = mmapi.processCitationsFromString("patients");
		try{
			for (Result result: res) {
				if (result != null) {
					System.out.println("input text: ");
					System.out.println(" " + result.getInputText());
					List<AcronymsAbbrevs> aaList = result.getAcronymsAbbrevsList();
					if (aaList.size() > 0) {
						System.out.println("Acronyms and Abbreviations:");
						for (AcronymsAbbrevs e: aaList) {
							System.out.println("Acronym: " + e.getAcronym());
							System.out.println("Expansion: " + e.getExpansion());
							System.out.println("Count list: " + e.getCountList());
							System.out.println("CUI list: " + e.getCUIList());
						}
					}
					List<Negation> negList = result.getNegationList();
					if (negList.size() > 0) {
						System.out.println("Negations:");
						for (Negation e: negList) {
							System.out.println("type: " + e.getType());
							System.out.print("Trigger: " + e.getTrigger() + ": [");
							for (Position pos: e.getTriggerPositionList()) {
								System.out.print(pos  + ",");
							}
							System.out.println("]");
							System.out.print("ConceptPairs: [");
							for (ConceptPair pair: e.getConceptPairList()) {
								System.out.print(pair + ",");
							}
							System.out.println("]");
							System.out.print("ConceptPositionList: [");
							for (Position pos: e.getConceptPositionList()) {
								System.out.print(pos + ",");
							}
							System.out.println("]");
						}
					}
					for (Utterance utterance: result.getUtteranceList()) {
						System.out.println("Utterance:");
						System.out.println(" Id: " + utterance.getId());
						System.out.println(" Utterance text: " + utterance.getString());
						System.out.println(" Position: " + utterance.getPosition());

						for (PCM pcm: utterance.getPCMList()) {
							System.out.println("Phrase:");
							System.out.println(" text: " + pcm.getPhrase().getPhraseText());
							System.out.println(" Minimal Commitment Parse: " + pcm.getPhrase().getMincoManAsString());
							System.out.println("Candidates:");

							for (Ev ev: pcm.getCandidatesInstance().getEvList()) {
								System.out.println(" Candidate:");
								System.out.println("  Score: " + ev.getScore());
								System.out.println("  Concept Id: " + ev.getConceptId());
								System.out.println("  Concept Name: " + ev.getConceptName());
								System.out.println("  Preferred Name: " + ev.getPreferredName());
								System.out.println("  Matched Words: " + ev.getMatchedWords());
								System.out.println("  Semantic Types: " + ev.getSemanticTypes());
								System.out.println("  MatchMap: " + ev.getMatchMap());
								System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
								System.out.println("  is Head?: " + ev.isHead());
								System.out.println("  is Overmatch?: " + ev.isOvermatch());
								System.out.println("  Sources: " + ev.getSources());
								System.out.println("  Positional Info: " + ev.getPositionalInfo());
								System.out.println("  Pruning Status: " + ev.getPruningStatus());
								System.out.println("  Negation Status: " + ev.getNegationStatus());
							}

							System.out.println("Mappings:");
							for (Mapping map: pcm.getMappingList()) {
								System.out.println(" Map Score: " + map.getScore());
								for (Ev mapEv: map.getEvList()) {
									System.out.println("   Score: " + mapEv.getScore());
									System.out.println("   Concept Id: " + mapEv.getConceptId());
									System.out.println("   Concept Name: " + mapEv.getConceptName());
									System.out.println("   Preferred Name: " + mapEv.getPreferredName());
									System.out.println("   Matched Words: " + mapEv.getMatchedWords());
									System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());
									System.out.println("   MatchMap: " + mapEv.getMatchMap());
									System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
									System.out.println("   is Head?: " + mapEv.isHead());
									System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
									System.out.println("   Sources: " + mapEv.getSources());
									System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
									System.out.println("   Pruning Status: " + mapEv.getPruningStatus());
									System.out.println("   Negation Status: " + mapEv.getNegationStatus());
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
