import java.io.*;
import java.util.*;


public class FlieProcessing {
	
	public ArrayList<String> CallStopWordList() {
		String path = ".//res//";
		String fileName = "StopWordList.txt";
	    //System.out.println("Reading File from Java code");
	    fileName = path + fileName;
	    ArrayList<String> RetArrList = new ArrayList<String>();
    	try{
	    	FileReader inputFile = new FileReader(fileName);
	    	BufferedReader bufferReader = new BufferedReader(inputFile);
	    	//Variable to hold the one line data
	    	String line;	
	    	// Read file line by line and print on the console
	    	while ((line = bufferReader.readLine()) != null) {
	    	   //System.out.println(line);
	    	   RetArrList.add(line);
	       }
	       //Close the buffer reader
	       
	       bufferReader.close();
	    }
	    catch(Exception e){
	       System.out.println("Error while reading file line by line:" + e.getMessage());                      
	    }
    	return RetArrList;
	}
	
	public void SavePosting(String line) {
		String path = ".//res//";
		String fileName = "PostingList.txt";
	    //System.out.println("Saving File from Java code");
	    fileName = path + fileName;
	    try{
    		FileWriter fstream1 = new FileWriter(fileName);
		    BufferedWriter out = new BufferedWriter(fstream1);
		    out.write(line);
		    out.close();
	    }
	    catch(Exception e){
	       System.out.println("Error while reading file line by line:" + e.getMessage());                      
	    }
	}
	
	public void SaveTF_IDF(String line) {
		String path = ".//res//";
		String fileName = "TF_IDF.txt";
	    //System.out.println("Saving File from Java code");
	    fileName = path + fileName;
	    try{
    		FileWriter fstream1 = new FileWriter(fileName);
		    BufferedWriter out = new BufferedWriter(fstream1);
		    out.write(line);
		    out.close();
	    }
	    catch(Exception e){
	       System.out.println("Error while reading file line by line:" + e.getMessage());                      
	    }
	}
	
	public ArrayList<PostingListClass> PostingListCall() {
		String path = ".//res//";
		String fileName = "PostingList.txt";
	    //System.out.println("Reading File from Java code");
	    fileName = path + fileName;
	    PostingListClass objPLC; // = new PostingListClass();
		ArrayList<PostingListClass> arrListPL = new ArrayList<PostingListClass>();
		int counter = 0;
	    try {
	    	FileReader inputFile = new FileReader(fileName);
	    	BufferedReader bufferReader = new BufferedReader(inputFile);
	    	//Variable to hold the one line data
		    String line;
		    // Read file line by line and print on the console
		    while ((line = bufferReader.readLine()) != null) {
		    	counter++;
		    	String[] subline = line.split("><"); 
		    	//System.out.println("Words: " + subline[0]);
		    	//System.out.println("Count: " + subline[1]);
		    	//System.out.println("List: " + subline[2]);
		    	//System.out.println("ListCount: " + subline[3]);
			    	
		    	objPLC = new PostingListClass();
		    	objPLC.word = subline[0];
		    	objPLC.count = Integer.parseInt(subline[1]);
		    	String newLine = subline[2].substring(1, subline[2].length() - 1);
		    	String[] words = newLine.split(","); 
			    for (String word : words) {
			    	objPLC.List.add(Integer.parseInt(word.trim()));
			    }
			    String newLineCount = subline[3].substring(1, subline[3].length() - 1);
		    	String[] wordsCount = newLineCount.split(","); 
			    for (String wordCount : wordsCount) {
			    	objPLC.ListCount.add(Integer.parseInt(wordCount.trim()));
			    }
		    	arrListPL.add(objPLC);
		    }
		    //Close the buffer reader
		    bufferReader.close();
	    }
		catch(Exception e) {
			System.out.println("Error while reading file line by line:" + e.getMessage());                      
		}
	    //System.out.println("counter:" + counter);
	    //System.out.println("size:" + arrListPL.size());
	    return arrListPL;
	}
	
	public void PostingListClassPrint(ArrayList<PostingListClass> arrListPL){
		System.out.println("Printing Array");
		//String doc = "";
		for(int i = 0; i < arrListPL.size(); i++){
			PostingListClass objPLCex = new PostingListClass();
			objPLCex = arrListPL.get(i);
			String line = "Word: "+objPLCex.word+", Count: "+objPLCex.count+", List: "+objPLCex.List;
			System.out.println(line);
			//line = objPLCex.word+","+objPLCex.count+","+objPLCex.List;
			//doc = doc + line + "\n";
		}
		System.out.println("Finished Printing Array");
		System.out.println("Posting List size from disk: " + arrListPL.size());
	}
	
	public void call() {
		//String path = "Documents\\Spring 2015\\IR\\";
		String path = ".//res//";
		// The name of the file to open.
	    String fileName = "cran.all.1400.txt";
	    System.out.println("Reading File from Java code");
	    fileName = path + fileName;
	    //Name of the file
	    //String fileName="RAILWAY.txt";
	    try{
	
	       FileReader inputFile = new FileReader(fileName);
	
	       BufferedReader bufferReader = new BufferedReader(inputFile);
	
	       //Variable to hold the one line data
	       String line;
	       String doc = "";
	       Boolean isNewFile = false;
	       
	       // Read file line by line and print on the console
	       while ((line = bufferReader.readLine()) != null)   {
	    	   if (line.contains(".I")){
	    		   isNewFile = true;
	    		   System.out.println("New doc");
	    		   //System.out.println(line);
	    		   System.out.println(doc);
	    		
	    		   
	    		   doc = "";
	    		   continue;
	    	   }
	    	   if (isNewFile){
	    		   //System.out.println(line);
	    		   //System.out.println(doc);
	    		   doc = doc + line;
	    		   //System.out.println(doc);
	    	   }
	    	   //System.out.println(doc);
	    	   //System.out.println(line);
	       }
	       //Close the buffer reader
	       
	       bufferReader.close();
	    }
	    catch(Exception e){
	       System.out.println("Error while reading file line by line:" + e.getMessage());                      
	    }
	}
}
