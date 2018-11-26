import java.io.FileReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class secretSanta {

   private ArrayList<String> names;
   private HashMap<String, String> santas;
   private HashMap<String, Boolean> hasViewed;
   
   private int failures;
   private boolean success;
   
   private Random random;
   
   public secretSanta(ArrayList<String> names) {
      this.names = names;
      santas = new HashMap<String, String>();
      hasViewed = new HashMap<String, Boolean>();
      
      random = new Random();
      
      success = assignSantas();
   }
   
   private void readNamesFromFile() {
      try{
         Scanner reader = new Scanner(new FileReader("secretSanta.txt"));
         while(reader.hasNextLine()) {
            names.add(reader.nextLine());
         }
      } catch(Exception e) {
         System.out.println("Error: Enter names in secretSanta.txt or args.");
      }
   }
   
   private boolean assignSantas() {
      ArrayList<String> namesLeft = copyArrayList(names);
      while(santas.size() < names.size()) {
         for(int i = 0; i < names.size(); i++) {
            String assignee = namesLeft.get(random.nextInt(namesLeft.size()));
            santas.put(names.get(i), assignee);
            namesLeft.remove(assignee);
         }
         if(personHasSelf()) {
            namesLeft = copyArrayList(names);
            santas = new HashMap<String, String>();
            failures++;
            //return false;
         }
      }
      setHasViewed();
      return true;
   }
   
   public void allowViewing() {
      Scanner input = new Scanner(System.in);
      String santa = "";
      String assignee = "";
      
      while(!allViewed()) {
         printHasViewed();
         System.out.println();
         
         while(!names.contains(santa)) {
            System.out.print("Enter your name: ");
            santa = input.nextLine();
         }
         if(hasViewed.get(santa)) {
            System.out.println("Error: Assignee already viewed.");
            santa = "";
            continue;
         } else {
            assignee = santas.get(santa);
            System.out.println("Assignee: " + assignee);
            hasViewed.put(santa, true);
         }
 
         santa = "";
         System.out.print("Type your assignee: ");
         String confirm = input.nextLine();
         while(!confirm.equals(assignee)) {
            System.out.println("Error: Wrong Assignee.");
            System.out.print("Type your assignee: ");
            confirm = input.nextLine();
         }         
         
         clearConsole();
      }
   }
   
   private boolean personHasSelf() {
      for(String santa : santas.keySet()) {
         if(santa.equals(santas.get(santa))) {
            return true;
         }
      }
      return false;
   }
   
   private void setHasViewed() {
      for(String santa : santas.keySet()) {
         hasViewed.put(santa, false);
      }
   }
   
   private boolean allViewed() {
      for(String santa : santas.keySet()) {
         if(!hasViewed.get(santa)) {
            return false;
         }
      }
      return true;
   }
   
   private ArrayList<String> copyArrayList(ArrayList<String> original) {
      ArrayList<String> copy = new ArrayList<String>();
      for(int i = 0; i < original.size(); i++) {
         copy.add(original.get(i));
      }
      return copy;
   }
   
   private void printHasViewed() {
      for(String santa : hasViewed.keySet()) {
         System.out.println(santa + ": " + hasViewed.get(santa));
      }
   }
   
   public HashMap<String, String> getSantaAssignees() {
      return santas;
   }
   
   public int getFailures() {
      return failures;
   }
   
   public boolean getSuccess() {
      return success;
   }
   
   public void clearConsole() {
      try {
         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } catch(Exception e) {
      
      }
   }
}