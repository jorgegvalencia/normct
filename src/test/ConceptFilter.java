package test;

import db.DBManager;

public class ConceptFilter {

	
	public static void main(String[] args){
		DBManager.getInstance().filterHierarchies();
	}
}
