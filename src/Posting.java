import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Posting {
	ArrayList<String> arrList = new ArrayList<String>();
	PostingListClass objPLC; // = new PostingListClass();
	ArrayList<PostingListClass> arrListPL = new ArrayList<PostingListClass>();
	
	FlieProcessing objFH = new FlieProcessing();
	ArrayList<String> arrStopWordList = objFH.CallStopWordList();
	int counter = 0;
	public void Filecall() {
		
		for(int i = 1; i<= 1400; i++) {
			try {
				FileReader inputFile = new FileReader(".//res//Documents//"+ i +".txt");
				BufferedReader bufferReader = new BufferedReader(inputFile);
				String line;
				
			    while ((line = bufferReader.readLine()) != null) {
			    	String[] words = line.split("[\\ \\.\\!\\?\\,\\(\\)]"); 
			    	for (String word : words) {
			    		if(!word.equals("")) {
			    			counter++;
				    		if(!arrStopWordList.contains(word.toLowerCase())) {
					    		int index = MatchPostingList(word);
					    		if(index != -1){
					    			PostingListClass objPLCex = new PostingListClass();
					    			objPLCex = arrListPL.get(index);
					    			objPLCex.count++;
					    			if (objPLCex.List.contains(i)) {
					    				int tempIndex = objPLCex.List.indexOf(i);
					    				int tempValue = objPLCex.ListCount.get(tempIndex);
					    				tempValue++;
					    				objPLCex.ListCount.remove(tempIndex);
					    				objPLCex.ListCount.add(tempIndex, tempValue);
					    			} else {
					    				objPLCex.List.add(i);
					    				objPLCex.ListCount.add(1);	
					    			}
					    		}
					    		else {
					    			objPLC = new PostingListClass();
						    		objPLC.word = word;
						    		objPLC.count++;
						    		objPLC.List.add(i);
						    		objPLC.ListCount.add(1);
						    		arrListPL.add(objPLC);
					    		}
					    		//System.out.println(word + "\n"); 
				    		}
			    		}
			    	}
			    }
			    bufferReader.close();
			    System.out.println("Finished adding words Array "+i);
			}
			catch(Exception e) {
				System.out.println("Error while reading file line by line:" + e.getMessage());                      
			}
		}
		
		
	}
	
	public void ArrayListPrint(){
		System.out.println("Printing Array");
		for(int i=0; i< arrList.size(); i++){
			System.out.println(arrList.get(i));
		}
		System.out.println("Finished Printing Array");
		System.out.println(arrList.size());
	}
		
	public void ArrayListPrint_Save(){
		System.out.println("Printing Array");
		String doc = "";
		for(int i=0; i< arrListPL.size(); i++){
			PostingListClass objPLCex = new PostingListClass();
			objPLCex = arrListPL.get(i);
			String line = "Word: "+objPLCex.word+", Count: "+objPLCex.count+ ", List: "+objPLCex.List;
			String line1 = "ListCount: "+ objPLCex.ListCount;
			System.out.println(line);
			System.out.println(line1);
			line = objPLCex.word+"><"+objPLCex.count+"><"+objPLCex.List+"><"+objPLCex.ListCount;
			doc = doc + line + "\n";
		}
		objFH.SavePosting(doc);
		System.out.println("Finished Printing Array");
		System.out.println(arrListPL.size());
		System.out.println("How many words are there in the collection: "+counter);
	}
	
	public int MatchPostingList(String word){
		for(int i = 0; i< arrListPL.size(); i++){
			PostingListClass objPLCex = new PostingListClass();
			objPLCex = arrListPL.get(i);
			if(objPLCex.word.toLowerCase().equals(word.toLowerCase())){
				return i;
			}
		}
		return -1;
	}
}
