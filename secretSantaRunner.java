import java.io.FileReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class SecretSantaRunner{
   private static ArrayList<String> names;
   
   public static void main(String[] args) {
      names = new ArrayList<String>();
      
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
      
      SecretSanta secretSantaObject = new SecretSanta(names);
      
      secretSantaObject.allowViewing();
   }
   
   /**
    * Reads the names from the file or prints out ussage information
    */
   private static void readNamesFromFile() {
      try{
         Scanner reader = new Scanner(new FileReader("SecretSanta.txt"));
         while(reader.hasNextLine()) {
            names.add(reader.nextLine());
         }
      } catch(Exception e) {
         System.out.println("Error: Enter names in secretSanta.txt or args.");
         System.out.println("  To run with names from file use the command:");
         System.out.println("    java -jar SecretSanta.jar");
         System.out.println("  To run with names as arguments use the command:");
         System.out.prihtln("    java - jar SecretSanta.jar name1 name2 name3");
      }
   }
}