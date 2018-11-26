import java.io.FileReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.Random;

public class secretSantaRunner{
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
      
      secretSanta secretSantaObject = new secretSanta(names);
      
      secretSantaObject.allowViewing();
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