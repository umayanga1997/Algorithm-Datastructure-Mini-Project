import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MiniProject {
    public static void main(String[] args) {

        LinkedList<Hashtable<Integer, String>> wordList = generateWordList();

        // Print Words (Task 01 -> (a))
        System.out.println(wordList);

        // Store Words in wordsHK6.txt file (Task 01 -> (b))
        writeHKFile(wordList);

        // Generate New List with New HashKey(s) (Task 02 -> (b))
        ArrayList<Hashtable<Integer, String>> newWordList =  generateNewWordList(wordList);

        // Store Words in wordsQHK6.txt file (Task 02 -> (c))
        writeQHKFile(wordList, newWordList);

        // Non-collision hash table using Quadratic probing (Task 03 -> (a))
        quaProbTable(newWordList);
    }

    private static LinkedList<Hashtable<Integer, String>> generateWordList (){
        // For read a file
        BufferedReader reader = null;
        try {
            // For create Word List
            LinkedList<Hashtable<Integer, String>> wordList = new LinkedList<>();

            // Read a File
            reader = new BufferedReader(new FileReader("src/file6.txt"));
            List<String> words = new ArrayList<>();

            // Read words from File
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineWords = line.split("[\\d\\s,.:;?!()\\[\\]{}'\"]+");
                words.addAll(List.of(lineWords));
            }

            // Set Word by Word to the wordList using Hash Map
            for (String word : words) {
                // Map key
                int key = generateHashKey(word);
                // Create a Map for Data Mapping
                Hashtable<Integer, String> map = new Hashtable<>();
                map.put(key, word);
                // Add data to the Word List
                if(!wordList.contains(map))
                    wordList.add(map);
            }

            return wordList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private static ArrayList<Hashtable<Integer, String>> generateNewWordList(LinkedList<Hashtable<Integer, String>> wordList) {
        ArrayList<Hashtable<Integer, String>> list = new ArrayList<>();
        // Count Indexes
        int index =0;
        // Looping words List
        for (Hashtable<Integer, String> word : wordList){
            // Get only word
            String w = word.entrySet().iterator().next().getValue();
            // Generate new Hash Key with h(j, i)
            int qkj = generateNewHashKey(w, index);
            // Mapping new Key with pre Value
            Hashtable<Integer, String> map = new Hashtable<>();
            map.put(qkj, w);
            // Add mapped data to the newWordList
            list.add(map);
            index++;
        }

        return list;
    }

    private static void quaProbTable(ArrayList<Hashtable<Integer, String>> newWordList){

        LinkedList<Hashtable<Integer, String>> quProbList = new LinkedList<>();

        quProbList.addAll(initList(119)); // Make size of Array List

        int quIndex =0;
        // Looping Word List
        for (Hashtable<Integer, String> mapData : newWordList){
            // Get Word only
            String word = mapData.entrySet().iterator().next().getValue();
            // Create a new Index/Key
            int key = (generateHashKey(word) + (quIndex * quIndex)) % 119; // (K + i2) % 119
            // Mapping Data
            Hashtable map = new Hashtable();
            map.put(key,  mapData.entrySet().iterator().next().getValue());
            // Set map to the List
            quProbList.set(key, map);
            // Count index
            quIndex++;
        }
        // Print QU List
        System.out.println("------------------------------------");
        System.out.println("Index\t\t|\tWords in file6.txt");
        System.out.println("------------------------------------");
        int inx =0;
        for (Hashtable<Integer, String> mapData : quProbList){
            if(mapData != null)
                System.out.println(inx+"\t\t\t\t"+ mapData.entrySet().iterator().next().getValue());
            else
                System.out.println(inx);
            inx++;
        }
    }

    private static void writeHKFile(LinkedList<Hashtable<Integer, String>> wordList){
        try {
            // Init File
            FileWriter writer = new FileWriter("src/wordsHK6.txt");
            // Clean All data in file
            writer.flush();
            // Write First Line
            writer.write("------------------------------------------------------\n");
            writer.write("Word Index j\t|\tWord\t\t\t|\tHash Key, Kj\n");
            writer.write("------------------------------------------------------\n");

            // Looping list data and write data to the file
            for (Hashtable<Integer, String> mapData : wordList){
                // Word
                String word = mapData.entrySet().iterator().next().getValue();
                // Key
                String key = mapData.entrySet().iterator().next().getKey().toString();
                writer.write(wordList.indexOf(mapData) + "\t\t\t\t\t"+word+ manageSpaces(word, 20)+key+"\n");
            }

            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void writeQHKFile(LinkedList<Hashtable<Integer, String>> wordList,
                                     ArrayList<Hashtable<Integer, String>> newWordList){
        try {
            // Init File
            FileWriter writer = new FileWriter("src/wordsQHK6.txt");
            // Clean All data in file
            writer.flush();
            // Header
            String header = "new Hash key, Qkj\t\tQuadratic h-f, h(j, i)";
            // Write First Line
            writer.write("----------------------------------------------------------------------------------------------------------\n");
            writer.write("Word Index j\t|\tWord\t\t\t|\tHash Key, Kj\t|\tnew Hash key, Qkj\t|\tQuadratic h-f, h(j, i)\n");
            writer.write("----------------------------------------------------------------------------------------------------------\n");

            // Looping list data and write data to the file
            for (Hashtable<Integer, String> mapData : wordList){
                int index= wordList.indexOf(mapData);
                // Word
                String word = mapData.entrySet().iterator().next().getValue();
                // Key
                String key = mapData.entrySet().iterator().next().getKey().toString();
                // Get new Hash Key for related value(mapData) from newWordList -> qkj
                String qkj =  newWordList.get(index).entrySet().iterator().next().getKey().toString();
                // j = word | i = index -> Quadratic -> Quadratic == qkj
                String hji =  qkj;

                writer.write(index + "\t\t\t\t\t" + word + manageSpaces(word, 20)+key +
                        manageSpaces(key, 20)  + qkj +manageSpaces(qkj, 24 )+ hji+"\n");
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static int generateNewHashKey(String jWord, int iIndex){
        // Constants
        int c1 = 1;
        int c2 = 1;
        int c3 = 0;

        // Generate key
        int key = generateHashKey(jWord);

        // (Kj + c1i*2 + c2i + c3) Return modified new key
        return (key + (c1*(iIndex*iIndex)) +(c2*iIndex) + c3 ) % 119;
    }

    // For Map Key
    private static int generateHashKey(String word)
    {
        int key = 0;
        // Looping char of word for Making a key
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = alphabetList().indexOf(String.valueOf(c));
            // Kj
            key += index;
        }
        return key;
    }

    private static List<String> alphabetList(){
        // Alphabet List
        ArrayList<String> list = new ArrayList<>();

        // Find Lowercase Latter
        for (char c = 'a'; c <= 'z'; c++) {
            list.add(Character.toString(c));
        }
        // Find Uppercase Latter
        for (char c = 'A'; c <= 'Z'; c++){
            list.add(Character.toString(c));
        }

        return list;
    }

    private static String manageSpaces(String word, int maxCount){
        int spaces = maxCount - word.length();
        String spc= "";
        for (int i = 0; i < spaces; i++) {
            spc += " ";
        }
        return spc;
    }

    private static List<Hashtable<Integer, String>> initList(int size){
        List<Hashtable<Integer, String>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i, null);
        }
        return list;
    }

}