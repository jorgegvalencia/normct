package model;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import db.DBManager;
import normalization.CoreDatasetServiceClient;

public class Concept {
	private static final Pattern pattern = Pattern.compile("\\([a-z\\s/]+\\)\\z");
	private String cui;
	private String sctid;
	private String fsn;
	private String hierarchy;
	private String normalForm;
	private String focusConcept;
	private String focusConceptFsn;
	private String focusConceptHierarchy;

	private DBManager db;

	public Concept(String cui) throws InstantiationException {
		this.cui = cui;
		try {
			getSCTid();
			getFullySpecifiedName();
			getSnomedHierarchy();
			getNF();
		} catch (InstantiationException e) {
			throw new InstantiationException();
		}
	}

	public Concept(String cui, String sctid, String fsn, String hierarchy, String normalform, String focusConcept, String focusConceptFsn, String focusConceptHierarchy) {
		this.cui = cui;
		this.sctid = sctid;
		this.fsn = fsn;
		this.hierarchy = hierarchy;
		this.normalForm = normalform;
		this.focusConcept = focusConcept;
		this.focusConceptFsn = focusConceptFsn;
		this.focusConceptHierarchy = focusConceptHierarchy;
	}

	public String getCui() {
		return cui;
	}

	public String getSctid() {
		return sctid;
	}

	public String getFsn() {
		return fsn;
	}

	public String getHierarchy() {
		return hierarchy;
	}
	
	public String getNormalForm() {
		return normalForm;
	}
	
	public String getFocusConcept() {
		return focusConcept;
	}
	
	public String getFocusConceptFsn() {
		return focusConceptFsn;
	}
	
	public String getFocusConceptHierarchy() {
		return focusConceptHierarchy;
	}

	private void getSCTid() throws InstantiationException {
		if (db == null)
			db = DBManager.getInstance();
		List<String> candidates = db.getSCTID(cui);
//		System.out.println("CUI: "+ cui + " candidates: "+candidates);
		if (candidates.size() < 1){
//			System.out.println("Peto aqui 1");
			throw new InstantiationException("Could not resolve the SnomedCT identifier for the concept " + cui);
		}
		else {
			int status = 0;
			for (String candidate : candidates) {
				status = db.getStatusFromDB(candidate);
				if (status == 1)
					sctid = candidate;
			}
		}
	}

	private void getFullySpecifiedName() throws InstantiationException {
		if (db == null)
			db = DBManager.getInstance();
		List<String> fsn = db.getFSN(sctid);
//		System.out.println("SCTID:"+sctid +"FSN:"+ fsn);
		if (fsn == null || fsn.size() < 1){
			throw new InstantiationException("Could not resolve the Fully Specified Name for the concept " + cui);
		}
		else
			this.fsn = fsn.get(0);
	}

	private void getSnomedHierarchy() {
		Matcher m = pattern.matcher(fsn);
		if (m.find())
			hierarchy = m.group(0).replaceAll("\\p{Punct}", "");
		else
			hierarchy = "N/A";
	}
	
	private void getNF(){
		CoreDatasetServiceClient normclient = CoreDatasetServiceClient.getInstance();
		normalForm = normclient.getNormalFormAsString(sctid, true);
		focusConcept = normclient.getNFFocusConcept(sctid);
		focusConceptFsn = normclient.getNFFocusConceptFSN(sctid);
		if(focusConceptFsn == null){
			focusConceptFsn = fsn;
			focusConceptHierarchy = hierarchy;
		} else {
			Matcher m = pattern.matcher(focusConceptFsn);
			if (m.find())
				focusConceptHierarchy = m.group(0).replaceAll("\\p{Punct}", "");
			else
				focusConceptHierarchy = "N/A";
		}
	}
	
	public HashMap<String, String> getNFRefinements() {
		CoreDatasetServiceClient normclient = CoreDatasetServiceClient.getInstance();
		return normclient.getNFRefinements(sctid);
	}
}
