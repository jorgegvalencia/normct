package main;

import java.util.ArrayList;
import java.util.Arrays;

public final class Environment {
	//+ "cell,fish,ftcn,geoa,idcn,inpr,menp,mnob,podg,qlco,qnco,spco,tmco "
	// "aapp"	
//	public static String METAMAP_HOST = "192.168.33.10"; //"luria.dia.fi.upm.es"
	public static String METAMAP_HOST = "127.0.0.1"; //"luria.dia.fi.upm.es"
	public static String METAMAP_OPTIONS = "-y -l -i --negex -R SNOMEDCT_US"; // --UDA UDAfile --nomap Exclusions
	// --restrict_to_sts resa,podg,horm,diap,dsyn,topp,chvf,neop,fndg,hlca 
	public static String TRIALS_PATH = "data/trials/";
	public static ArrayList<String> XML_ATTRIBUTES = new ArrayList<>(Arrays.asList(
					"study_type",
					"gender",
					"url",
					"overall_status",
					"lastchanged_date",
					"start_date"
					));
	public static ArrayList<String> EXCLUDED_BRANCHES = new ArrayList<>(Arrays.asList(
					"attribute",
					"contextual qualifier",
					"qualifier value",
					"environment",
					"geographic location",
					"person",
					"severity modifier",
					"core metadata concept",
					"ethnic group",
					"foundation metadata concept"
					));
}
