package test;

import java.util.List;

import normalization.CoreDatasetServiceClient;

public class NormalizationTest {

	public static void main(String[] args) {
		CoreDatasetServiceClient normclient = CoreDatasetServiceClient.getInstance();
		//String normalform = normclient.getNormalFormAsString("126926005", true);
		//System.out.println(normalform);
		List<String> padres = normclient.getParents("116154003");
		for(String padre: padres){
			System.out.println(padre);
		}
	}

}
