import java.util.Scanner;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import com.google.gson.Gson;



public class QueryExpansion {
	Kparser kp;
	Gson gson;
	String query = "";
	String res = "res:";
	String prop = "prop:";
	
	@SuppressWarnings("unused")
	String createSPARQLQuery(String query) throws Exception {
		
		kp = new Kparser();
	    gson = new Gson();
	    
	    // We're not using ConceptNet due to reasons mentioned in the report.
	    if(query.split(" ").length > 7) {
	    	System.out.println("Using ConceptNet");
		    String simple_query = ConceptNet.simplify(query);
		    
		    if(simple_query != null)     	
		    	query = simple_query;
		    
	    }
	    System.out.println(query);
	    
	    Node node = gson.fromJson(kp.parse(query), Node.class);
	    Rules rules = new Rules(node);
    	rules.rulesForData(node);
	        
	    return expandQuery(node);
	}
	
	void parseSemanticGraph(Node node) {

		
		
		String root = node.data.word;
		
		
		if(node.data.word != null && node.data.word.matches("(.*)_(.*)")) {
			
			query += "?" + "Sen_node" + " " + prop + "HasIns" + " " + "?" + node.data.word + " ." + "\n"; 
		}
		
		
		for(int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i); 
			
			if(child.data.Edge != null && !child.data.Edge.contains("dependent")) {
				if(node.data.isEntity && child.data.Edge.contains("instance_of")) {
					
//					query += "{" + "\n";
					if(child.children.size() == 0)
						query += "{ " +  "?" + root + " " + (prop + child.data.Edge) + " "+ "\"" + child.data.word + "\"" + " ." + "}" + "\n";
					else
						query += "{ " +  "?" + root + " " + (prop + child.data.Edge) + " "+ (res + child.data.word) + " ." + "}" + "\n";
			
					query += "union" + "\n";
					query += "{" + "\n";
					query += "?" + root + " " + (prop + child.data.Edge) + " " + "?" + child.data.word + "_kind" + " ."  + "\n";
					query += "?" + child.data.word + "_kind" + " " + (prop + "is_subclass_of") + " "+ "\"" + child.data.word+ "\"" + " ." + "\n";
					query += "}" + "\n";
//					query += "}" + "\n";
//					query += "union" +  "\n";
				}
				else if(child.data.Edge != null && !child.data.Edge.contains("is_subclass_of") && root != null) {
//					query += "{" + "\n";
					if( (child.data.isEvent || child.data.isEntity) ) 
						query +=  "?" + root + " " + (prop + child.data.Edge) + " " + "?" +  child.data.word + "." + "\n"; 
					else if(child.children.size() == 0)
						query += "?" + root + " " + (prop + child.data.Edge) + " " + "\"" + child.data.word + "\""+ "." +  "\n"; 
					else
						query += "?" + root + " " + (prop + child.data.Edge) + " " +(res +  child.data.word) + "." +  "\n"; 
//					query += "}" + "\n";
//					query += "union" +  "\n";
				}
			}
			

	
			if(child.children.size() > 0)
				parseSemanticGraph(child);
				
			
			
		}
	}
	
	String expandQuery(Node node) {

//		node.print();
		parseSemanticGraph(node);	
		
//		System.out.println("\n\n" + query);

		return query;


	}
	public static void main (String args[]) throws Exception {
		QueryExpansion qe = new QueryExpansion();
		Scanner input = new Scanner(System.in);
		String query = input.nextLine();

		input.close();
		
		System.out.println(qe.createSPARQLQuery(query));
	}
}
