import java.util.ArrayList;

public class Person {
    private String name;
    private String email;
    private String assignee;
    private ArrayList<String> history;

    public Person (String name, String email) {
        this(name, email, new ArrayList<String>());
    }

    public Person (String name, String email, ArrayList<String> history) {
        this.name = name;
        this.email = email;
        this.history = history;
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

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public void addToHistory(String assignee) {
        this.history.add(0, assignee);
    }
}
