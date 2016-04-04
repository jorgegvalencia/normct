package main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
				System.out.print(dateFormat.format(new Date()));
				System.out.print(" [" + j + "]" + "[" + f.getName().replace(".xml", "") + "] ");
				ProcessingUnit pu = new ProcessingUnit (f.getName().replace(".xml", ""));
				ce.process(pu, STORE);
				System.out.println(pu.getTime() + " s");
				pulist.add(pu);
			}
		}
		System.out.println("...done");
		return pulist;
	}

	private static ProcessingUnit processTrial(String nctid) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		ConceptExtractor ce = new ConceptExtractor(Environment.METAMAP_HOST);
		System.out.print(dateFormat.format(new Date()));
		System.out.print(" [*]" + "[" + nctid + "] ");
		ProcessingUnit pu = new ProcessingUnit(nctid);
		ce.process(pu, STORE);
		System.out.println(pu.getTime() + " s");
		return pu;
	}
}
