package F28DA_CW1;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/** Main class for the Word Index program */
public class WordIndex {

	static final File textFilesFolder = new File("TextFiles");
	static final FileFilter commandFileFilter = (File file) -> file.getParent()==null;
	static final FilenameFilter txtFilenameFilter = (File dir, String filename) -> filename.endsWith(".txt");
	

	public static void main(String[] argv) {
		int numOfWords = 0;
		argv = new String[1];
		argv[0] = "commands.txt";
		if (argv.length != 1 ) {
			System.err.println("Usage: WordIndex commands.txt");
			System.exit(1);
		}
		try{
			File commandFile = new File(argv[0]);
			if (commandFile.getParent()!=null) {
				System.err.println("Use a command file in current directory");
				System.exit(1);
			}

			// creating a command reader from a file
			WordTxtReader commandReader = new WordTxtReader(commandFile);

			// initialise map
			IWordMap wordPossMap = new ListWordMap();
			
			

			// reading the content of the command file
			while(commandReader.hasNextWord()) {
				// getting the next command
				String command = commandReader.nextWord().getWord();

				switch (command) {
				case "addall":
					
					assert(textFilesFolder.isDirectory());
					int count = 0; 
					File[] listOfFiles = textFilesFolder.listFiles(txtFilenameFilter);
					Arrays.sort(listOfFiles);
					for (File textFile : listOfFiles) {
						WordTxtReader wordReader = new WordTxtReader(textFile);

						while (wordReader.hasNextWord()) {
							WordPosition wordPos = wordReader.nextWord();
							// adding word to the map
							wordPossMap.addPos(wordPos.getWord(), wordPos);
							count++;
							numOfWords++;
							
						}
					}
					
					System.out.println(count + " entries have been indexed from " + textFilesFolder.list().length + " files.");
					
					break;

				case "add":
					File textFile = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					WordTxtReader wordReader = new WordTxtReader(textFile);
					// the count
					count = 0; 
					while (wordReader.hasNextWord()) {
						WordPosition word = wordReader.nextWord();
						wordPossMap.addPos(word.getWord(), word);
						count++;
					}
					System.out.println(count + " entries have been indexed from file " + textFile);
					break;

				case "search":
					long startingTime = System.currentTimeMillis();
					int nb = Integer.parseInt(commandReader.nextWord().getWord());
					String word = commandReader.nextWord().getWord();
					// search for word entry in map
					// ...
					try {
						Iterator<IPosition> poss = wordPossMap.positions(word);
						int i = 0;
						while(poss.hasNext()) {
							poss.next();
							i++;
						}
						System.out.println("found " + word + " " + i + " " + "times");
					} catch (WordException e) {
						System.err.println("not found");
					}
					// ...
					long endingTime = System.currentTimeMillis();
					System.out.println("Time taken for search process: " + (endingTime - startingTime));
					break;

				case "remove":
					File textFileToRemove = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					wordReader = new WordTxtReader(textFileToRemove);
					while (wordReader.hasNextWord()) {
						WordPosition wordPos = wordReader.nextWord();
						try
						{
							wordPossMap.removePos(wordPos.getWord(), wordPos);
							
						}
						catch(WordException e)
						{
							
						}
					}
					System.out.println("removed " + textFileToRemove);
					break;
					

				case "overview":
					
					int numPos = 0;
					Iterator<String> wordIterator = wordPossMap.words();
					while( wordIterator.hasNext() )
					{
					
							if ( (wordIterator.next().equals("")) != true) {
								numPos++;
							}
					}
					
						
					System.out.println("Overview:\n" + 
							"      number of words: " + numOfWords + "\n" + 
							"      number of positions: "  + numPos + "\n" + 
							"      number of files: " + textFilesFolder.list().length);
					break;

				default:
					break;
				}

			}

		}
		catch (IOException e){ // catch exceptions caused by file input/output errors
			System.err.println("Check your file name");
			System.exit(1);
		}  
	}
}
