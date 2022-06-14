import java.util.*;
import java.io.*;

public class wordle {
    //reduce list to words with previous step's constraints
    //make sure it includes all yellow letter and that greens are in the correct spot and there are no grays
    //reduce list to words with the most amount of most common letters
    //make HashMap to count appearances of each character then use it in a comparator 
    //for a heap that gets each letter in alphabet and keep top 5
    //find if there is word with all 5, then 4, stop when non-empty list is made
    //randomly pick entry
    //six guesses

    //gets the whole text file and loads into arraylist
    public static ArrayList<String> initList = getWords();

    //method for running a game
    public static boolean game() {
        //ArrayList<String> currList = new ArrayList<>(initList);
        //System.out.println(initList);
        Random rand = new Random();
        String sol = initList.get(rand.nextInt(initList.size())); //get random entry from valid word list
        System.out.println("The secret word is: "+sol);
        HashMap<Integer,Character> green = new HashMap<>(); //letters in right positon
        HashSet<Character> yellow = new HashSet<>(); //required letters
        HashSet<Character> gray = new HashSet<>(); //not allowed letters
        for(int i=0; i<6; i++) { //loop through six guesses
            ArrayList<String> currList = new ArrayList<>(initList); //initial potential words as entire starting list
            if(i!=0) { //Greedy constrainsts start after first iteration
                ArrayList<String> temp = new ArrayList<>(); //New list of potential words
                for(String s: currList) {
                    boolean valid = true; //flag for valid words
                    HashSet<Character> chs = new HashSet<>(); //keeps all characters of current word
                    for(int j=0; j<s.length(); j++) {
                        if(gray.contains(s.charAt(j))) { //if has gray letters, reject
                            valid = false;
                            break;
                        }
                        else if(green.containsKey(j) && s.charAt(j) != green.get(j)) { //if green is in wrong spot, reject
                            valid = false;
                            break;
                        }
                        chs.add(s.charAt(j));
                    }
                    for(char ch: yellow) { //If missing yellow letters, reject
                        if(!chs.contains(ch)) {
                            valid = false;
                            break;
                        }
                    }
                    if(valid) {
                        temp.add(s); //otherwise keep the word in the list
                    }
                }
                currList = temp; //update list to valid words based on constraints
            }
            
            HashMap<Character,Integer> counts = new HashMap<>(); //HashMap for saving count of each character in current list
            PriorityQueue<Character> pq = new PriorityQueue<>((a,b)->counts.get(a)-counts.get(b)); //Heap for keeping the top 5 most common letters
            for(String s: currList) {
                for(int j=0; j<s.length(); j++) {
                    counts.put(s.charAt(j),counts.getOrDefault(s.charAt(j), 0)+1); //loop for doing the counting
                }
            }
            for(char ch: counts.keySet()) { //keeping only top five in the heap
                pq.add(ch);
                if(pq.size()>5) {
                    pq.remove();
                }
            }

            HashSet<Character> topChs = new HashSet<>(); //converting heap to HashSet for O(1) search retrieval

            while(pq.size()!=0) {
                topChs.add(pq.remove());
            }
            
            //more narrowed down list after applying heuristic 
            //of most number of most common characters
            ArrayList<String> temp = new ArrayList<>(); 
            for(int j=topChs.size(); j>0; j--) {
                for(String s: currList) {
                    HashSet<Character> curChs = new HashSet<>();
                    for(int k=0; k<s.length(); k++) {
                        if(topChs.contains(s.charAt(k))) {
                            curChs.add(s.charAt(k));
                        }
                    }
                    if(topChs.size()==j) {
                        temp.add(s);
                    }
                }
                if(temp.size()>0) {
                    currList = temp;
                    break;
                }
            }
            System.out.println("Word bank size: "+currList.size());
            String guess = currList.get(rand.nextInt(currList.size())); //randomly guesses from current reduced word bank
            if(guess.equals(sol)) { //checks if correct
                System.out.println("We win: "+sol);
                return true;
            }
            HashSet<Character> solChs = new HashSet<>(); //Makes hashSet of chars in secret word for O(1) retreivals
            for(int j=0; j<sol.length(); j++) {
                solChs.add(sol.charAt(j));
            }
            for(int j=0; j<sol.length(); j++) { //updating constraints
                if(solChs.contains(guess.charAt(j))) {
                    yellow.add(guess.charAt(j));
                    if(sol.charAt(j)==guess.charAt(j)) {
                        green.put(j,guess.charAt(j));
                    }
                }
                else {
                    gray.add(guess.charAt(j));
                }
            }
            System.out.println("Guess: "+guess);
        }
        System.out.println("It was: "+sol);
        return false;
    }

    public static ArrayList<String> getWords() { //IO for getting the valid words from the txt
        ArrayList<String> initList = new ArrayList<String>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File("words.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                initList.add(line.replace("\n", ""));
        }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return initList;
    }

    public static void main(String[] args) { //runs the game 10000 times
        int wins = 0;
        for(int i=0; i<10000; i++) {
            boolean win = game();
            if(win) {
                wins++;
            }
        }
        System.out.println("Wins out of 10,000: "+wins);
        
    }

}