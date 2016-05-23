package nlp;

import model.ClinicalTrial;
import model.CriteriaSet;
import model.EligibilityCriteria;

public class ProcessingUnit {

	private ClinicalTrial clinicalTrial;
	private double time;
	private CriteriaSet criteria;

	public ProcessingUnit(String nctid) {
		this.clinicalTrial = new ClinicalTrial(nctid);
		if(this.clinicalTrial.getNctid() != null){
			this.criteria = new CriteriaSet(nctid, clinicalTrial.getCriteriaTextBlock());
			this.time = 0.0;
		}
	}

	public ClinicalTrial getClinicalTrial() {
		return clinicalTrial;
	}
	
	public CriteriaSet getCriteriaSet(){
		return criteria;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public boolean isProcessed() {
		return time > 0.0;
	}
	
	public void printResults(){
		if(time > 0.0){
			for(EligibilityCriteria ec : criteria.getEligibilityCriteriaList()){
				for(Match m: ec.getMatches()){
					m.print();
					System.out.println("---------------------------");
				}
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
			}
		}
	}
}
