import javax.mail.MessagingException;
import java.lang.StringBuilder;
import java.util.*;

public class SecretSantaRunner {
    private static HashMap<String, Person> persons;
    private static ArrayList<String> names;
    private static HashMap<String, String> emails;

    public static void main(String[] args) throws MessagingException {
        persons = new HashMap<String, Person>();
        names = new ArrayList<String>();
        emails = new HashMap<String, String>();

        readNamesFromFile();

        if (names.size() < 2) {
            System.out.println("Error: Invalid Number of Persons.");
            return;
        }

        SecretSanta secretSantaObject = new SecretSanta(persons);
        HashMap<String, String> santas = secretSantaObject.getSantaAssignees();
        MailHandler mailHandler = new MailHandler();
        for (HashMap.Entry<String, String> entry : santas.entrySet()) {
            //System.out.println(entry.getKey() + " " +  entry.getValue());
            StringBuilder sb = new StringBuilder();
            sb.append("Greetings, ");
            sb.append(entry.getKey());
            sb.append("\n\nYour Secret Santa Assignee is: ");
            sb.append(entry.getValue());
            sb.append("\nPlease add in your request ASAP.");
            sb.append("\nTHIS IS A TEST EMAIL. THIS IS NOT YOUR ASSIGNEE.");
            sb.append("\nLink to the Document: ");
            sb.append("https://docs.google.com/document/d/168OoYs1EkCaQ5iQ9cwRTCqpCbHlJJYmM6yA7XoCEE6s/edit?usp=sharing");
            sb.append("\nRegards, \n\tJonathan Chang");
            //System.out.println("Email: " + emails.get(entry.getKey()));
            //System.out.println(sb.toString());
            //mailHandler.sendMessage(emails.get(entry.getValue()), emails.get(entry.getKey()),
            //   "Secret Santa", sb.toString());
            System.out.println("Email Sent to: " + emails.get(entry.getKey()));
        }
    }

    /**
     * Reads the names from the file or prints out usage information
     */
    private static void readNamesFromFile() {
        try {
            Scanner reader = new Scanner(SecretSantaRunner.class.getResource("secretSanta.txt").openStream());
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String parts[] = line.split("\\|");
                if (parts.length != 2 && parts.length != 3 && parts.length != 4) {
                    System.out.println("Error: Enter Names in format 'Name | email'.");
                    names.clear();
                    emails.clear();
                    break;
                }
                String name = parts[0].trim();
                String email = parts[1].trim();
                Person person = new Person(name, email);
                // If file contains history
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
                emails.put(name, email);
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