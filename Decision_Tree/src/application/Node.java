package application;

import java.util.ArrayList;
import java.util.List;

// this Node of the tree 
public class Node {

	String attribute;	//contain the Attribute name 
	int index;     //contain the index of the next child node
	List<Node> children = new ArrayList<>(); // The list of children for this node 
	boolean isLeaf;      // if is leaf then contain the result (True or False)
	boolean classification;  // true = edible, false = poisonous
	
	Node(String attribute) {
		this.attribute = attribute;
		this.isLeaf = false;
	}
	
	Node(String attribute, boolean classification) {
		this.attribute = attribute;
		this.classification = classification;
		this.isLeaf = false;
	}

	Node(boolean classification) {
		this.isLeaf = true;
		this.classification = classification;
	}

}
