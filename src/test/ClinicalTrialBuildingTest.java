package test;

import java.io.File;
import model.ClinicalTrial;
import model.CriteriaSet;
import model.EligibilityCriteria;

public class ClinicalTrialBuildingTest {

	public static void main(String[] args) {	
		File[] files = new File("data/trials/").listFiles();
		for(File f: files){
			if(f.isFile()){
				ClinicalTrial trial = new ClinicalTrial(f.getName().replace(".xml", ""));
				System.out.println(trial.getNctid());
				CriteriaSet set = new CriteriaSet(trial.getNctid(),trial.getCriteriaTextBlock());
				System.out.println("TEXTBLOCK:  ______________________________________");
				System.out.println(set.getCriteriaTextBlock());
				System.out.println("UTTERANCES: ______________________________________");
				for(EligibilityCriteria ec: set.getEligibilityCriteriaList()){
					System.out.println(ec.getUtterance());
					System.out.println("-");
				}
				System.out.println("__________________________________________________");
			}
		}
	}

}
