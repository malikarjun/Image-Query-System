import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;
import com.sun.management.UnixOperatingSystemMXBean;


public class Script {

	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

        
//        Thread someThread = new Thread(new Runnable() {
//        	private volatile boolean running = true;
//        	
//            public void terminate() {
//                running = false;
//            }
//        	
//            public void run() {
//            	try {
//					Main.main(running);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//        });
//        someThread.setDaemon(true);
		IndexProcessor runnable = new IndexProcessor();
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);

		thread.start();
		
		int noOfFilesOpen;
		OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        while(true) {
        	System.out.println("Main running");
            
             noOfFilesOpen = (int) ((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount();

                System.out.println("Number of open fd: " + noOfFilesOpen);
                if(noOfFilesOpen > 4000) {
                	System.out.println("Aborting the Main thread and starting again");
                	runnable.terminate();
                	thread.join();
                	System.out.println(((UnixOperatingSystemMXBean) os).getOpenFileDescriptorCount());
                	
                	thread = new Thread(runnable);
                	thread.setDaemon(true);

                	thread.start();
                	
                }
            
            

            
        	Thread.sleep(600000);
        	System.gc();

        }
        
//		Main.main(args);


	}
	

}

class IndexProcessor implements Runnable {

	private volatile boolean running = true;
	Main driver;
	
    public void terminate() {
//        running = false;
    	driver.terminate();
    }
	
    public void run() {
    	
    	try {
    		driver = new Main();
			driver.main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

