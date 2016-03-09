package nlp;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import db.DBManager;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import model.Concept;
import model.ConceptFactory;
import model.EligibilityCriteria;

public class ConceptExtractor {
	// private static HashSet<String> excluded = new HashSet<String>(); // CUI
	private MetaMapApi mmapi;
	private static String options = "-Q 2 -i -k cell,fish,ftcn,idcn,inpr,menp,mnob,podg,qlco,qnco,spco,tmco -R SNOMEDCT_US";
	private static NumberFormat format = NumberFormat.getInstance(Locale.US);

	public ConceptExtractor() {
		mmapi = new MetaMapApiImpl();
		mmapi.setOptions(options);
	}

	public ConceptExtractor(String host) {
		mmapi = new MetaMapApiImpl();
		if (!host.equals("localhost"))
			mmapi.setHost(host);
		mmapi.setOptions(options);
	}

	public ProcessingUnit process(ProcessingUnit pu, boolean save) {
		boolean processed = pu.isProcessed();
		if (!processed){
			List<Match> matches;
			// metamap processing starts
			long startTime = System.nanoTime();
			for (EligibilityCriteria ec : pu.getCriteriaSet().getEligibilityCriteriaList()) {
				matches = getMetamapMatches(ec.getUtterance());
				ec.setMatches(matches);
				int aasd = 0;
			}
			// metamap processing ends
			long endTime = System.nanoTime();

			// feedback output
			Number number;
			double time = 0;
			try {
				number = format.parse(String.format("%.2f", (endTime - startTime) / Math.pow(10, 11)));
				time = number.doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			pu.setTime(time);
		}
		// save the results in the database
		if (save)
			DBManager.getInstance().saveProcessingUnit(pu);
		return pu;
	}

	private List<Match> getMetamapMatches(String utterance) {
		List<Match> matches = new ArrayList<Match>();
		try {
			List<Result> result = metamapQuery(utterance);
			for (Result res : result)
				for (Utterance uttr : res.getUtteranceList())
					for (PCM pcm : uttr.getPCMList())
						for (Mapping map : pcm.getMappingList())
							for (Ev mapEv : map.getEvList()) {
								Concept c = ConceptFactory.getConcept(mapEv.getConceptId());
								if (c == null)
									continue;
								Match match = new Match(c, pcm.getPhrase().getPhraseText(), mapEv.getConceptName(),
										mapEv.getPreferredName(), mapEv.getMatchedWords());
								matches.add(match);
							}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matches;
	}

	private List<Result> metamapQuery(String text) throws IOException {
		List<Result> resultList = new ArrayList<>();
		try {
			resultList = mmapi.processCitationsFromString(text);
		} catch (Exception e) {
			System.err.println("Restarting servers...");
		}
		return resultList;
	}
}
