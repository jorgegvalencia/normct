package main;

import java.util.ArrayList;
import java.util.Arrays;

public final class Environment {
	//+ "cell,fish,ftcn,geoa,idcn,inpr,menp,mnob,podg,qlco,qnco,spco,tmco "
	// "aapp"
	public static String METAMAP_HOST = "192.168.33.10"; //"luria.dia.fi.upm.es"
	public static String METAMAP_OPTIONS = "-y --restrict_to_sts resa,podg,horm,diap,dsyn,topp,chvf,neop,fndg,hlca -R SNOMEDCT_US";
	public static String TRIALS_PATH = "data/trials/";
	public static ArrayList<String> XML_ATTRIBUTES = new ArrayList<>(Arrays.asList("study_type","gender","url","overall_status","lastchanged_date","start_date"));
}
