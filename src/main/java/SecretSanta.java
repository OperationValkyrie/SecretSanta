import java.util.*;

public class SecretSanta {

    //Holds the list of names
    private ArrayList<String> names;
    //Holds the key value pairs of santas and assignee
    private HashMap<String, String> santas;

    private HashMap<String, Person> persons;

    //Holds the number of failed runs (No longer used with new assigning algorithm)
    private int failures;
    //Holds whether the assignments were sucessful (No longer used with new assigning algorithm)
    private boolean success;

    //Holds the randomness for assigning
    private final Random random;

    /**
     * Constructor for this SecretSanta Object
     *
     * @param persons The ArrayList of names to assignee SecretSantas (No duplicate names are allowed)
     * @returns The SecretSanta Object
     */
    public SecretSanta(HashMap<String, Person> persons) {
        this.names = new ArrayList<String>(persons.keySet());
        this.persons = persons;
        santas = new HashMap<String, String>();

        random = new Random(System.currentTimeMillis());

        failures = 0;

        calculateHistoryTickets();
        success = assignSantas();
    }

    private void calculateHistoryTickets() {
        for (Map.Entry<String, Person> entry : persons.entrySet()) {
            //System.out.println(entry.getKey() + ":" + entry.getValue().getHistory());
            entry.getValue().calculateHistoryTickets(names, random);
            //System.out.println(entry.getKey() + ":" + entry.getValue().getHistoryTickets());
        }
    }

    /**
     * Randomly assigns assignees to santas
     */
    private boolean assignSantas() {
        ArrayList<Integer> namesUsed = new ArrayList<>();
        ArrayList<Integer> orderOfAssignment = new ArrayList<>();
        int o = 0;
        for (String name : names) {
            orderOfAssignment.add(o++);
        }
        Collections.shuffle(orderOfAssignment, random);
        for (int j = 0; j < names.size(); ++j) {
            int i = orderOfAssignment.get(j);
            String santa = names.get(i);

            ArrayList<Integer> tickets = new ArrayList<>(persons.get(santa).getHistoryTickets());
            if (tickets.isEmpty()) {
                System.out.println("Error: Name: " + santa + " has no valid possible assignees.");
                System.exit(1);
            }
            tickets.removeAll(namesUsed);
            // Remove santa of this santa from possible
            if (namesUsed.contains(i)) {
                for (HashMap.Entry<String, String> entry : santas.entrySet()) {
                    if (Objects.equals(entry.getValue(), santa)) {
                        tickets.removeAll(Collections.singleton(names.indexOf(entry.getKey())));
                        break;
                    }
                }
            }
            if (tickets.isEmpty()) {
                namesUsed.clear();
                santas.clear();
                j = -1;
                Collections.shuffle(orderOfAssignment, random);
                ++failures;
                if (failures >= 10) {
                    System.out.println("Error: Unable to find a valid combination.");
                    return false;
                }
                continue;
            }
            Collections.shuffle(tickets, random);
            String assignee = names.get(tickets.get(random.nextInt(tickets.size())));
            santas.put(santa, assignee);
            namesUsed.add(names.indexOf(assignee));
            //j = names.indexOf(assignee);
        }

        for (Map.Entry<String, String> entry : santas.entrySet()) {
            persons.get(entry.getKey()).setAssignee((entry.getValue()));
            //persons.get(entry.getKey()).addToHistory((entry.getValue()));
        }

        return true;
    }

    /**
     * Creates a deep copy of the given ArrayList
     *
     * @param original The ArrayList to copy
     * @returns The deep copy of the given ArrayList
     */
    private ArrayList<Integer> copyArrayList(ArrayList<Integer> original) {
        return new ArrayList<>(original);
    }

    /**
     * Returns the Hashmap for testing
     *
     * @returns The santa-assginee HashMap
     */
    public HashMap<String, String> getSantaAssignees() {
        return santas;
    }

    /**
     * Returns the number of failures
     * (No longer in use)
     *
     * @returns The number of failures
     */
    public int getFailures() {
        return failures;
    }

    /**
     * Returns whether the program was successful
     *
     * @returns Whether or not the program ran successfully
     */
    public boolean getSuccess() {
        return success;
    }
}