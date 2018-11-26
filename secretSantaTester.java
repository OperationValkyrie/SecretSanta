import java.io.FileReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class SecretSantaTester{

   private static HashMap<String, HashMap<String, Integer>> santaResults;
   private static ArrayList<String> names;
   
   public static void main(String[] args) {
      names = new ArrayList<String>();
      
      santaResults = new HashMap<String, HashMap<String, Integer>>();
      
      for(int i = 0; i < args.length; i++) {
         names.add(args[i]);
      }
      if(names.size() == 0) {
         readNamesFromFile();
      }
      if(names.size() == 1) {
         System.out.println("Error: Single Person.");
         return;
      }
      
      intializeResults();
      
      int totalRuns = 100000;
      
      int totalFailures = 0;
      for(int i = 0; i < totalRuns; i++) {
         System.out.print("Run " + i + ": ");
         SecretSanta secretSantaOccurance = new SecretSanta(names);
         if(secretSantaOccurance.getSuccess()) {
            readResults(secretSantaOccurance.getSantaAssignees());
            totalFailures += secretSantaOccurance.getFailures();
            System.out.print("Success");
         } else {
            totalFailures++;
            System.out.print("Failure");
         }
         System.out.println();
      }
      for(int i = 0; i < names.size(); i++) {
         System.out.println(names.get(i) + ": ");
         for(int j = 0; j < names.size(); j++) {
            if(!names.get(i).equals(names.get(j))) {
               int value = santaResults.get(names.get(i)).get(names.get(j));
               System.out.println("   " + names.get(j) + ": " + 
                                  value + " - " + (double) value/totalRuns);
            }
         }
      }
      System.out.println("Failures: " + totalFailures + " - " + (double) totalFailures/totalRuns);
   }
   
   private static void intializeResults() {
      for(int i = 0; i < names.size(); i++) {
         santaResults.put(names.get(i), new HashMap<String, Integer>());
         for(int j = 0; j < names.size(); j++) {
            if(!names.get(i).equals(names.get(j))) {
               santaResults.get(names.get(i)).put(names.get(j), 0);
            }
         }
      }
   }
   
   private static void readResults(HashMap<String, String> results) {
      for(int i = 0; i < names.size(); i++) {
         String assignee = results.get(names.get(i));
         Integer currentValue = santaResults.get(names.get(i)).get(assignee);
         currentValue++;
         santaResults.get(names.get(i)).put(assignee, currentValue);
      }
   }
   
   private static void readNamesFromFile() {
      try{
         Scanner reader = new Scanner(new FileReader("secretSanta.txt"));
         while(reader.hasNextLine()) {
            names.add(reader.nextLine());
         }
      } catch(Exception e) {
         System.out.println("Error: Enter names in secretSanta.txt or args.");
      }
   }
}