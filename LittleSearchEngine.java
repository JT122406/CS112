package lse;

import java.io.*;
import java.util.*;


/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		if(docFile == null)
			throw new FileNotFoundException("File not found on disk");
		File myObj = new File(docFile);	
		HashMap<String,Occurrence>Keywords = new HashMap<String,Occurrence>();
		Scanner scanner = new Scanner(myObj);
		
		while(scanner.hasNext()){
			String word = scanner.next();
			word = getKeyword(word);
			if(word == null){
				continue;
			}else if(Keywords.containsKey(word) == false){
				Occurrence occurs = new Occurrence(docFile, 1);
				Keywords.put(word, occurs);
			}else{
				Occurrence occurs = Keywords.get(word);
				occurs.frequency++;
				Keywords.put(word, occurs);
			}
		}
		scanner.close();
		
		return Keywords;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for(String x:kws.keySet()){
			if(keywordsIndex.containsKey(x) == false){
				ArrayList<Occurrence> occ = new ArrayList<Occurrence>();
				Occurrence recent = kws.get(x);
				occ.add(recent);
				insertLastOccurrence(occ);
				keywordsIndex.put(x, occ);
			}else{
				ArrayList<Occurrence> occ = keywordsIndex.get(x);
				Occurrence recent = kws.get(x);
				occ.add(recent);
				insertLastOccurrence(occ);
				keywordsIndex.put(x, occ);
			}
		}
	}
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		word = word.trim();
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++){
			char comp = word.charAt(word.length()-1);
		if((comp == '.') ||(comp == ',')|| (comp == '?')||(comp == ':') ||(comp == ';') ||(comp == '!')){
			word = word.substring(0, word.length() - 1);
		}
		else
		break;
		}
		for(int i = 0; i < word.length(); i++){
			char comp = word.charAt(i);
			if(!Character.isLetter(comp))
				return null;
			else if(noiseWords.contains(word))
				return null;
		}
		
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		if(occs.size() == 1)
			return null;

		ArrayList<Integer> frequencythingy = new ArrayList<Integer>();
		Occurrence hold = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);
	
		//search
		int hi = 0 ,low = occs.size()-1, mid = 0, middle;
		while(hi<=low){
			mid = (hi+low)/2;
			middle = occs.get(mid).frequency;
			if(middle == hold.frequency){
				frequencythingy.add(mid);
				break;
			}
			if(middle < hold.frequency){
				low = mid-1;
				frequencythingy.add(mid);
			}
			if(middle > hold.frequency){
				hi = mid + 1;
				frequencythingy.add(mid);
				mid++;
			}
		}
		occs.add(mid, hold);
		return frequencythingy;
	}
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		ArrayList<String> Docs = new ArrayList<String>(5);
		//both kw1 and kw2 exist
		 int i = 0, j = 0, count = 0;
		 ArrayList<Occurrence> k1 = keywordsIndex.get(kw1);
		 ArrayList<Occurrence> k2 = keywordsIndex.get(kw2);
		 if(keywordsIndex.containsKey(kw1)==true && keywordsIndex.containsKey(kw2) == true)
		 {
			while(i < k1.size() && j < k2.size())
			{
				if(count > 5)
					break;

					if(k1.get(i).frequency > k2.get(j).frequency)
					{	if(!Docs.contains(k1.get(i).document)){
						Docs.add(k1.get(i).document);
						
					}
						count++;
						i++;
						continue;
					}
					else if(k1.get(i).frequency == k2.get(j).frequency)
					{
						if(!Docs.contains(k1.get(i).document)){
							Docs.add(k1.get(i).document);
							
						}
					
						i++;
						count++;
						j++;
						continue;
					}
					else
					{
						if(!Docs.contains(k1.get(j).document)){
							Docs.add(k1.get(j).document);
							
						}
					
						count++;
						j++;
					}
			}
			//check if list is empty
			if(i < k1.size())
			{
				for(int z = i; z <k1.size() && count!= 5; z++)
				{
					if(!Docs.contains(k1.get(i).document)){
						Docs.add(k1.get(i).document);
						
					}
					
					count++;
				}
			}
			else if(j < k1.size())
			{
				for(int z = j; z < k1.size() && count!= 5;z++)
				{	if(!Docs.contains(k1.get(j).document)){
					Docs.add(k1.get(j).document);
					
				}
					count++;
				}
				
			}
		 }//if only kw2 exists
		 else if(keywordsIndex.containsKey(kw1) == false && keywordsIndex.containsKey(kw2)==true)
		 {
			 for(int z = 0 ; z < 5 && count!= 5 && z < k2.size();z++){
				if(!Docs.contains(k2.get(z).document)){
					Docs.add(k2.get(z).document);
					
				}
				
				 count++;
				 
			 }
		 }//if kw1 exists
			else if(keywordsIndex.containsKey(kw1) == true && keywordsIndex.containsKey(kw2)==false)
			{
				for(int z = 0; z < 5  && count!= 5 && z < k1.size();z++)
				{
					if(!Docs.contains(k1.get(z).document)){
						Docs.add(k1.get(z).document);
						
					}
					
					count++;
				}
			}
			else 
				return null;

			Docs.trimToSize();
			int[] occ = new int[Docs.size()];
			for(int one = 0;one < Docs.size(); one++){
				for(int two = 0; two < Docs.size(); two++){
					for(int three = 0; three < Docs.size(); three++){
				if((Docs.get(two) == keywordsIndex.get(kw1).get(one).document) && (Docs.get(two) == keywordsIndex.get(kw2).get(three).document)){
					
					occ[two] = keywordsIndex.get(kw1).get(one).frequency;
					occ[two] += keywordsIndex.get(kw2).get(three).frequency;
					break;
				}
				}
			}
			}
			for(int one = 0; one < occ.length; one++){
				for(int two = 0; two < occ.length; two++){
					if(one == two)
					continue;
					else if((occ[one] < occ[two]) && (one < two)){
						String temp = Docs.get(one);
						String temp2 = Docs.get(two);
						Docs.set(one, temp2);
						Docs.set(two, temp);
						int temp1 = occ[one];
						occ[one] = occ[two];
						occ[two] = temp1;
						one = 0;
						two = 0;
					}
				}
			}
			
	 return Docs;
	}
}