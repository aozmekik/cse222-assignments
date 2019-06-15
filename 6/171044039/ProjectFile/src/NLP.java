import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * Represents the Natural Language Processing controller class which
 * performs two operations with using Word_Map and File_map model classes:
 * Retrieval of biagrams.
 * TFIDF calculation
 * @see File_Map
 * @see Word_Map
 * @author Ahmed Semih Özmekik
 */
public class NLP
{
    private Word_Map wmap = new Word_Map();
    private int totalNumOfFile = 0;



    /**
     * Reads the dataset from the given directory
     * @param dir is the directory of the dataset.
     */
    public void readDataset(String dir){

        File[] datasetFileList = Objects.requireNonNull(new File(dir).listFiles());
        totalNumOfFile = datasetFileList.length;

        // Put each file to the map.
        for (File datasetFile : datasetFileList) {
            String text = readFileAsString(datasetFile.getAbsolutePath());
            text = cleanStringFromLetters(text);
            putFileIntoMap(text, datasetFile.getName());
        }

    }


    /**
     * Finds all the bi-grams starting with the given word.
     * @param word is starting word of bi-grams.
     * @return is the list of all the bi-grams starting with the given word.
     */
    public List<String> bigrams(String word){

        List<String> biagramList = new ArrayList<>();

        if (wmap.containsKey(word))
            findBiagrams(word, biagramList);

        return biagramList;
    }


    /**
     * Calculates the TFIDF value of the given word for the given file.
     * @param word is the word.
     * @param fileName is the file name.
     * @return TFIDF value of the given word for the given file.
     */
    public double tfIDF(String word, String fileName)
    {
        final double TF =  (double) timesWordAppeardInFile(word, fileName) / totalWordsInFile(fileName);
        final double IDF = Math.log((double) totalNumOfFile / totalFileWordInIt(word));

        return TF*IDF;
    }


    /**
     * Prints the word map.
     */
    public void printWordMap()
    {
        for (Object object: wmap){
            String key = (String) object;
            File_Map value = (File_Map) wmap.get(key);
            System.out.printf("[%s]--->\n", key);
            value.printFileMap();
        }

    }

    /* Returns the given file as a whole string */
    private static String readFileAsString(String file){
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {

            System.out.println("In reading file:" + e.getMessage());
            e.printStackTrace();
        }
        return file; // Exception thrown case, return the input file.
    }

    /* Purges the file string from undesirable letters:
     * New line, unnecessary spaces and punctuations.
     * Returns the cleaned string.
     * */
    private static String cleanStringFromLetters(String file){
        return file.
                replace("\n", " ").  // clean new line.
                replace("\r", ""). // clean carriage return.
                replace("    ", ""). // clean heads.
                trim(). // clean redundant spaces.
                replaceAll("\\p{Punct}", ""); // clean punctuation.
    }

    /* Puts all the words in the text to the map.
     * @param text is cleaned text.
     * @filename is the name of the file containing the text.
    * */
    private void putFileIntoMap(String text, String filename){
        // Split the text into words with regular expression.
        String[] words = text.split("\\s+");

        for (int position=0;position<words.length;++position){
            File_Map file_map;
            String word = words[position];

            if (wmap.containsKey(word)){ // map has the word, just add a new position.
                file_map = (File_Map)wmap.get(word);
                file_map.put(filename, position);
            }
            else{ // map doesn't have the word, insert the word and a new file map.
                file_map = new File_Map();
                file_map.put(filename, position);
                wmap.put(word, file_map);
            }
        }
    }

    /*
     * Inserts all the biagrams found for sourceWord to biagramList.
     * Traverses all the words in the word map and every file map of that word too.
     */
    @SuppressWarnings("unchecked cast")
    private void findBiagrams(String sourceWord, List<String> biagramList) {

        // Get input word's file map.
        File_Map wordFileMap = (File_Map) wmap.get(sourceWord);

        // Traverse the entries in the word's file map.
        for (Map.Entry entryInFileMap : wordFileMap.entrySet()) {

            // Get both key and value from the entry.
            String filename = (String) entryInFileMap.getKey();
            List<Integer> sourcePositionList = (List<Integer>) entryInFileMap.getValue();

            // Traverse all the words in the map.
            for (Map.Entry entryInWordMap : wmap.entrySet()) {
                String targetWord = (String) entryInWordMap.getKey();
                File_Map targetFileMap = (File_Map) entryInWordMap.getValue();

                // Whether target map has a filename the source word is in.
                if (targetFileMap.containsKey(filename)) {
                    List<Integer> targetPositionList =  (List<Integer>)targetFileMap.get(filename);

                    // Check the all positions of source word in the target file.
                    for (Integer pos : sourcePositionList) {
                        if (targetPositionList.contains(pos+1)){
                            String biagram = sourceWord + " " + targetWord;
                            if (!biagramList.contains(biagram))
                                biagramList.add(biagram);
                        }
                    }
                }
            }
        }
    }

    private int timesWordAppeardInFile(String word, String filename){
        File_Map fileMap = (File_Map) wmap.get(word); // Word's file map.

        @SuppressWarnings("unchecked cast")
        List<Integer> positionList = (List<Integer>)fileMap.get(filename);

        return positionList.size();
    }

    private int totalWordsInFile(String file){
        int totalNum = 0;

        for (Object object:wmap){
            File_Map fmap = (File_Map) wmap.get(object);

            if (fmap.containsKey(file)){
                @SuppressWarnings("unchecked cast")
                List<Integer> list = (List<Integer>)(fmap.get(file));
                totalNum += list.size();
            }
        }

        return totalNum;
    }

    private int totalFileWordInIt(String word){
        File_Map fileMap = (File_Map) wmap.get(word); // Word's file map.

        @SuppressWarnings("unchecked cast")
        Set<String> set = (Set<String>) fileMap.keySet();

        return set.size();
    }

}
