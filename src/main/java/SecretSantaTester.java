import java.util.*;

public class SecretSantaTester {
    private static HashMap<String, Person> persons;
    private static HashMap<String, HashMap<String, Integer>> santaResults;
    private static ArrayList<String> names;

    public static void main(String[] args) {
        persons = new HashMap<String, Person>();
        names = new ArrayList<String>();

        santaResults = new HashMap<String, HashMap<String, Integer>>();

        readNamesFromFile();

        if (names.size() < 2) {
            System.out.println("Error: Invalid Number of Persons.");
            return;
        }

        initializeResults();

        int totalRuns = 100000;

        int totalFailures = 0;
        int maxFailures = 0;
        for (int i = 0; i < totalRuns; i++) {
            System.out.print("Run " + i + ": ");
            try {
                SecretSanta secretSantaOccurance = new SecretSanta(persons);
                if (secretSantaOccurance.getSuccess()) {
                    readResults(secretSantaOccurance.getSantaAssignees());
                    totalFailures += secretSantaOccurance.getFailures();
                    maxFailures = Math.max(maxFailures, secretSantaOccurance.getFailures());
                    System.out.print("Success");
                } else {

                    totalFailures++;

                    System.out.print("Failure");
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                totalFailures++;
                System.out.print("Failure Exception");
            }
            System.out.println();
        }
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i) + ": ");
            for (int j = 0; j < names.size(); j++) {
                if (!names.get(i).equals(names.get(j))) {
                    int value = santaResults.get(names.get(i)).get(names.get(j));
                    System.out.printf("   %10s: %8d - %1.4f\n", names.get(j), value, (double) value / totalRuns);
                }
            }
        }
        System.out.println("Failures: " + totalFailures + " - " + (double) totalFailures / totalRuns + " - " + maxFailures);
    }

    private static void initializeResults() {
        for (int i = 0; i < names.size(); i++) {
            santaResults.put(names.get(i), new HashMap<String, Integer>());
            for (int j = 0; j < names.size(); j++) {
                if (!names.get(i).equals(names.get(j))) {
                    santaResults.get(names.get(i)).put(names.get(j), 0);
                }
            }
        }
    }

    private static void readResults(HashMap<String, String> results) {
        //System.out.println(results);
        for (int i = 0; i < names.size(); i++) {
            String assignee = results.get(names.get(i));
            Integer currentValue = santaResults.get(names.get(i)).get(assignee);
            currentValue++;
            santaResults.get(names.get(i)).put(assignee, currentValue);
        }
    }

    /**
     * Reads the names from the file or prints out usage information
     */
    private static void readNamesFromFile() {
        try {
            Scanner reader = new Scanner(SecretSantaTester.class.getResource("secretSanta.txt").openStream());
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split("\\|");
                if (parts.length != 2 && parts.length != 3 && parts.length != 4) {
                    System.out.println("Error: Enter Names in format 'Name | email'.");
                    names.clear();
                    break;
                }
                String name = parts[0].trim();
                String email = parts[1].trim();
                Person person = new Person(name, email);
                if (parts.length >= 3) {
                    person.setHistory(new ArrayList<>(Arrays.asList(parts[2].trim().split(","))));
                    person.getHistory().replaceAll(String::trim);
                }
                // If file contains banned
                if (parts.length >= 4) {
                    person.setBanned(new ArrayList<>(Arrays.asList(parts[3].trim().split(","))));
                }
                if (persons.containsKey(name)) {
                    System.out.println("Error: Duplicate Name: " + name);
                    System.exit(1);
                }
                persons.put(name, person);
                names.add(name);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error: Enter names in secretSanta.txt or args.");
            System.out.println("  To run with names from file use the command:");
            System.out.println("    java -jar SecretSanta.jar");
            System.out.println("  To run with names as arguments use the command:");
            System.out.println("    java - jar SecretSanta.jar name1 name2 name3");
        }
    }
}