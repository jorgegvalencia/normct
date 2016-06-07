package test;

import java.util.List;

import db.DBManager;
import normalization.CoreDatasetServiceClient;

public class NormalizationTest {

	public static void main(String[] args) {
		CoreDatasetServiceClient normclient = CoreDatasetServiceClient.getInstance();
		//String normalform = normclient.getNormalFormAsString("126926005", true);
		//System.out.println(normalform);
		List<String> padres = normclient.getNextGen("277132007");
		for(String padre: padres){
			String fsn = "";
			if (!padre.contains(":node1aces")){
				List<String> asd = DBManager.getInstance().getFSN(padre);
				if(asd.size() > 0){
					fsn = asd.get(0);
					System.out.println(padre + "|" + fsn);
				}
			}
		}
	}

}
