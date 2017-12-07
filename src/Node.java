import java.io.*;
import java.util.ArrayList;


public class Node implements Serializable {

    public Data data = null;
    public ArrayList<Node> children = null;
    
    public static Object deepClone(Object object) {
    	   try {
    	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	     ObjectOutputStream oos = new ObjectOutputStream(baos);
    	     oos.writeObject(object);
    	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    	     ObjectInputStream ois = new ObjectInputStream(bais);
    	     return ois.readObject();
    	   }
    	   catch (Exception e) {
    	     e.printStackTrace();
    	     return null;
    	   }
    	 }
    
    public void print() {
    	if(this.data == null)
    		return;
    	
    	System.out.println(data.toString());
    	for(int i = 0; i < children.size(); i++)
    		children.get(i).print();
    }
    
}
