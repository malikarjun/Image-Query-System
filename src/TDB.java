import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

import com.google.gson.Gson;


public class TDB {
	Dataset ds;
	Preprocess pp;
	Gson gson;
	QueryExpansion qe;
	Kparser kp;
	
	public TDB() {
		ds = TDBFactory.createDataset(Globals.tdb_path);
		pp = new Preprocess();
		kp = new Kparser();
		qe = new QueryExpansion();
	}
	
	public TDB(String tdb_path) {
		ds = TDBFactory.createDataset(tdb_path);
		pp = new Preprocess();
		kp = new Kparser();
		qe = new QueryExpansion();
	}
	
	public void addModel(String caption, String url ) throws Exception {
		
	    gson = new Gson();

	    String lines[] = caption.split("\\n");
	    

	    
	    for(String line:lines) {
	    	Node node;
	    	String id;
	    	Model model;
	    	try {
	    		
	    		if(line.length() == 0) {
	    			File file = new File(Globals.rdf_path + url);
	    			file.createNewFile();
	    			continue;
	    		}
	    		
	    		System.out.println("Trying for " + line);
	    		node = gson.fromJson(kp.parse(line), Node.class);	
//			    node.print();
			    
			    // Adding rules 1-3
			    Rules rules = new Rules(node);
		    	rules.rulesForData(node);
			    
			    // Note that url of the image has been used as its identifier (id). Hence, id and url are the same.
		    	id = url;
		    	
			    model = pp.createModel(node, url, line);
			    pp.writeRDFAnnotations(id, model);
	    		
	    	}catch(Exception e) {
	    		System.out.println("Failed for " + line);
	    		e.printStackTrace();
	    		break;
	    	}

			
			
			ds.begin(ReadWrite.WRITE);
			try {
				
				if(!ds.containsNamedModel(id))
					ds.addNamedModel(id, model);
				else 
					ds.getNamedModel(id).add(model);
				
				
				ds.commit();
			}
			finally {
				ds.end();
			}	
	    }
		
	}
	
	public void checkTDB() {

		ds.begin(ReadWrite.READ);
		try {
		
		    String sparqlQueryString = "PREFIX res: <resource:>  " +
		    							"PREFIX prop: <property:>  " + 
		    							"select ?g ?p ?o where { graph ?g { ?s ?p ?o .   } } ";

		    org.apache.jena.query.Query query = QueryFactory.create(sparqlQueryString) ;
		    QueryExecution qexec = QueryExecutionFactory.create(query, ds) ;
		    ResultSet results = qexec.execSelect() ;
		    ResultSetFormatter.out(results) ;
			
			Iterator<String> graphNames = ds.listNames();
			while (graphNames.hasNext()) {
			    String graphName = graphNames.next();
			    System.out.println(graphName);
			}
			

		}
		finally {
			ds.end();
		}	
	}
	
	public ResultSet queryTDB(String query_string) throws Exception {

		ResultSet results = null; 
		try {
			
			ds.begin(ReadWrite.READ);
			String[] words = query_string.split("\\s+");
			
			String predicate = "";
			for(String word:words)
			{
				if(predicate.length() != 0)
					predicate += "union";
				predicate += " { ?s ?p \"" + word +  "\" .} ";
			}

//		    String sparqlQueryString = "PREFIX res: <resource:>  " +
//		    							"PREFIX prop: <property:>  " + 
//		    							"select distinct ?g { graph ?g { " + predicate + " } } ";
			
			
			String sparql = qe.createSPARQLQuery(query_string);
//			System.out.println(sparql);
			

			
		    String sparqlQueryString = "PREFIX res: <resource:>  " +
		    							"PREFIX prop: <property:>  " + 
    									"select distinct ?g where { graph ?g { " + sparql + " } }";

		    
		    
//		    String sparqlQueryString = "PREFIX res: <resource:>  " +
//					"PREFIX prop: <property:>  " + 
//					"select ?g where { graph ?g {?s prop:instance_of ?o . ?ss ?p res:fight} } ";
//		    System.out.println(sparqlQueryString);

		    org.apache.jena.query.Query query = QueryFactory.create(sparqlQueryString) ;
		    QueryExecution qexec = QueryExecutionFactory.create(query, ds) ;
		   results = qexec.execSelect() ;

//		    ResultSetFormatter.out(results) ;
		    
			
//			Iterator<String> graphNames = ds.listNames();
//			while (graphNames.hasNext()) {
//			    String graphName = graphNames.next();
//			    System.out.println(graphName);
//			}
			

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ds.end();
		}
		return results;
	}
	
	
}
