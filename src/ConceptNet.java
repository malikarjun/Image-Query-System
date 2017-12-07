import java.io.*;
import java.util.*;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;


public class ConceptNet {
	
	public static String[] parse(String str) {
		
		StringTokenizer st = new StringTokenizer(str, " \n");

	     String[] ans = new String[100];
	     Arrays.fill(ans, "");
	     
	     int p = 0,s = 0,len = -1;
	     while(st.hasMoreTokens()){
	    	 String token = st.nextToken();
//	    	 System.out.println("Working with " + token + " p=" + p + " s="+s);
	    	 if(token.contains("(VP")) {
	    		 p++;
	    		 len = Math.max(p, len);
	    		 continue;
	    	 }
	    	 
	    	 if(p > 0) {
	    		 if(token.contains("(")) {
	    			s++; 
	    		 }
	    		 else {
	    			
//	    			System.out.println("Added " + token);
    				ans[p] += (token.replace(')', ' ').trim() + " ");
	    			s--;
	    			if(s < 0) {
	    				s = 0;
	    				p--;
	    			}
	    		 }
	    		 
	    	 }
	     }
	     ans[len+1] = "#";
	     return ans;
		
		

	}
	
	public static String simplify(String caption) throws Exception {
		WebDriver driver = new FirefoxDriver();
		driver.get("http://nlp.stanford.edu:8080/parser/index.jsp");
		
		WebElement textbox = driver.findElement(By.id("query"));
		textbox.clear();
		textbox.sendKeys(caption);
		WebElement button = driver.findElement(By.id("parseButton"));
		button.click();
		Thread.sleep(5000);
		WebElement output = driver.findElement(By.className("spacingFree"));
		
		String[] ans = parse(output.getText());
		
		for(String str:ans) {
			System.out.println(str);
		}
		
		
        // Search conceptNet for found phrases
		int i = 1;
		while(!ans[i].contains("#")) {
			
			String phrase = ans[i++].trim().replace(' ', '_');
			
			System.out.println(phrase);
			
			Process cn = Runtime.getRuntime().exec("python conceptNet.py " + phrase);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(cn.getInputStream()));
			String ret = in1.readLine();		
			
			System.out.println(ret);
			String new_query = null;
			if(ret != null) {
				Process snlp = Runtime.getRuntime().exec("python stanfordNLP.py " + caption.replace(' ', '_') + " " + phrase);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(snlp.getInputStream()));
				if(in2.readLine().contains("left"))
//					return (ret + " " +  in2.readLine()) ;
					new_query = (ret + " " +  in2.readLine());
				else
//					return ( in2.readLine() + " " + ret) ;
					new_query = ( in2.readLine() + " " + ret);
				System.out.println(new_query);
				return new_query;
			}
		
		}
		
		return null;
	
	}

}
