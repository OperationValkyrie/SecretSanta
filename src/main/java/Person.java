import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Person {
    private final String name;
    private final String email;
    private String assignee;
    private ArrayList<String> history;
    private final ArrayList<Integer> historyTickets;
    private ArrayList<String> banned;

    private static final int[] historyWeight = {0, 0, 10, 20, 20, 30};


    public Person(String name, String email) {
        this(name, email, new ArrayList<String>());
    }

    public Person(String name, String email, ArrayList<String> history) {
        this.name = name;
        this.email = email;
        this.history = history;
        this.historyTickets = new ArrayList<>();
        this.banned = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public ArrayList<Integer> getHistoryTickets() {
        return historyTickets;
    }

    public ArrayList<String> getBanned() {
        return banned;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public void addToHistory(String assignee) {
        this.history.add(0, assignee);
    }

    public void calculateHistoryTickets(ArrayList<String> names, Random random) {
        // Only run function once
        if (!historyTickets.isEmpty()) {
            return;
        }
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
        for (String name : names) {
            if (this.name.equals(name) || banned.contains((name))) {
                continue;
            }
            int capacity = ticketNumber[names.indexOf(name)];
            ArrayList<Integer> ticketSingle = new ArrayList<Integer>(Collections.nCopies(capacity, names.indexOf(name)));
            historyTickets.addAll(ticketSingle);
        }
    }

    public void setBanned(ArrayList<String> banned) {
        this.banned = banned;
    }

    public void addToBanned(String ban) {
        this.banned.add(0, ban);
    }
}
