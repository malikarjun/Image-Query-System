
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.jena.rdf.model.Model;

import com.google.gson.Gson;


public class Rules {
	
	Map<String, String> words = new TreeMap<>();
	
	public Rules(Node node) {
//		POSTagging(node);
	}
	
	
	void POSTagging(Node node) {
		
		if(node == null)
			return;
		
		if(node.data.isEntity || node.data.isEvent) {
			String[] part = node.data.word.split("_");
			words.put(part[1]+ "_" + part[0], node.data.pos) ;
		}
		
		for(int i = 0; i < node.children.size(); i++) 
			POSTagging(node.children.get(i));
		
	}
	
	
	void rule1(Node node) {
		if(node == null)
			return;
		
		
		for(int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i); 
			
			if(child.data.word.contains("group") && child.data.Edge != null && child.data.Edge.contains("agent")) {
				node.children.remove(child);
				
				// Find the correct child to replace the removed agent.
				for(int j = 0; j < child.children.size(); j++) {
					Node grand_child = child.children.get(j);
					
					if(grand_child.data.Edge.contains("is_part_of")) {
						grand_child.data.Edge = "agent";
						node.children.add(grand_child);
					}
				}
			}
			rule1(child);
		}
		
	}
	
	void rule2(Node node) {
		// nnpr refers to noun or pronoun
		Node nnpr = null;
		if (node.children.size() >= 2) {
			for (int i = 0; i < node.children.size(); i++) {
				Node child = node.children.get(i);
				if(child.data.pos.contains("NN") || child.data.pos.contains("PR"))
					nnpr = child;
				else if(nnpr != null && child.data.pos.contains("VBG")) {
					nnpr.data.Edge = "agent";
					child.children.add(nnpr);
					return;
				}
			}
		}
	
	}
	
	void rule3(Node node) {
		if(node == null )
			return;
		
//		System.out.println(node.data.word);

		
		int no_of_adj = 0;
		Node grand_child = null;
		for (int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i);
			if(child.data.pos.contains("JJ") ) {
				no_of_adj++;

				
				if(child.children.size() > 0) {
					grand_child = child.children.get(0);

				}

			}
		}
		if(no_of_adj > 1 && node.data.pos.contains("NN")) {
			for (int i = 0; i < node.children.size(); i++) {
				Node child = node.children.get(i);
				if(child.data.pos.contains("JJ") ) {
					if(child.children.size() == 0) {
						
						Node temp = (Node)Node.deepClone(grand_child);
						String[] part = child.data.word.split("_");
						
						temp.data.word = part[0];
						child.children.add(temp);
						
					}
				}
			}
			return;
		}
		
		for (int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i);
			rule3(child);
		}
			
		
	}	

	
	void rulesForData(Node node) {
		
		rule1(node);
		rule2(node);
		try {
			rule3(node);
		}catch(Exception e) {
			return;
		}
		
//		return node;
	}
	public static void main (String args[]) throws Exception {
		
		Preprocess pp = new Preprocess();
		Kparser kp = new Kparser();
		Rules rules;
		
		Scanner input = new Scanner(System.in);
		String caption = input.nextLine();
		input.close();
		
		
	    Gson gson = new Gson();

	    Node node = gson.fromJson(kp.parse(caption), Node.class);	    
//	    node.print();
	    
    	System.out.println("-");
	    
	    rules = new Rules(node);
    	rules.rulesForData(node);

	    node.print();
	
	    Model model = pp.createModel(node, "https://someurl", caption);
	    model.write(System.out, "N-TRIPLES");
	    
	    for (String key: rules.words.keySet()) {
	    	System.out.println(key + ":" + rules.words.get(key));
	    }
	    
	}
}
