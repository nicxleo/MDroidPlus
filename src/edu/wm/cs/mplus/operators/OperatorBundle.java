package edu.wm.cs.mplus.operators;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import edu.wm.cs.mplus.detectors.MutationLocationDetector;
import edu.wm.cs.mplus.detectors.xml.ActivityNotDefinedDetector;
import edu.wm.cs.mplus.detectors.xml.InvalidActivityNameDetector;
import edu.wm.cs.mplus.detectors.xml.InvalidColorDetector;
import edu.wm.cs.mplus.detectors.xml.InvalidLabelDetector;
import edu.wm.cs.mplus.detectors.xml.MissingPermissionDetector;
import edu.wm.cs.mplus.detectors.xml.SDKVersionDetector;
import edu.wm.cs.mplus.detectors.xml.WrongMainActivityDetector;
import edu.wm.cs.mplus.detectors.xml.WrongStringResourceDetector;

public class OperatorBundle {

	private static final String PROPERTY_FILE_NAME = "operators";
	private ResourceBundle bundle;

	public enum TextBasedOperator {
		ActivityNotDefined(1), InvalidActivityName(3), InvalidColor(3), InvalidLabel(28), MissingPermission(9), WrongStringResource(10), SDKVersion(12), WrongMainActivity(8);

		public int id;

		TextBasedOperator(int id) {
			this.id = id;
		}
	}

	public OperatorBundle(String propertyDir) {
		init(propertyDir);
	}

	
	public boolean isOperatorSelected(String id) {
		return bundle.containsKey(id);
	}


	public List<MutationLocationDetector> getTextBasedDetectors() {
		List<MutationLocationDetector> textBasedDetectors = new ArrayList<>();
		
		if(bundle.containsKey(TextBasedOperator.ActivityNotDefined.id+"")) {
			textBasedDetectors.add(new ActivityNotDefinedDetector());
		} if(bundle.containsKey(TextBasedOperator.InvalidActivityName.id+"")) {
			textBasedDetectors.add(new InvalidActivityNameDetector());
		} if(bundle.containsKey(TextBasedOperator.InvalidColor.id+"")) {
			textBasedDetectors.add(new InvalidColorDetector());
		} if(bundle.containsKey(TextBasedOperator.InvalidLabel.id+"")) {
			textBasedDetectors.add(new InvalidLabelDetector());
		} if(bundle.containsKey(TextBasedOperator.MissingPermission.id+"")) {
			textBasedDetectors.add(new MissingPermissionDetector());
		} if(bundle.containsKey(TextBasedOperator.WrongStringResource.id+"")) {
			textBasedDetectors.add(new WrongStringResourceDetector());
		} if(bundle.containsKey(TextBasedOperator.SDKVersion.id+"")) {
			textBasedDetectors.add(new SDKVersionDetector());
		} if(bundle.containsKey(TextBasedOperator.WrongMainActivity.id+"")) {
			textBasedDetectors.add(new WrongMainActivityDetector());
		}

		return textBasedDetectors;
	}
	
	public String printSelectedOperators() {
		
		Set<String> ids = bundle.keySet();
		String selectedOperators = "Selected Operators: "+ids.size()+"\n";

		for (String id : ids) {
			selectedOperators += id+" "+bundle.getString(id)+"\n";
		}
		selectedOperators += "------------\n";
		
		return selectedOperators;
	}



	private void init(String propertyDir) {
		File file = new File(propertyDir);
		URL url = null;

		try {
			url = file.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		URL[] urls = {url};
		ClassLoader loader = new URLClassLoader(urls);
		bundle = ResourceBundle.getBundle(PROPERTY_FILE_NAME, Locale.getDefault(), loader);
	}

}
