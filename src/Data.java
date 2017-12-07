import java.io.Serializable;


public class Data implements Serializable {
    public Boolean isClass =  false;
    public Boolean isACoreferent = false;
    public String pos = null;
    public Boolean isASemanticRole = false;
    public Boolean isEntity = false;
    public String wordSense = null;
    public String word = null;
    public String Edge = null;
    public Boolean isEvent =  false;
    
    public String toString() {
        return "isClass : " +  isClass.toString() + "\n" +
        		"isACoreferent : " + isACoreferent.toString() + "\n" +
        		"pos : " + pos + "\n" +
        		"isASemanticRole : " + isASemanticRole.toString() + "\n" +
        		"isEntity : " + isEntity.toString() + "\n" +
        		"wordSense : " + wordSense + "\n" +
        		"word : " + word + "\n" +
        		"Edge : " + Edge + "\n" +
        		"isEvent : " + isEvent.toString() + "\n";
        		
        	
    }
}
