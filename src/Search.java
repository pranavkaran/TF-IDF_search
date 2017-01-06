import java.text.DecimalFormat;
import java.util.*;
import java.math.*;
public class Search {
	Scanner in = new Scanner(System.in);
	FlieProcessing objFH = new FlieProcessing();
	ArrayList<String> arrStopWordList = objFH.CallStopWordList();
	Posting objPost = new Posting();
	ArrayList<PostingListClass> arrListPL; // = new ArrayList<PostingListClass>();
	ArrayList<ArrayList> arrPostingList = new ArrayList<ArrayList>();
	
	ArrayList<Double> alIDF = new ArrayList<Double>();
	double[][] arrTF_IDF;
	double[] qryTF_IDF;
	double qryLen = 0;
	ArrayList<Double> alDocLen = new ArrayList<Double>();
	ArrayList<String> alStrCosSim = new ArrayList<String>();
	Map<Double, Integer> mapCosSim = new HashMap<Double, Integer>();
	ArrayList<Integer> alFreqSum = new ArrayList<Integer>();
	
	public void AskUser() {
		arrListPL = objFH.PostingListCall();
		//objFH.PostingListClassPrint(arrListPL);
		CreateTF_IDF();
		//ProcessQueryBoolean("flow");
		System.out.println("Enter yours search query: ");
		String query = in.nextLine();
		ProcessQuery(query);
	}
	public void ProcessQuery(String query) {
		//String[] subQuery = query.split("[\\ \\and]");
		String[] subQuery = query.split(" ");
		qryTF_IDF = new double[arrListPL.size()];
		double maxFreqOfAnyWord = 1;//2055;
		for(int i = 0; i < arrListPL.size(); i++) {
			for (String subquery : subQuery) {
				if(!arrStopWordList.contains(subquery.toLowerCase())) {
					//System.out.println(subquery);
					PostingListClass objPLCex = arrListPL.get(i);
					if(objPLCex.word.toLowerCase().equals(subquery.toLowerCase())){
						// check no of frequency of the query word.
						qryTF_IDF[i] =  (1/maxFreqOfAnyWord) * alIDF.get(i);
						break;
					} else {
						qryTF_IDF[i] =  0;
						//break;
					}
				}
				// end subquery
				if(qryTF_IDF[i] > 0) {
					//System.out.println("i:"+i+", qryTF_IDF[i]: "+qryTF_IDF[i]);
				}
			}
			// for end here
		}
		for(int i = 0; i < qryTF_IDF.length; i++) {
			qryLen += qryTF_IDF[i] * qryTF_IDF[i];
		}
		qryLen = Math.sqrt(qryLen);
		//System.out.println(qryTF_IDF);
		
		// find cosine similarity 
		DecimalFormat df = new DecimalFormat("#.####");
		for(int i = 0; i < 1400; i++) {
			double tempCosSim = 0;
			for(int j = 0; j < arrListPL.size(); j++) {
				//System.out.println(arrTF_IDF[i][j]);
				//System.out.println(qryTF_IDF[j]);
				tempCosSim += (arrTF_IDF[i][j] * qryTF_IDF[j]);
//				if((arrTF_IDF[i][j] * qryTF_IDF[j]) > 0) {
//					System.out.println("i: "+i+",j: "+j);
//					System.out.println("arrTF_IDF[i][j]: "+arrTF_IDF[i][j]);
//					System.out.println("qryTF_IDF[j]: "+qryTF_IDF[j]);
//					System.out.println(tempCosSim);
//					//System.out.println(alDocLen.get(i));
//					//System.out.println(qryLen);
//				}
			}
			if(i == 994 || i == 470 || qryLen <= 0) {
				//System.out.println(i + ":" + mapCosSim.size());
				
			}else {
				//alStrCosSim.add((i+1)+ ":" + df.format(tempCosSim/(alDocLen.get(i)*qryLen)));
				mapCosSim.put(tempCosSim/(alDocLen.get(i)*qryLen), (i+1));
			}
			//alStrCosSim.add((i+1)+ ":" + df.format(tempCosSim/(alDocLen.get(i)*qryLen)));
			//mapCosSim.put(tempCosSim/(alDocLen.get(i)*qryLen), (i+1));
		}
		
		// sort for ranking
		Map<Double, Integer> treeMapCosSim = new TreeMap<Double, Integer>(
				new Comparator<Double>() {
	 
				@Override
				public int compare(Double o1, Double o2) {
					return o2.compareTo(o1);
				}
	 
			});
		treeMapCosSim.putAll(mapCosSim);
		
		// show Cosine Sim
		
//		for(int i = 0; i < alStrCosSim.size(); i++){
//			System.out.println(alStrCosSim.get(i));
//		}
		
		System.out.println("Showing the list:");
		DecimalFormat df1 = new DecimalFormat("#.####");
		int displayCounter = 0;
		for (Map.Entry<Double, Integer> entry : treeMapCosSim.entrySet()) {
			displayCounter++;
			if(displayCounter > 25) {
				break;
			}
			System.out.println(displayCounter + " - cos sim: " + df1.format(Double.parseDouble(entry.getKey().toString())) + ", doc id: " + entry.getValue());
		}
		System.out.println("-- End of program --");
	}
	
	public void CreateIDF(String word, double NoOfDocForEachWord) {
		double NoOfTotalDoc = 1400;
		double logbase2 = Math.log(2);
		double varIdf = Math.log(NoOfTotalDoc/NoOfDocForEachWord)/logbase2;
		alIDF.add(varIdf);
	}
	
	public void CreateTF_IDF(){
		int freqCounter = 0;
		for(int i = 0; i < arrListPL.size(); i++) {
			PostingListClass objPLCex = arrListPL.get(i);
			CreateIDF(objPLCex.word, (double)objPLCex.List.size());
		}
		
		for(int i = 0; i < arrListPL.size(); i++) {
			freqCounter = 0;
			PostingListClass objPLCex = arrListPL.get(i);
			for(int j = 0; j < objPLCex.ListCount.size(); j++) {
				freqCounter += objPLCex.ListCount.get(j);
			}
			alFreqSum.add(freqCounter);
		}
		
		arrTF_IDF = new double[1400][arrListPL.size()];
		for(int i = 0; i < 1400; i++) {
			for(int j = 0; j < arrListPL.size(); j++) {
				PostingListClass objPLCex = arrListPL.get(j);
				int index = MatchPostingList_List(objPLCex, i+1);
    			if (index != -1) {
    				arrTF_IDF[i][j] = (double)objPLCex.ListCount.get(index) * alIDF.get(j); // / alFreqSum.get(j);
    				//arrTF_IDF[i][j] = alIDF.get(j);
    			} else {
    				arrTF_IDF[i][j] = 0;
    			}
			}
		}
		for(int i = 0; i < 1400; i++) {
			double tempsum = 0;
			for(int j = 0; j < arrListPL.size(); j++) {
				//System.out.print(arrTF_IDF[i][j]+ " ");
				//line = line + arrTF_IDF[i][j]+ " ";
				tempsum += arrTF_IDF[i][j] * arrTF_IDF[i][j];
			}
			//System.out.println(Math.sqrt(tempsum));
			alDocLen.add(Math.sqrt(tempsum));
		}
		//System.out.println("End Saving TF_IDF");
	}
	
	public int MatchPostingList_List(PostingListClass objPLCex,int index){
		for(int i = 0; i < objPLCex.List.size(); i++){
			if(objPLCex.List.get(i).equals(index)){
				return i;
			}
		}
		return -1;
	}
	
	// Start Boolean model functions
	
	public int MatchPostingList_Disk(String word){
		for(int i = 0; i< arrListPL.size(); i++){
			PostingListClass objPLCex; // = new PostingListClass();
			objPLCex = arrListPL.get(i);
			if(objPLCex.word.toLowerCase().equals(word.toLowerCase())){
				return i;
			}
		}
		return -1;
	}
	
	public void ProcessQueryBoolean(String query) {
		//String[] subQuery = query.split("[\\ \\and]");
		String[] subQuery = query.split(" ");
		qryTF_IDF  = new double[arrListPL.size()];
		for (String subquery : subQuery) {
			if(!arrStopWordList.contains(subquery.toLowerCase())) {
				System.out.println(subquery);
				
				int index = MatchPostingList_Disk(subquery);
	    		if(index != -1) {
	    			//System.out.println(subquery);
	    			PostingListClass objPLCex = arrListPL.get(index);
	    			System.out.println("size: "+objPLCex.List.size());
	    			
	    		} else {
	    			//System.out.println("Word not found !!");
	    		}
				
				// start of boolean model
				
//	    		int index = MatchPostingList_Disk(subquery);
//	    		if(index != -1) {
//	    			//System.out.println(subquery);
//	    			PostingListClass objPLCex = new PostingListClass();
//	    			objPLCex = arrListPL.get(index);
//	    			//objPLCex.count++;
//	    			
//	    			System.out.println(objPLCex.List);
//	    			System.out.println(objPLCex.ListCount);
//	    			//arrPostingList.add(objPLCex.List);
//	    		}
				
				// end of boolean model
				
			}
		}
		//CreateTF_IDF();
		//MergePostingList();
		// for end here
	}
	
	public void MergePostingList(){
		if (!arrPostingList.isEmpty() & arrPostingList.size() > 1) {
			ArrayList<Integer> outputList = new ArrayList<Integer>();
			while(arrPostingList.size() > 1){
				outputList = MergeTwoList(arrPostingList.get(0),arrPostingList.get(1));
				arrPostingList.remove(0);
				arrPostingList.add(0, outputList);
				arrPostingList.remove(1);
			}
			System.out.println("Search result/s: ");
			System.out.println(outputList);
		}
		else {
			if (arrPostingList.size() == 1) {
				System.out.println("Search result/s: ");
				System.out.println(arrPostingList.get(0));
			}
			else {
				System.out.println("No search result: ");
			}
		}
	}
	
	public ArrayList<Integer> MergeTwoList(ArrayList<Integer> arr1, ArrayList<Integer> arr2){
		ArrayList<Integer> retList = new ArrayList<Integer>();
		for(int i = 0; i < arr1.size(); i++) {
			for(int j = 0; j< arr2.size(); j++) {
				if (arr1.get(i).equals(arr2.get(j))) {
					retList.add(arr1.get(i));
					break;
				}
			}
			
		}
		return retList;
	}
	
	// End Boolean model functions
}
