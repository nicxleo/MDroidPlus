package edu.wm.cs.mplus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.wm.cs.mplus.detectors.MutationLocationDetector;
import edu.wm.cs.mplus.detectors.MutationLocationListBuilder;
import edu.wm.cs.mplus.detectors.xml.ActivityNotDefinedDetector;
import edu.wm.cs.mplus.detectors.xml.InvalidActivityNameDetector;
import edu.wm.cs.mplus.detectors.xml.InvalidColorDetector;
import edu.wm.cs.mplus.detectors.xml.InvalidLabelDetector;
import edu.wm.cs.mplus.detectors.xml.MissingPermissionDetector;
import edu.wm.cs.mplus.detectors.xml.WrongStringResourceDetector;
import edu.wm.cs.mplus.detectors.xml.SDKVersionDetector;
import edu.wm.cs.mplus.detectors.xml.WrongMainActivityDetector;
import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.processors.MutationsProcessor;
import edu.wm.cs.mplus.processors.SourceCodeProcessor;
import edu.wm.cs.mplus.processors.TextBasedDetectionsProcessor;
//import edu.wm.cs.semeru.core.helpers.TerminalHelper;

public class MPlus {

	public static void main(String[] args){
		try {
			runMPlus(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void runMPlus(String[] args) throws IOException{

		//Usage Error
		if(args.length != 4){
			System.out.println("******* ERROR: INCORRECT USAGE *******");
			System.out.println("Argument List:");
			System.out.println("1. Binaries path");
			System.out.println("2. App Source Code path");
			System.out.println("3. App Name");
			System.out.println("4. Mutants path");
			return;
		}

		//Getting arguments
		String binariesFolder = args[0];
		String rootPath = args[1];
		String appName = args[2];
		String mutantsFolder = args[3];


		List<MutationLocationDetector> textBasedDetectors = new ArrayList<>();
		textBasedDetectors.add(new ActivityNotDefinedDetector());
		textBasedDetectors.add(new InvalidActivityNameDetector());
		textBasedDetectors.add(new InvalidColorDetector());
		textBasedDetectors.add(new InvalidLabelDetector());
		textBasedDetectors.add(new MissingPermissionDetector());
		textBasedDetectors.add(new WrongStringResourceDetector());
		textBasedDetectors.add(new SDKVersionDetector());
		textBasedDetectors.add(new WrongMainActivityDetector());


		//1. Run detection phase for Text-based detectors
		HashMap<MutationType, List<MutationLocation>> locations = TextBasedDetectionsProcessor.process(rootPath, textBasedDetectors);

		Set<MutationType> keys = locations.keySet();
		List<MutationLocation> list = null;
		for (MutationType mutationType : keys) {
			list = locations.get(mutationType);
			for (MutationLocation mutationLocation : list) {
				System.out.println("File: "+mutationLocation.getFilePath()+", start line:" + mutationLocation.getStartLine()+", end line: "+mutationLocation.getEndLine()+", start column"+mutationLocation.getStartColumn());
			}
		}

		//2. Run detection phase for AST-based detectors
		//2.1 Preprocessing: Find locations to target API calls (including calls to constructors)
		//SourceCodeProcessor scp = SourceCodeProcessor.getInstance(); (not safe, if MPlus is executed on different apps)
		SourceCodeProcessor scp = new SourceCodeProcessor();
		locations.putAll( scp.processFolder(rootPath, binariesFolder, appName));

		//2.2. Call the detectors on each location in order to find any extra information required for each case.
		locations = scp.findExtraInfoRequired(locations);

		//3. Build MutationLocation List
		List<MutationLocation> mutationLocationList = MutationLocationListBuilder.buildList(locations);
		System.out.println("Total Locations: "+mutationLocationList.size());

		//3. Run mutation phase
		MutationsProcessor mProcessor = new MutationsProcessor(rootPath, appName, mutantsFolder);
		mProcessor.process(mutationLocationList);
	}

}
