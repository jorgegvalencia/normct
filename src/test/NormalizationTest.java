package test;

import normalization.CoreDatasetServiceClient;

public class NormalizationTest {

	public static void main(String[] args) {
		CoreDatasetServiceClient normclient = new CoreDatasetServiceClient();
		String normalform = normclient.getNormalFormAsString("116154003", true);
		System.out.println(normalform);
	}

}
