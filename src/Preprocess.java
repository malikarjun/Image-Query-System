import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.jena.rdf.model.*;

public class Preprocess {


	

	String res = Globals.res;
	String prop = Globals.prop;

	public Preprocess() {
	}
	
	void writeRDFAnnotations( String id, Model model) throws Exception {
		File file = new File(Globals.rdf_path + id);
		FileOutputStream fop = new FileOutputStream(file,true);
		model.write(fop, "N-TRIPLES");
		
	}
	
	

	
	void parseSemanticGraph(Model model, Node node) {
		Resource root = model.createResource(res + node.data.word);
		

		
		if(node.data.word != null && node.data.word.matches("(.*)_(.*)")) 
			model.createResource(res + "Sen_node").addProperty(model.createProperty(prop + "HasIns"), model.createResource(res + node.data.word));
		
		
		for(int i = 0; i < node.children.size(); i++) {
			Node child = node.children.get(i); 
			if(child.children.size() == 0) 
				root.addProperty(model.createProperty(prop + child.data.Edge), child.data.word);
			else {
				root.addProperty(model.createProperty(prop + child.data.Edge), model.createResource(res + child.data.word));
				parseSemanticGraph(model, child);
			}
			
		}
	}
	
	Model createModel(Node node, String url, String caption) {
		Model model = ModelFactory.createDefaultModel();
		
//		String modelText = "@prefix res: <http://dbpedia.org/resource> .";
//		InputStream stream = new ByteArrayInputStream(modelText.getBytes(StandardCharsets.UTF_8));
//		model.read(stream, null, "N-TRIPLES");
		    
		model.createResource(res + url).addProperty(model.createProperty(prop + "HasSen"), model.createResource(res + "Sen_node"));
		model.createResource(res + "Sen_node").addProperty(model.createProperty(prop + "SenContent"), caption);
		parseSemanticGraph(model, node);		
		
		return model;


	}

}
