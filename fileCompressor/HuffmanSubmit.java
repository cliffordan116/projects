//@author Clifford An

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Import any package as required

public class HuffmanSubmit implements Huffman {

	// Feel free to add more methods and variables as required.

	public class HashTable {

		Character keys[];
		int freq[];
		int size;
		String binrep[];	//stores binary representation of each character

		public HashTable() {
			keys = new Character[300];
			freq = new int[300];
			binrep = new String[300];
		}

		public void put(Character c) {
			if (keys[c.hashCode()] == null) {
				size++;
				keys[c.hashCode()] = c;
			}
			freq[c.hashCode()]++;
		}

		public void putChar(Character c) {
			keys[c.hashCode()] = c;
		}

		public void putint(Character c, int i) {
			freq[c.hashCode()] = i;
		}

	}

	public class Node {

		int freq;
		Character character;
		Node left;
		Node right;

		public Node(int freq, Character character) {
			this.freq = freq;
			this.character = character;
		}

	}

	public String binaryPath(Node root, Character c, String recurs) {	//finds the shortened binary representation of a character using the huffman tree
		if (root.character != null && root.character.equals(c)) {
			return recurs;
		} else if (root.character != null) {
			return null;
		}
		String left = binaryPath(root.left, c, recurs + "0");
		if (left != null) {
			return left;
		}
		String right = binaryPath(root.right, c, recurs + "1");
		if (right != null) {
			return right;
		}
		return null;
	}

	public static Comparator<Node> nodeFreq = new Comparator<>() { // https://www.geeksforgeeks.org/how-to-sort-an-arraylist-of-objects-by-property-in-java/

		public int compare(Node n1, Node n2) {
			int n1freq = n1.freq;
			int n2freq = n2.freq;

			return n1freq - n2freq;
		}
	};

	public void encode(String inputFile, String outputFile, String freqFile) {
		// TODO: Your code here
		BinaryIn in = new BinaryIn(inputFile);
		HashTable hash = new HashTable();
		while (!in.isEmpty()) {
			hash.put(in.readChar());	//hashes all characters into hash table and increments freqency for each character added
		}
		Character chars[] = hash.keys;
		int[] freqs = hash.freq;
		ArrayList<Node> mergearry = new ArrayList<>();
		try {
			FileWriter test = new FileWriter(freqFile);
			BufferedWriter freqtest = new BufferedWriter(test);
			for (int i = 0; i < chars.length; i++) { // creates frequency file and node array for merging
				if (chars[i] != null) {
					freqtest.write(Integer.toString(chars[i]) + ":" + freqs[i]);	//writes ascii number for each character
					freqtest.newLine();
					Node newNode = new Node(freqs[i], chars[i]);
					mergearry.add(newNode);
				}
			}
			freqtest.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (hash.size != 1) { // merges nodes in node array until only one node is left standing
			Collections.sort(mergearry, nodeFreq);
			Node n1 = mergearry.get(0);
			Node n2 = mergearry.get(1);
			Node newNode = new Node(n1.freq + n2.freq, null);
			newNode.right = n2;
			newNode.left = n1;
			mergearry.remove(0);
			mergearry.remove(0);
			mergearry.add(0, newNode);
			hash.size--;
		}
		BinaryIn input = new BinaryIn(inputFile);
		BinaryOut output = new BinaryOut(outputFile);
		for (Character c : hash.keys) {
			if (c != null) {
				hash.binrep[c.hashCode()] = binaryPath(mergearry.get(0), c, "");	//stores binary representation of each character in hash table
			}
		}
		while (!input.isEmpty()) {	//alternatively, i can traverse the tree for every character to find the binary representation, but this is much more efficient
			Character c = input.readChar();
			String str = hash.binrep[c.hashCode()];
			for (int i = 0; i < str.length(); i++) {
				if (str.substring(i, i + 1).equals("1")) {
					output.write(true);	//writes binary representation in boolean rather than string
				} else {
					output.write(false);
				}
			}
		}
		output.close();
	}

	public Node constructTree(String freqFile) {	//method for constructing huffman tree using the frequency file
		FileReader test;
		HashTable hash = new HashTable();
		try {
			test = new FileReader(freqFile);
			BufferedReader test2 = new BufferedReader(test);
			try {
				while (test2.ready()) {
					String str = test2.readLine();
					if (!str.equals("")) {
						String[] strarry = str.split(":");	//reads integer behind the colon as the character and the integer after as frequency
						Character c = (char) Integer.parseInt(strarry[0]);
						int freq = Integer.parseInt(strarry[1]);
						hash.putChar(c);	//creates the identical hash table used in encode method
						hash.putint(c, freq);
					}
				}
				ArrayList<Node> mergearry = new ArrayList<>();
				for (Character c : hash.keys) {
					if (c != null) mergearry.add(new Node(hash.freq[c.hashCode()], c));
				}
				while (mergearry.size() != 1) { // merges nodes in node array until only one node is left standing
					Collections.sort(mergearry, nodeFreq);
					Node n1 = mergearry.get(0);
					Node n2 = mergearry.get(1);
					Node newNode = new Node(n1.freq + n2.freq, null);
					newNode.right = n2;
					newNode.left = n1;
					mergearry.remove(0);
					mergearry.remove(0);
					mergearry.add(0, newNode);
				}
				test2.close();
				return mergearry.get(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void decode(String inputFile, String outputFile, String freqFile) {
		// TODO: Your code here
		Node root = constructTree(freqFile);	//construct huffman tree using frequency file
		BinaryIn input = new BinaryIn(inputFile);
		BinaryOut output = new BinaryOut(outputFile);
		while (!input.isEmpty()) {
			Node curNode = root;
			while (curNode.character == null) {	//traverses tree for every boolean read
				if (input.isEmpty()) break;
				Boolean inb = input.readBoolean();
				if (inb == true) {	//if boolean is true, turn right
					curNode = curNode.right;
				} else {			//if false, turn left
					curNode = curNode.left;
				}
			}
			if (curNode.character != null) output.write(curNode.character);
			output.flush();
		}
		output.close();

	}

	public static void main(String[] args) {
		Huffman huffman = new HuffmanSubmit();
		// "alice.txt"
		huffman.encode(args[0], "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.txt", "freq.txt");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same.
		// On linux and mac, you can use `diff' command to check if they are the same.
	}

}
