package edu.wm.cs.mplus.processors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.MutationOperator;
import edu.wm.cs.mplus.operators.MutationOperatorFactory;

public class MutationsProcessor {
	
	
	private String appFolder;
	private String appName;
	private String mutantsRootFolder;
	
	
	
	
	public MutationsProcessor(String appFolder, String appName, String mutantsRootFolder) {
		super();
		this.appFolder = appFolder;
		this.appName = appName;
		this.mutantsRootFolder = mutantsRootFolder;
	}

	
	private void setupMutantFolder(int mutantIndex) throws IOException{
		FileUtils.copyDirectory(new File(getAppFolder()), 
				new File(getMutantsRootFolder()+File.separator+getAppName()+"-mutant"+mutantIndex));
	
	}

	public  void process(List<MutationLocation> locations) throws IOException{
		MutationOperatorFactory factory = MutationOperatorFactory.getInstance();
		MutationOperator operator = null;
		int mutantIndex  = 1;
		String mutantFolder = null;
		String newMutationPath = null;
		BufferedWriter writer = new BufferedWriter(new FileWriter(getMutantsRootFolder()+File.separator+getAppName()+"-mutants.log"));
		for (MutationLocation mutationLocation : locations) {
			try {
				setupMutantFolder(mutantIndex);
				System.out.println("Mutant: "+mutantIndex + " - Type: "+ mutationLocation.getType());
				operator = factory.getOperator(mutationLocation.getType().getId());
				
				mutantFolder = getMutantsRootFolder()+File.separator+getAppName()+"-mutant"+mutantIndex + File.separator;
				//The mutant should be written in mutantFolder
				
				newMutationPath = mutationLocation.getFilePath().replace(appFolder,mutantFolder);
				//System.out.println(newMutationPath);
				mutationLocation.setFilePath(newMutationPath);
				operator.performMutation(mutationLocation);
				
				writer.write("Mutant "+mutantIndex+": "+mutationLocation.getFilePath()+"; "+mutationLocation.getType().getName()+" in line "+(mutationLocation.getStartLine()+1));
				writer.newLine();
				writer.flush();
				
			} catch (Exception e) {
				Logger.getLogger(MutationsProcessor.class.getName()).warning("- Error generating mutant  "+mutantIndex);
				e.printStackTrace();
			}
			mutantIndex++;
		}
		writer.close();
	}


	
	
	public String getAppFolder() {
		return appFolder;
	}


	public void setAppFolder(String appFolder) {
		this.appFolder = appFolder;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getMutantsRootFolder() {
		return mutantsRootFolder;
	}


	public void setMutantsRootFolder(String mutantsRootFolder) {
		this.mutantsRootFolder = mutantsRootFolder;
	}
	
	
}
