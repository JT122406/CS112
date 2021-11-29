package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null, null, null); 
		//check for nothing
		if(allWords.length == 0)
			return root;

		root.firstChild = new TrieNode(new Indexes(0, (short)(0), (short)(allWords[0].length() -1)), null, null);
		int value = -1;
		
		int index1 = -1, index2 = -1, winIndex = -1;
		TrieNode pointer = root.firstChild;
		TrieNode prev = root.firstChild;
		for(int i = 1; i < allWords.length; i++){
			
			String currentWord = allWords[i];

			while(pointer != null){
				
				index1 = pointer.substr.startIndex;
				index2 = pointer.substr.endIndex;
				winIndex = pointer.substr.wordIndex;
				if(index1 > currentWord.length()){
					prev = pointer;
					pointer = pointer.sibling;
					continue;
				}
				
				value = 0;
				while(value < currentWord.substring(index1).length() && value < allWords[winIndex].substring(index1, index2 +1).length() && currentWord.substring(index1).charAt(value)==allWords[winIndex].substring(index1, index2 +1).charAt(value)){
					value++;
				}
				value--;
				if(value != -1){
					value += index1;
				}
				if(value == -1){
					prev = pointer;
					pointer = pointer.sibling;
				}else{
					if(value == index2){
						prev = pointer;
						pointer = pointer.firstChild;
					}else if(value < index2){
						prev = pointer;
						break;
					}
				}
			}
			if(pointer == null){
				Indexes x = new Indexes(i, (short)index1, (short)(currentWord.length() -1));
				prev.sibling = new TrieNode(x, null, null);
			}else{
				Indexes c = prev.substr;
				TrieNode fc = prev.firstChild;
				Indexes cw = new Indexes(c.wordIndex, (short)(value+1), c.endIndex);
				c.endIndex = (short)value;
				prev.firstChild = new TrieNode(cw, null, null);
				prev.firstChild.firstChild = fc;
				prev.firstChild.sibling = new TrieNode(new Indexes((short)i, (short)(value+1), (short)(currentWord.length() -1)), null, null);
			}
			pointer = prev = root.firstChild;
			value = index1 = index2 = winIndex = -1;
			
		}

		return root;
		
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		if(root == null) return null;

		ArrayList<TrieNode> match = new ArrayList<>();
		TrieNode point = root;
		while(point != null){
			if(point.substr == null)
				point = point.firstChild;
			
			String a = allWords[point.substr.wordIndex];
			String b = a.substring(0, point.substr.endIndex +1);
			if(a.startsWith(prefix) || prefix.startsWith(b)){
				if(point.firstChild != null){
				match.addAll(completionList(point.firstChild, allWords, prefix));
				point = point.sibling;
			}else{
				match.add(point);
				point = point.sibling;
			}
		}else{
			point = point.sibling;
		}

	}

		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return match;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
