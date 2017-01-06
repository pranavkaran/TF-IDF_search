import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


public class FileSpliting {
	public void split(){
		String path = ".//res//";
		// The name of the file to open.
	    String fileName = "cran.all.1400.txt";
	    System.out.println("Spliting start");
	    fileName = path + fileName;
		// Actual splitting of file into smaller files  

	    try {
			FileInputStream fstream = new FileInputStream(fileName); 
			DataInputStream in = new DataInputStream(fstream);  
	
			BufferedReader br = new BufferedReader(new InputStreamReader(in)); 
			String strLine;  
			String doc = "";
		    Boolean isNewFile = false;
		    FileWriter fstream1;
		    BufferedWriter out = null;
		    String OutFileName = "";
		    while ((strLine = br.readLine()) != null)   {
		    	if (strLine.contains(".I")){
		    		isNewFile = true;
		    		OutFileName = ".//res//Documents//"+strLine.substring(3)+".txt";
		    		doc = ""; 
		    		continue;
		    	}
		    	if (isNewFile){
		    		   doc = doc + strLine + "\n";
		    	   }
		    	fstream1 = new FileWriter(OutFileName);     
			    out = new BufferedWriter(fstream1);
			    out.write(doc);
			    out.close();
			    
		    }
		    in.close();
		    System.out.println("Spliting done");
	    }
	    catch (Exception e)  
	    {  
	     System.err.println("Error: " + e.getMessage());  
	    } 
	}
	
}
