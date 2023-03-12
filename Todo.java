//Imports
import java.time.LocalDateTime;

//To Do Class
public class Todo {
    //Declarations
    private String text;
    private LocalDateTime due;
    private Category cat;
    private Importance importance;
    private Status completion;

    //To Do Constructor
    public Todo(String text, LocalDateTime due, Category cat, Importance importance, Status completion) {
        this.text = text;
        this.due = due;
        this.cat = cat;
        this.importance = importance;
        this.completion = completion;
    }

    //toString Method For ToDo
    public String toString() { return text + "      Due: [" + due + "]      Priority: " + importance + "      Completion: " + completion + "      Category: " + cat; }

    //Getters & Setters
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDue() {
        return due;
    }
    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    public Category getCat() {
        return cat;
    }
    public void setCat(Category cat) {
        this.cat = cat;
    }

    public Importance getImportance() {
        return importance;
    }
    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public Status getCompletion() {
        return completion;
    }
    public void setCompletion(Status completion) {
        this.completion = completion;
    }
}
