/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.*;
import java.util.List;
import java.util.Scanner;

import module.graph.ParserHelper;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.Model;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.google.gson.Gson;




/** Tutorial 1 creating a simple model
 */


public class Main extends Object {
	boolean running = true;
	
	
    public void terminate() {
        running = false;
    }
    
    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        
        int cnt = 0;
        for (char c : chars) {
        	if(c == '-')
        		cnt++;
        		
            if(! (Character.isLetter(c) || Character.isDigit(c) ||           		
            		 c == '.' || c == '\n' || c == ' ' || c == ',' || 
            		 c == '\'' || c == '/' || c == '-' || c == '&' || c == '"' || c == ':') ) {
                return false;
            }
        }
        
        if(cnt > 3)
        	return false;

        return true;
    }
    
	public void main () throws Exception {

		
		File folder = new File("/home/pixel/mallikarjun/datasets/text4/");
		File[] listOfFiles = folder.listFiles();
		
		TDB tdb = new TDB();
		
// Uncomment the lines below for the implementation of querying code.	
		
//		Scanner input = new Scanner(System.in);
//		int query_db = 0;
//		query_db = input.nextInt();
//		
//		
//		if(query_db == 1) {	
//			
//			File qry = new File("/home/pixel/mallikarjun/datasets/qry.txt");
//			String query = FileUtils.readFileToString(qry, "UTF-8");
//			System.out.println("Querying " + query + " in the database");
//			
//			tdb.queryTDB(query);
////			tdb.checkTDB();
//	
//			input.close();
//			return;
//		}
//		input.close();

		
		PrintWriter writer;
		for(File file:listOfFiles) {
			
			// Check if file has already been added to the database
			File f = new File(Globals.rdf_path + file.getName());
			if(f.exists() && !f.isDirectory())
				continue;
			
			if(!running)
				break;
			
			String caption = FileUtils.readFileToString(file, "UTF-8");
			String url = file.getName();
			
			System.out.println("Adding " + url);
			
			 try {
				System.out.println(caption);
				if(!isAlpha(caption)){
					System.out.println("Not Alpha");
					continue;

				}
					
				
				tdb.addModel(caption, url);
			
			}catch(Exception e) {
				
				System.out.println("Missed this");
				writer = new PrintWriter(new FileWriter("/home/pixel/mallikarjun/datasets/missing_images.txt", true));
				writer.println(url);
				writer.close();
				continue;
				
			}
			// Run garbage collector
			System.gc();
			

	   
		}
		
	
		
//		tdb.checkTDB(3);
		System.out.println("Completed Successfully");
//		tdb.queryTDB("");
		
		

		
	      

    	  
    	  



      }
      
}
