import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;


public class Query {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TDB tdb = new TDB();
		String base_path = "/home/pixel/mallikarjun/datasets/";
		
	
 		// Take input from command line
		Scanner input = new Scanner(System.in);
		String query = input.nextLine();
		
		ResultSet results =	tdb.queryTDB(query);
		
// 		## Uncomment the below lines to write the result of the query to a file ##		
//		File file = new File(MessageFormat.format(base_path + "results/{0}.txt", query));
//		
//		if (!file.exists()) {
//			file.createNewFile();
//		}
		
//		FileOutputStream fop = new FileOutputStream(file, true);
//		ResultSetFormatter.out(fop, results);
		
		ResultSetFormatter.out(results);
		System.out.println("Done!");
		input.close();

		
/*		// Open queries.txt and query the database for each of the complex queries
		File queries = new File(base_path + "queries.txt");
		Scanner sc = new Scanner(queries);
		
		// Iterate over each query in the file queries.txt
		while(sc.hasNextLine()) {

			String query = sc.nextLine();
			
			// Instantiate the tdb dataset
			
						
			System.out.println(MessageFormat.format("Querying {0} in the database",query));
			ResultSet results =	tdb.queryTDB(query);
			
			File file = new File(MessageFormat.format(base_path + "results/{0}.txt", query));
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			try (FileOutputStream fop = new FileOutputStream(file, true)) {	
				ResultSetFormatter.out(fop, results);	
				System.out.println("Done");	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		sc.close();
		*/

		
		

	}

}
