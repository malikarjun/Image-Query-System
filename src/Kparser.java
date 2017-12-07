import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import module.graph.ParserHelper;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;



public class Kparser {

	

    Preprocess pp;
    
    ParserHelper ph = new ParserHelper();

//    String parse(String caption) throws Exception {
//    	
//    	String json_output;
//    	
//    	ParserHelper ph = new ParserHelper();
//    	
//    	if(caption.split("-").length > 3)
//    		return null;
//    	
//    	if(caption.split(" ").length > 20) {
//    		String short_caption = "";
//    		
//    		for(int i = 0; i < caption.split(" ").length/4; i++) {
//    			short_caption += (caption.split(" ")[i] + " ");
//    		}
//    		
//    		caption = short_caption;
//    	}
//    	
//    	json_output = ph.getPrettyJsonString(caption, false);
//    	
//	    json_output = "{ \"data\": {},\"children\":" + json_output + "}";
//	    json_output = json_output.replace('-', '_');
//		    
////	    System.out.println(json_output);
//    	
//
//    	return json_output;
//
//    }
    
    
	String parse(String caption) throws Exception {
    	
		String json_output = null;
		
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Task(caption));

        try {
//            System.out.println("Started..");
//            System.out.println(future.get(10, TimeUnit.SECONDS));
            json_output = future.get(20, TimeUnit.SECONDS);
            
    	    json_output = "{ \"data\": {},\"children\":" + json_output + "}";
    	    json_output = json_output.replace('-', '_');
//            System.out.println("Finished!");
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("Terminated!");
        }finally {
            executor.shutdownNow();
//            System.out.println(json_output);
            return json_output;
        }


    }
	
	
class Task implements Callable<String> {
	String caption;
	public Task(String caption) {
		this.caption = caption;
	}
	
    @Override
    public String call() throws Exception {
//	        Thread.sleep(4000); // Just to demo a long running task of 4 seconds.
    	
    	String json = null;
    	json = ph.getPrettyJsonString(caption, false);	
    	return json;

    	
    }
}

//    String parse(String caption) throws Exception {
//    	
//    	String json_output;
//    	try {
//    	driver = new FirefoxDriver();
//	
//	//    	System.setProperty("webdriver.chrome.driver", "/home/pixel/mallikarjun/chromedriver");
//	//    	driver = new ChromeDriver();
//	    	
//	        // And now use this to visit website
//	        driver.get("http://bioai8core.fulton.asu.edu/kparser/");
//	
//	
//	        // Find the text input element by its name
//	        WebElement input = driver.findElement(By.id("sentence_input"));
//	        WebElement button = driver.findElement(By.id("Process"));
//	        
//	        // Enter something to search for
//	        input.sendKeys(caption);    
//	        button.click();
//	
//	        Thread.sleep(5000);
//	        	    
//	        WebElement output = driver.findElement(By.id("json_output"));    	    
//		    json_output = output.getAttribute("value"); 
//		    json_output = "{ \"data\": {},\"children\":" + json_output + "}";
//		    json_output = json_output.replace('-', '_');
//		    
//	//	    System.out.println(json_output);
//    	
//    	}finally {
//    	    driver.close();
//    	    
//    	}
//    	return json_output;
//
//    }

}
