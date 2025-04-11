package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.*;

public class DecisionTree {

	public  int true_positive;
	public  int true_neagtive;
	public  int fasle_positive;
	public  int false_neagtive;
	public List<String[]> trainData = new ArrayList<>();
	public List<String[]> testDat= new ArrayList<>();
	//no parameter constructor 
	public DecisionTree() {
		super();
	}

	//This method to  load all data from the file in ArrayList 
	public  List<String[]> load_Data() throws IOException { 
		List<String[]> data = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader("C:/Users/abdal/OneDrive/Desktop/mushroom.csv"));
		String line;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			data.add(values); // Tab-separated values	
		}
		br.close();
		data.remove(0);
		return data;
	}

	// this method split the data to train and test data (i do shuffle to the data before split )
	public void load_Train_Data(List<String[]> data,double x) throws IOException {
		//Collections.shuffle(data);
		// Split into training and testing data sets
		int splitIndex = (int) (data.size() * x); 
		List<String[]> trainData = data.subList(0, splitIndex); 
		List<String[]> testData = data.subList(splitIndex, data.size()); 
		this.trainData=trainData;
		this.testDat=testData;
	}

	//this method to get all attributes from the file 
	public String[] load_attributes() throws IOException {
		List<String[]> data = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader("C:/Users/abdal/OneDrive/Desktop/mushroom.csv"));
		String line;
		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			data.add(values); 
		}
		br.close();
		String[] st=data.get(0);
		return st; 
	}

	// this method to Build the decision tree
	public Node buildTree(List<String[]> data,String[] st) {
		List<String> attributes = new ArrayList<>(Arrays.asList(st)); // this that i delete and change the attribute 
		List<String> mainattributes = new ArrayList<>(Arrays.asList(st)); // this i don't change anything in it  
		// Remove headers from data
		Node x = buildTree(data, 0,attributes,mainattributes ," ");
		return x;
	}

	// A method to calculate the entropy of a set of data
	private  double calculateEntropy(List<String[]> data, int targetIndex) {
		double entropy = 0;
		int totalCount = data.size();
		// Count occurrences of each class (Edible or Poisonous)
		int countEdible = 0, countPoisonous = 0;
		for (String[] row : data) {
			if (row[targetIndex].equals("EDIBLE")) {
				countEdible++;
			} else {
				countPoisonous++;
			}
		}
		// Calculate entropy using the formula
		double pEdible = (double) countEdible / totalCount;
		double pPoisonous = (double) countPoisonous / totalCount;
		if (pEdible > 0) entropy -= pEdible * Math.log(pEdible) / Math.log(2); // if =0 shift (don't need to count the value)
		if (pPoisonous > 0) entropy -= pPoisonous * Math.log(pPoisonous) / Math.log(2);
		return entropy;
	}

	// Method to calculate Information Gain
	private  double calculateInformationGain(List<String[]> data, int targetIndex, int attributeIndex) {
		double originalEntropy = calculateEntropy(data, targetIndex);
		double gain = originalEntropy ;
		List<String> attributeValues = new ArrayList<>();
		for ( int j=0;j<data.size();j++) {
			String[]row=data.get(j);
			String value = row[attributeIndex];
			if (!attributeValues.contains(value)) {
				attributeValues.add(value);
			}
		}
		for(int i=0;i<attributeValues.size();i++) {
			String z=attributeValues .get(i);
			List<String[]> list = new ArrayList<>();
			for (String[] row : data) {
				if (row[attributeIndex].equals(z)) {
					list.add(row);
				} 
			}
			double r=(list.size()/(double) data.size())*calculateEntropy(list, targetIndex);
			originalEntropy -=r; //IG=H(original)-r(weightedEntropy)
		}
		return gain;
	}

	// Method to calculate Intrinsic Value for Gain Ratio
	private static double calculateIntrinsicValue(List<String[]> data, int attributeIndex) {
		double intrinsicValue = 0;
		int totalCount = data.size();

		// Count occurrences of each value for the attribute
		List<String> attributeValues = new ArrayList<>();
		for (String[] row : data) {
			String value = row[attributeIndex];
			if (!attributeValues.contains(value)) {
				attributeValues.add(value);
			}
		}

		// Calculate Intrinsic Value
		for (String value : attributeValues) {
			int count = 0;
			for (String[] row : data) {
				if (row[attributeIndex].equals(value)) {
					count++;
				}
			}
			double p = (double) count / totalCount;
			intrinsicValue -= p * Math.log(p) / Math.log(2);
		}

		return intrinsicValue;
	}

	// Method to calculate Gain Ratio
	private  double calculateGainRatio(List<String[]> data, int targetIndex, int attributeIndex) {
		double informationGain = calculateInformationGain(data, targetIndex, attributeIndex);
		double intrinsicValue = calculateIntrinsicValue(data, attributeIndex);

		if (intrinsicValue == 0) return 0;  // Prevent division by zero
		return informationGain;
	}

	// Recursive method to build the decision tree
	private Node buildTree(List<String[]> data, int targetIndex, List<String> attributes,List<String> mainattributes,String target) {
		// If all instances have the same target value, return a leaf node
		if (allSameClass(data, targetIndex)) {
			boolean classVal = data.get(0)[targetIndex].equals("EDIBLE");
			Node n=new Node(target,classVal);
			n.isLeaf=true;
			return n; 
		}
		// if no attributes 
		if (attributes.isEmpty()) {
			boolean classVal = majorityClass(data, targetIndex);
			return new Node(classVal);
		}
		// Select the best attribute to split on
		String bestAttribute = null;
		double bestGainRatio = -1;
		int bestAttributeIndex = -1;
		double count=0;
		for (int i = 1; i < attributes.size(); i++) {
			double gainRatio = calculateGainRatio(data, targetIndex, i);
			count+=gainRatio;
			if (gainRatio > bestGainRatio) {
				bestGainRatio = gainRatio;
				bestAttribute = attributes.get(i);
				bestAttributeIndex = i;
			}
		}

		// the first run 
		if( ! target.equalsIgnoreCase(" ")) {
			Node node1 = new Node(target);
			node1.index=bestAttributeIndex;
			Node node = new Node(bestAttribute);
			node1.children.add(node);
			attributes.remove(bestAttribute);  // Remove the chosen attribute 
			List<String> attributeValues = new ArrayList<>();
			for ( int j=0;j<data.size();j++) {
				String[]row=data.get(j);
				String value = row[bestAttributeIndex];
				if (!attributeValues.contains(value)) {
					attributeValues.add(value);
				}
			}
			for(int i=0;i<attributeValues.size();i++) {
				String z=attributeValues .get(i);
				List<String[]> list = new ArrayList<>();
				for (String[] row : data) {
					if (row[bestAttributeIndex].equals(z)) {
						list.add(row);
					} 

				}
				node.children.add(buildTree(list, targetIndex, new ArrayList<>(attributes),mainattributes,z));
			}	
			return node1;
		}
		Node node = new Node(bestAttribute);
		node.index=bestAttributeIndex;
		attributes.remove(bestAttribute);  // Remove the chosen attribute 
		List<String> attributeValues = new ArrayList<>();
		for ( int j=0;j<data.size();j++) {
			String[]row=data.get(j);
			String value = row[bestAttributeIndex];
			if (!attributeValues.contains(value)) {
				attributeValues.add(value);
			}
		}
		for(int i=0;i<attributeValues.size();i++) {
		//for example color ==>> Red //green
			String z=attributeValues.get(i);
			List<String[]> list = new ArrayList<>();
			for (String[] row : data) {
				if (row[bestAttributeIndex].equals(z)) {
					list.add(row);
				} 
			}
			node.children.add(buildTree(list, targetIndex, new ArrayList<>(attributes),mainattributes,z));
		}	
		return node;
	}

	// Method to check if all instances have the same class (Edible/Poisonous)
	private  boolean allSameClass(List<String[]> data, int targetIndex) {
		String firstClass = data.get(0)[targetIndex];
		for (String[] row : data) {
			if (!row[targetIndex].equals(firstClass)) {
				return false;
			}
		}
		return true;
	}

	// Method to get the majority class in case of an equal split
	private  boolean majorityClass(List<String[]> data, int targetIndex) {
		int countEdible = 0;
		int countPoisonous = 0;
		for (String[] row : data) {
			if (row[targetIndex].equals("EDIBLE")) {
				countEdible++;
			} else {
				countPoisonous++;
			}
		}
		return countEdible > countPoisonous;
	}

	// this method call the predict method and calculate the classification Details
	public  void predict(Node node,List<String[]> test ) {
		for(String[] instance:test ) {
			boolean t=predict(node,instance,0);
			if(t==true&&instance[0].equalsIgnoreCase("EDIBLE")) {
				true_positive++;
			}
			else if(t==true&&instance[0].equalsIgnoreCase("POISONOUS")) {
				false_neagtive++;
			}
			else if(t==false&&instance[0].equalsIgnoreCase("POISONOUS")) {
				true_neagtive++;
			}
			else {
				fasle_positive++;
			}

		}
	}

	// this method to test the Decision tree by using test data
	private static boolean predict(Node node, String[] instance,int x) {
		int z=x;
		if (node.isLeaf) {
			return node.classification;
		}
		if(x==0) {
			String value = instance[node.index];
			for (Node child : node.children) {
				if (child.attribute.equals(value)) {
					z=z+1;
					return predict(child, instance,z);
				}
			}}
		else {
			String value = instance[node.index];
			for (Node child : node.children) {
				for (Node child1 : child.children) {
					if (child1.attribute.equals(value)) {
						z=z+1;
						return predict(child1, instance,z);
					}
				}
			}
		}
		return false;
	}


}


