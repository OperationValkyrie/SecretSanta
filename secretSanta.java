import java.io.FileReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class SecretSanta {

   //Holds the list of names
   private ArrayList<String> names;
   //Holds the key value pairs of santas and assignee            
   private HashMap<String, String> santas;    
   //Holds if the santa has viewed thier assignee yet 
   private HashMap<String, Boolean> hasViewed;
   
   //Holds the number of failed runs (No longer used with new assigning algorithm)
   private int failures;
   //Holds whether the assignments were sucessful (No longer used with new assigning algorithm)
   private boolean success;
   
   //Holds the randomness for assigning
   private Random random;
   
   /**
    * Constructor for this SecretSanta Object
    * @param ames The ArrayList of names to assgine SecretSantas (No duplicate names are allowed)
    * @returns The SecretSanta Object
   */
   public SecretSanta(ArrayList<String> names) {
      this.names = names;
      santas = new HashMap<String, String>();
      hasViewed = new HashMap<String, Boolean>();
      
      random = new Random(System.currentTimeMillis());
      
      failures = 0;
      
      if(verifyNames()) {
         success = assignSantas();
      } else{
         success = false;
      }
   }
   
   /**
    * Randomly assigns assignees to santas
   */
   private boolean assignSantas() {
      ArrayList<String> namesLeft = copyArrayList(names);
      for(int i = 0; i < names.size(); i++) {
         String santa = names.get(i);
         ArrayList<String> possibleNames = copyArrayList(namesLeft);
         //Remove santa from possible names to avoid getting self
         if(possibleNames.indexOf(santa) >= 0) {
            possibleNames.remove(possibleNames.indexOf(santa));
         }
         String assignee = possibleNames.get(random.nextInt(possibleNames.size()));
         santas.put(santa, assignee);
         namesLeft.remove(assignee);
      }
      setHasViewed();
      return true;
   }
   
   /**
    * Allows viewing of the secret santas through the command line
    * (TODO: Replace with email system).
   */
   public void allowViewing() {
      Scanner input = new Scanner(System.in);
      String santa = "";
      String assignee = "";
      
      while(!allViewed()) {
         printHasViewed();
         System.out.println();
         
         //Get santa name
         System.out.print("Enter your name: ");
         santa = input.nextLine();
         while(!names.contains(santa)) {
            System.out.print("Error: Santa Not Found ");
            System.out.print("Enter your name: ");
            santa = input.nextLine();
         }
         
         //Show assignee if not yet viewed otherwise refuse to show
         if(hasViewed.get(santa)) {
            System.out.println("Error: Assignee already viewed.");
            santa = "";
            continue;
         } else {
            assignee = santas.get(santa);
            System.out.println("Assignee: " + assignee);
            hasViewed.put(santa, true);
         }
 
         //Confirm assignee to continue
         System.out.print("Type your assignee: ");
         String confirm = input.nextLine();
         while(!confirm.equals(assignee)) {
            System.out.println("Error: Wrong Assignee.");
            System.out.print("Type your assignee: ");
            confirm = input.nextLine();
         }         
         
         santa = "";
         clearConsole();
      }
   }
   
   /**
    * Initalizes the has viewed to all false
   */
   private void setHasViewed() {
      for(String santa : santas.keySet()) {
         hasViewed.put(santa, false);
      }
   }
   
   /**
    * Checks if all santas have viewed thier assignee
    * @returns Whether or not all santas have viewed their assignee
   */
   private boolean allViewed() {
      for(String santa : santas.keySet()) {
         if(!hasViewed.get(santa)) {
            return false;
         }
      }
      return true;
   }
   
   /**
    * Creates a deep copy of the given ArrayList
    * @param original The ArrayList to copy
    * @returns The deep copy of the given ArrayList
   */
   private ArrayList<String> copyArrayList(ArrayList<String> original) {
      ArrayList<String> copy = new ArrayList<String>();
      for(int i = 0; i < original.size(); i++) {
         copy.add(original.get(i));
      }
      return copy;
   }
   
   /**
    * Prints out a list of who has viewed and who has not viewed thier assignee
   */
   private void printHasViewed() {
      for(String santa : hasViewed.keySet()) {
         System.out.print(santa + ": ");
         if(hasViewed.get(santa)) {
            System.out.println("Viewed.");
         } else{
            System.out.println("Not Viewed.");
         }
      }
   }
   
   /**
    * Returns the Hashmap for testing
    * (Should not be used outside testing)
    * @returns The santa-assginee HashMap
   */
   public HashMap<String, String> getSantaAssignees() {
      return santas;
   }
   
   /**
    * Returns the number of failures
    * (No longer in use)
    * @returns The number of failures
   */
   public int getFailures() {
      return failures;
   }
   
   /**
    * Returns whether the program was successful
    * @returns Whether or not the program ran successfully
   */
   public boolean getSuccess() {
      return success;
   }
   
   /**
    * Clears the console to prevent others from viewing a santa's assignee
   */
   public void clearConsole() {
      try {
         new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } catch(Exception e) {
      
      }
   }
}