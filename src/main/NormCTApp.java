package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import nlp.ConceptExtractor;
import nlp.ProcessingUnit;

public class NormCTApp {
	private static final boolean STORE = true;
	private static final int MEAN = 10;
	private static final String NAME = "NormCTApp";

	public static void main(String[] args) {
		// read properties config file
		loadConfig();		
		if (args.length == 6 && args[0].equals("-f") && args[2].equals("-o") && args[4].equals("-l")) {
			String filepath = args[1];
			if (new File(filepath).getAbsoluteFile().exists()) {
				int offset = Integer.parseInt(args[3]);
				int limit = Integer.parseInt(args[5]);
				processBatch(filepath, offset, limit);
			} else {
				System.err.println("The filepath: " + filepath + " doesn't exists.");
				usage();
			}
		} else if (args.length == 1 && args[0].equals("-d")) {
			processBatch(Environment.TRIALS_PATH, 0, 10000);
		} else if (args.length == 2 && args[0].equals("-t")) {
			String trial = args[1];
			processTrial(trial);
		} else if (args.length > 1 && args[0].equals("-tl")) {
			List<String> trials = new ArrayList<String>();
			for(int i=1; i< args.length; i++){
				trials.add(args[i]);
			}
			processTrialList(trials);
		} else
			usage();
	}

	private static void usage() {
		HelpFormatter hf = new HelpFormatter();
		
		Options options = new Options();
		Option singleTrial = Option.builder("t").hasArg().argName("nctid").desc("Process a single trial <nctid>").build();
		Option filepath = Option.builder("f").hasArg().argName("filepath").desc("Process the set of trials located in <filepath>").build();
		Option offset = Option.builder("o").hasArg().argName("offset").desc("The position in the directory of the initial trial to be processed").build();
		Option limit = Option.builder("l").hasArg().argName("limit").desc("The maximum number of trials to be processed").build();
		Option report = Option.builder("r").hasArg().argName("nctid").desc("The trial to build the report of").build();
		
		String header = "Options:\n\n";
		String footer = "\nIMPORTANT: Files must be in XML format.\nPlease report issues at http://gib.fi.upm.es/contact";
		
		options
			.addOption(singleTrial)
			.addOption(report)
			.addOption(filepath)
			.addOption(offset)
			.addOption(limit);
		
		String usage = "\n" + NAME + " [-t|-r <nctid>] [-f <filepath> -o <offset> -l <limit>]";
		
		hf.printHelp(usage,header,options,footer,false);
	}

	private static List<ProcessingUnit> processBatch(String filepath, int offset, int limit) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String path = filepath;
		File[] files = new File(path).listFiles();
		ConceptExtractor ce = new ConceptExtractor(Environment.METAMAP_HOST);
		List<ProcessingUnit> pulist = new ArrayList<>();

		// Time estimation
		System.out.print("Processing trials... estimated time: ");
		if (((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 60 > 60)
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 3600 + " h\n");
		else if (((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 60 > 1)
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 60 + " min\n");
		else
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * MEAN + " s\n");

		// Processing
		int j = 0;
		for (int i = offset; i < files.length && i < limit + offset; i++) {
			File f = files[i];
			if (j >= limit + offset)
				break;
			if (f.getName().contains("NCT")) {
				j++;
				System.out.print("> "+dateFormat.format(new Date()) + " [" + j + "]" + "[" + f.getName().replace(".xml", "") + "] ");
				ProcessingUnit pu = new ProcessingUnit (f.getName().replace(".xml", ""));
				ce.process(pu, STORE);
				System.out.println("< "+dateFormat.format(new Date()) + " [" + j + "]" + "[" + f.getName().replace(".xml", "") + "] " + pu.getTime()*(-1));
				pulist.add(pu);
			}
		}
		System.out.println("< Done");
		return pulist;
	}
	
	private static List<ProcessingUnit> processTrialList(List<String> trials) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		ConceptExtractor ce = new ConceptExtractor(Environment.METAMAP_HOST);
		List<ProcessingUnit> pulist = new ArrayList<>();
		
		// Processing
		for(int i=0; i < trials.size(); i++){
			System.out.println("> "+dateFormat.format(new Date())+" ["+(i+1)+"]" + "[" + trials.get(i) + "] ");
			ProcessingUnit pu = new ProcessingUnit(trials.get(i));
			ce.process(pu, STORE);
			if(pu.isProcessed()){
				System.out.println("< "+dateFormat.format(new Date())+ " ["+(i+1)+"]" + "[" + trials.get(i) + "] ENDED");
			} else {
				System.out.println("< "+dateFormat.format(new Date())+ " ["+(i+1)+"]" + "[" + trials.get(i) + "] NOT AVAILABLE");
			}
			pulist.add(pu);
		}
		System.out.println("< Done");
		return pulist;
	}

	private static ProcessingUnit processTrial(String nctid) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		ConceptExtractor ce = new ConceptExtractor(Environment.METAMAP_HOST);
		System.out.println("> "+dateFormat.format(new Date())+ " [*]" + "[" + nctid + "] ");
		ProcessingUnit pu = new ProcessingUnit(nctid);
		ce.process(pu, STORE);
		if(pu.isProcessed()){
			System.out.println("< "+dateFormat.format(new Date())+ " [*]" + "[" + nctid + "] ENDED");
		} else {
			System.out.println("< "+dateFormat.format(new Date())+ " [*]" + "[" + nctid + "] NOT AVAILABLE");
		}
		System.out.println("< Done");
		return pu;
	}
	
	private static void loadConfig(){
		Properties config = new Properties();
		InputStream input = null;
		
    	try {
        
    		String filename = "config.properties";
    		input = NormCTApp.class.getClassLoader().getResourceAsStream(filename);
    		if(input==null){
    	            System.out.println("Unable to find " + filename);
    		    return;
    		}
    		//load a properties file from class path, inside static method
    		config.load(input);
    	    
    	    String metamap_host = config.getProperty("metamap.host");
    	    String metamap_options = config.getProperty("metamap.options");
    	    String trials_path = config.getProperty("trials.path");
    	    String excluded_snomed_hierarchies = config.getProperty("excluded.snomed.hierarchies");
    	        	    
    	    Environment.METAMAP_HOST = metamap_host == null? Environment.METAMAP_HOST : metamap_host;
    	    Environment.METAMAP_OPTIONS = metamap_options == null? Environment.METAMAP_OPTIONS : metamap_options;
    	    Environment.TRIALS_PATH = trials_path == null? Environment.TRIALS_PATH : trials_path;
    	    
    	    if(excluded_snomed_hierarchies != null){
        	    ArrayList<String> hierarchies = new ArrayList<>(Arrays.asList(excluded_snomed_hierarchies.split(",")));
    	    	for(String h: hierarchies){
    	    		h.trim();
    	    	}
    	    	Environment.EXCLUDED_BRANCHES = excluded_snomed_hierarchies == null? Environment.EXCLUDED_BRANCHES : hierarchies;
    	    }
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }
	}
}
