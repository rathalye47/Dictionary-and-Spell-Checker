package cs146F20.athalye.project4;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class RBTTester {

	@Test
	// Tests the Red Black Tree.
	public void test() {
		RedBlackTree<String> rbt = new RedBlackTree<String>();
		rbt.insert("D"); 
		rbt.insert("B");
		rbt.insert("A");
		rbt.insert("C");
		rbt.insert("F");
		rbt.insert("E");
		rbt.insert("H");
		rbt.insert("G");
		rbt.insert("I");
		rbt.insert("J");

		assertEquals("DBACFEHGIJ", makeString(rbt));

		String str=     "Color: 1, Key:D Parent: \n"+
				"Color: 1, Key:B Parent: D\n"+
				"Color: 1, Key:A Parent: B\n"+
				"Color: 1, Key:C Parent: B\n"+
				"Color: 1, Key:F Parent: D\n"+
				"Color: 1, Key:E Parent: F\n"+
				"Color: 0, Key:H Parent: F\n"+
				"Color: 1, Key:G Parent: H\n"+
				"Color: 1, Key:I Parent: H\n"+
				"Color: 0, Key:J Parent: I\n";

		assertEquals(str, makeStringDetails(rbt));
	}

	// Inserts all the words from the dictionary into a Red Black Tree.
	// Calls lookups to the dictionary to see if a word exists in the poem.
	// Calculates the time to create the dictionary and the time for spell checking.
	@Test
	public void checkSpelling() throws IOException
	{
		RedBlackTree<String> dictionary = new RedBlackTree<String>(); // Red Black Tree dictionary.

		long dictionaryStartTime = System.nanoTime(); // Starting time to create the dictionary.

		createDictionary(dictionary); // Creates the dictionary.

		long dictionaryEndTime = System.nanoTime(); // Ending time to create the dictionary.
		long dictionaryTotalTime = dictionaryEndTime - dictionaryStartTime; // Total time to create the dictionary.
		System.out.println("Time to create the dictionary: " + dictionaryTotalTime + " nanoseconds.");

		ArrayList<String> poemWords = new ArrayList<>(); // ArrayList to hold all the words of the poem.
		readPoem(poemWords); // Reads each line from the poem and stores every word in the poemWords ArrayList.

		assertEquals(144, poemWords.size()); // Verifies that there are 144 words in the poem.

		ArrayList<String> missingWords = new ArrayList<>(); // ArrayList to hold all the words from the poem that are not found in the dictionary.

		long lookupStartTime = System.nanoTime(); // Starting time for spell checking.

		addMissingWords(missingWords, dictionary, poemWords); // Finds all the missing words from the poem.

		long lookupEndTime = System.nanoTime(); // Ending time for spell checking.
		long lookupTotalTime = lookupEndTime - lookupStartTime; // Total time for spell checking.
		System.out.println("Time for spell checking: " + lookupTotalTime + " nanoseconds.");

		assertEquals(69, missingWords.size()); // Verifies that there are 69 words from the poem not found in the dictionary.

		printMissingWords(missingWords); // Prints out the number of missing words and every word from the poem not found in the dictionary (separated by a comma).
	}

	// Goes through each word in the poem, and if the word is not found in the dictionary, add it to the missingWords ArrayList.
	public void addMissingWords(ArrayList<String> missingWords, RedBlackTree<String> dictionary, ArrayList<String> poemWords)
	{
		for (String word : poemWords)
		{
			if (dictionary.lookup(word) == null)
			{
				missingWords.add(word);
			}
		}
	}

	// Prints out all words from the poem that were not found in the dictionary as well as how many such words there were.
	public void printMissingWords(ArrayList<String> missingWords)
	{
		// If the missingWords ArrayList is empty, then every word from the poem was found in the dictionary.
		if (missingWords.size() == 0)
		{
			System.out.println("Every word from the poem was found in the dictionary.");
		}

		// Prints all the words from the poem not found in the dictionary.
		// Each word is separated by a comma.
		else
		{
			int count = 0;
			System.out.print("There are " + missingWords.size() + " words from the poem not found in the dictionary: ");

			for (String missingWord : missingWords)
			{
				if (count == missingWords.size() - 1)
				{
					System.out.print(missingWord + ".");
				}

				else
				{
					System.out.print(missingWord + ", ");
				}

				count++;
			}
		}
	}

	// Reads each line from the poem and adds each word from the poem into an ArrayList.
	public void readPoem(ArrayList<String> poemWords) throws IOException
	{
		FileReader poemFR = new FileReader("FavoritePoem.txt"); // Poem file reader.
		BufferedReader poemBR = new BufferedReader(poemFR); // Poem buffered reader.

		String[] poemLines; // Array to hold the words of each line in the poem.
		String line = poemBR.readLine(); // Reads the first line of the poem.

		while (line != null) // While the line is not null.
		{
			String updatedLine = line.trim(); // Removes any leading and trailing spaces from the line.
			poemLines = updatedLine.split("\\s+"); // Splits each word in the line by spaces.

			for (int index = 0; index < poemLines.length; index++)
			{
				// Removes any character that is not in between a-z or A-Z and converts every letter to lowercase.
				// Adds each word in the line to the poemWords ArrayList.
				poemLines[index] = poemLines[index].replaceAll("[^a-zA-Z]", "").toLowerCase();
				poemWords.add(poemLines[index]);
			}

			line = poemBR.readLine(); // Reads the next line of the poem.
		}

		poemBR.close(); // Closes the poem buffered reader.
	}

	// Creates the dictionary.
	// Reads each line from the dictionary and inserts each word into a Red Black Tree.
	public void createDictionary(RedBlackTree<String> dictionary) throws IOException
	{
		FileReader dictionaryFR = new FileReader("Dictionary.txt"); // Dictionary file reader.
		BufferedReader dictionaryBR = new BufferedReader(dictionaryFR); // Dictionary buffered reader.

		String dictionaryLine = dictionaryBR.readLine(); // Reads the first line of the dictionary.

		while (dictionaryLine != null) // While the line is not null.
		{
			dictionaryLine = dictionaryLine.trim(); // Removes leading and trailing spaces from the line.
			dictionary.insert(dictionaryLine); // Inserts each word from the dictionary into a Red Black Tree.
			dictionaryLine = dictionaryBR.readLine(); // Reads the next line of the dictionary.
		}

		dictionaryBR.close(); // Closes the dictionary buffered reader.
	}

	public static String makeString(RedBlackTree t)
	{
		class MyVisitor implements RedBlackTree.Visitor {
			String result = "";

			public void visit(RedBlackTree.Node n)
			{
				result = result + n.key;
			}
		};

		MyVisitor v = new MyVisitor();
		t.preOrderVisit(v);
		return v.result;
	}

	public static String makeStringDetails(RedBlackTree t) {
		{
			class MyVisitor implements RedBlackTree.Visitor {
				String result = "";

				public void visit(RedBlackTree.Node n)
				{
					if (n.parent == null){
						result = result + "Color: " + n.color + ", Key:" + n.key + " Parent: \n";
					}

					else if (!(n.key).equals(""))
						result = result +"Color: "+n.color+", Key:"+n.key+" Parent: "+n.parent.key+"\n";

				}
			};

			MyVisitor v = new MyVisitor();
			t.preOrderVisit(v);
			return v.result;
		}
	}
}