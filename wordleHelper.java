import java.util.*;
import java.io.*;

public class wordleHelper {
    //Similar to the automated wordle.java, but actually acts as
    //An AI assistant for the real game by getting the dynamically
    //Changing constraints from the user at each step
    //Works the same but is interactive by prompting user for input

    public static ArrayList<String> initList = getWords();

    public static ArrayList<String> getWords() {
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

    public static void main(String[] args) {

        Random rand = new Random();
        HashMap<Integer,Character> green = new HashMap<>(); //letters in right positon
        HashSet<Character> yellow = new HashSet<>(); //required letters
        HashSet<Character> gray = new HashSet<>(); //not allowed
        for(int i=0; i<6; i++) {
            ArrayList<String> currList = new ArrayList<>(initList);
            if(i!=0) {
                ArrayList<String> temp = new ArrayList<>();
                for(String s: currList) {
                    boolean valid = true;
                    HashSet<Character> chs = new HashSet<>();
                    for(int j=0; j<s.length(); j++) {
                        if(gray.contains(s.charAt(j))) {
                            valid = false;
                            break;
                        }
                        else if(green.containsKey(j) && s.charAt(j) != green.get(j)) {
                            valid = false;
                            break;
                        }
                        chs.add(s.charAt(j));
                    }
                    for(char ch: yellow) {
                        if(!chs.contains(ch)) {
                            valid = false;
                            break;
                        }
                    }
                    if(valid) {
                        temp.add(s);
                    }
                }
                currList = temp;
            }
            HashMap<Character,Integer> counts = new HashMap<>();
            PriorityQueue<Character> pq = new PriorityQueue<>((a,b)->counts.get(a)-counts.get(b));
            for(String s: currList) {
                for(int j=0; j<s.length(); j++) {
                    counts.put(s.charAt(j),counts.getOrDefault(s.charAt(j), 0)+1);
                }
            }
            for(char ch: counts.keySet()) {
                pq.add(ch);
                if(pq.size()>5) {
                    pq.remove();
                }
            }

            HashSet<Character> topChs = new HashSet<>();

            while(pq.size()!=0) {
                topChs.add(pq.remove());
            }

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
            String guess = currList.get(rand.nextInt(currList.size()));
            System.out.println("Guess this: "+guess);
            System.out.println("Did you win? (y/n)");
            Scanner scan = new Scanner(System.in);
            String ans = scan.next();
            if(ans.equals("y")) {
                System.out.println("We win!");
                break;
            }
            else {
                System.out.println("List green letters with position (comma delimited, enter ',' if empty)");
                String gre = scan.next();
                System.out.println("List yellow letters (comma delimited, enter ',' if empty)");
                String yel = scan.next();
                System.out.println("List gray letters (comma delimited, enter ',' if empty)");
                String gra = scan.next();
                //scan.close();
                String[] gree = gre.split(",",-1);
                for(int j=0; !gre.equals(",") && j< gree.length; j+=2) {
                    green.put(Integer.valueOf(gree[j+1]), gree[j].charAt(0));
                }
                String[] yell = yel.split(",");
                for(int j=0; !yel.equals(",") && j< yell.length; j++) {
                    yellow.add(yell[j].charAt(0));
                }
                String[] graa = gra.split(",");
                for(int j=0; !gra.equals(",") && j< graa.length; j++) {
                    gray.add(graa[j].charAt(0));
                }
            }
        }
        System.out.println("Good game!");
             
    }

}