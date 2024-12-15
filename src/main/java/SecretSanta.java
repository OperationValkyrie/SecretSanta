import java.util.*;

public class SecretSanta {

   //Holds the list of names
   private ArrayList<String> names;
   //Holds the key value pairs of santas and assignee            
   private HashMap<String, String> santas;

   private HashMap<String, Person> persons;
   private HashMap<String, ArrayList<String>> historyTickets;
   private int[] historyWeight = {0, 0, 1, 10, 20, 30};
   
   //Holds the number of failed runs (No longer used with new assigning algorithm)
   private int failures;
   //Holds whether the assignments were sucessful (No longer used with new assigning algorithm)
   private boolean success;
   
   //Holds the randomness for assigning
   private Random random;
   
   /**
    * Constructor for this SecretSanta Object
    * @param persons The ArrayList of names to assignee SecretSantas (No duplicate names are allowed)
    * @returns The SecretSanta Object
   */
   public SecretSanta(HashMap<String, Person> persons) {
      this.names = new ArrayList<String>(persons.keySet());
      this.persons = persons;
      santas = new HashMap<String, String>();
      historyTickets = new HashMap<String, ArrayList<String>>();

      random = new Random(System.currentTimeMillis());
      
      failures = 0;

      calculateHistoryTickets();
      success = assignSantas();
   }

   private void calculateHistoryTickets() {
      for (Map.Entry<String, Person> entry : persons.entrySet()) {
         ArrayList<String> history = entry.getValue().getHistory();
         int defaultIndex = history.size() >= historyWeight.length ? historyWeight.length - 1 : history.size();
         int defaultNumber = historyWeight[defaultIndex];
         int[] ticketNumber = new int[names.size()];
         Arrays.fill(ticketNumber, defaultNumber);

         int historyWeightIndex = 0;
         for (String historyName : history) {
            ticketNumber[names.indexOf(historyName)] = Math.min(ticketNumber[names.indexOf(historyName)], historyWeight[historyWeightIndex++]);
            if (defaultIndex < historyWeightIndex) {
               break;
            }
         }
         historyTickets.put(entry.getKey(), new ArrayList<String>());
         for (String name : names) {
            if (name == entry.getKey()) {
               continue;
            }
            ArrayList<String> tickets = historyTickets.get(entry.getKey());
            String[] ticketSingle = new String[ticketNumber[names.indexOf(name)]];
            Arrays.fill(ticketSingle, name);
            Collections.addAll(tickets, ticketSingle);
         }
         //System.out.println(entry.getKey() + ":" + historyTickets.get(entry.getKey()));
      }
   }

   /**
    * Randomly assigns assignees to santas
   */
   private boolean assignSantas() {
      Collections.shuffle(names, random);
      ArrayList<String> namesUsed = new ArrayList<String>();
      for (int i = 0; i < names.size(); ++i) {
         String santa = names.get(i);

         ArrayList<String> tickets = copyArrayList(historyTickets.get(santa));
         tickets.removeAll(namesUsed);
         if (tickets.size() == 0) {
            namesUsed.clear();
            santas.clear();
            i = -1;
            ++failures;
            continue;
         }
         Collections.shuffle(tickets, random);
         String assignee = tickets.get(random.nextInt(tickets.size()));
         santas.put(santa, assignee);
         namesUsed.add(assignee);
      }

      for (Map.Entry<String, String> entry : santas.entrySet()) {
         persons.get(entry.getKey()).setAssignee((entry.getValue()));
         //persons.get(entry.getKey()).addToHistory((entry.getValue()));
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
    * Returns the Hashmap for testing
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
}