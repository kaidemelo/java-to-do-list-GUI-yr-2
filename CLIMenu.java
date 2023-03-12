//Imports
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

//CLIMenu Class
public class CLIMenu {
    //Initializations
    ArrayList<Todo> Todo;
    Scanner scanChoice = new Scanner(System.in);
    boolean startup = true;
    int menuChoice= 0, menuChoices = 5, fieldChoices = 5;
    int importanceChoices = Importance.values().length;
    int categoryChoices = Category.values().length;
    int statusChoices = Status.values().length;
    String dueDatePickerText = "Enter a due date for the todo in the format (YYYY-MM-DDTHH:MM)";
    String deleteErrorMsg = "Sorry, choice must be one of the Todos listed above.";
    String fieldErrorMsg = "Sorry, choice must be one of the fields listed above.";
    String dueDateErrorMsg = "Sorry, please enter a valid due date for your todo";
    String statusErrorMsg = "Sorry, choice must be a value between 1 and 4.";
    String menuErrorText = "Sorry, choice must be a value between 1 and 5.";
    String catErrorMsg = "Sorry, choice must be a value between 1 and 6.";
    String impErrorMsg = "Sorry, choice must be a value between 1 and 3.";
    String updatePickerText = "Which ToDo do you want to update?";
    String titleErrorMsg = "Sorry, please enter a valid title";
    String deleteText = "Which ToDo do you want to delete? ";
    String titlePickerText = "Enter title for the todo";
    String startupText = """
            Welcome to the To Do list program
            ----------------------------------------------""";
    String fieldText = """
            Select an item between 1 and 5 and press enter
            1. Title
            2. Due Date
            3. Category
            4. Importance
            5. Completion
            """;
    String catPickerText = """
            Select a category for the todo
            Select an item between 1 and 6 and press enter
            1. Red
            2. White
            3. Blue
            4. Purple
            5. Yellow
            6. Green""";
    String impPickerText = """
            Select an importance for the todo
            Select an item between 1 and 3 and press enter
            1. LOW
            2. NORMAL
            3. HIGH""";
    String statusPickerText = """
            Select a status for the todo
            Select an item between 1 and 4 and press enter
            1. Pending
            2. Started
            3. Partial
            4. Completed
            """;
    String mainMenuText = """
            Select an item between 1 and 5 and press enter
            1: List todos
            2: Add todo
            3: Update todo
            4: Delete todo
            5: Quit" 
            """;

    //CLIMenu Constructor
    public CLIMenu(ArrayList<Todo> Todo) {
        this.Todo = Todo;
        mainMenu();
    }

    //Main Menu Method
    public void mainMenu() {
        do {
            if (startup) {
                System.out.println(startupText);            //Startup Message
                startup = false;
            }
            menuChoice = menuPicker();                      //User selects choice via menuPicker method

            switch (menuChoice) {                           //Switch statement for programs choices
                case 1 -> toDoLister();                     // Option 1 - List All ToDo's
                case 2 -> createTodo();                     // Option 2 - Add ToDo
                case 3 -> updateTodo();                     // Option 3 - Update ToDo
                case 4 -> deleteTodo();                     // Option 4 - Delete ToDo
                case 5 -> System.out.println("Bye...");     // Option 5 - Quit
            }
        } while (menuChoice != 5);            //Do while loop that runs until the quit option is selected by the user
        scanChoice.close();                   //Closes scanner when quit has been selected by user to prevent data leak
    }

    //Method To Create Todo & Adds It To ArrayList
    public void createTodo() {
        String text = titlePicker();                        // Title
        LocalDateTime due = dueDatePicker();                // Due date & time
        Category cat = catPicker();                         // Category
        Importance imp = impPicker();                       // Importance
        Status stat = Status.PENDING;                       // Status
        Todo toDoObj = new Todo(text, due, cat, imp, stat); //Creates ToDo from user inputs
        Todo.add(toDoObj);                                  //Adds todo to the "Todo" array list
    }

    //Method To Update Todo & validates user input
    public void updateTodo() {
        int size = Todo.size(); //Gets amount of Todos in arraylist
        if (size == 0) {
            //If there are no todos in the array list
            System.out.print(Category.RED.getColour() + "No Todo's to update..." + "\033[0m" + "\n");
        } else {
            toDoLister();   //Lists todo's with numbers so user can select a todo to update
            //Gets user input via inputInt method
            int updateChoice = inputInt(updatePickerText, deleteErrorMsg, scanChoice, size, true);
            updateChoice -= 1;
            //Gets user input via inputInt method
            int fieldChoice  = inputInt(fieldText, fieldErrorMsg, scanChoice, fieldChoices, false);
            //Sets variables to the values of the elements of the Todo the user selected
            String text = Todo.get(updateChoice).getText();
            LocalDateTime due = Todo.get(updateChoice).getDue();
            Category cat = Todo.get(updateChoice).getCat();
            Importance imp = Todo.get(updateChoice).getImportance();
            Status stat = Todo.get(updateChoice).getCompletion();

            //Switch Statement for the field choice
            switch (fieldChoice) {
                case 1 -> text = titlePicker();
                case 2 -> due = dueDatePicker();
                case 3 -> cat = catPicker();
                case 4 -> imp = impPicker();
                case 5 -> stat = statusPicker();
            }
            //Sets new Todo object as the values of the old Todo along with the new value the user entered
            Todo toDoObj = new Todo(text, due, cat, imp, stat);
            //Replaces the old Todo with its updated value, and other values which didn't change
            Todo.set(updateChoice, toDoObj);
            updateChoice += 1; //Adds one to updateChoice to return arraylist index back to the number the user selected
            System.out.println("Todo " + updateChoice + ": " + "updated..." + "\n");
        }
    }

    //Method To Pick Status & Validates User Input
    public Status statusPicker() {
        Status status = null;  //Initializations
        //Gets user input via inputInt method
        int statChoice = inputInt(statusPickerText, statusErrorMsg, scanChoice, statusChoices, false);

        //Switch Statement for the users choice
        switch (statChoice) {
            case 1 -> status = Status.PENDING;
            case 2 -> status = Status.STARTED;
            case 3 -> status = Status.PARTIAL;
            case 4 -> status = Status.COMPLETED;
        }
        return status;
    }

    //Method To Pick Due Date (Gets user input via inputDueDate utility method)
    public LocalDateTime dueDatePicker() {
        String dueDateString = inputString(dueDatePickerText, dueDateErrorMsg, scanChoice, true);
        return LocalDateTime.parse(dueDateString);
    }

    //Method To Pick Menu Choice (Gets user input via inputInt utility method)
    public int menuPicker() { return inputInt(mainMenuText, menuErrorText, scanChoice, menuChoices, false); }

    //Method To Pick The Title (Gets user input via inputString utility method)
    public String titlePicker() { return inputString(titlePickerText, titleErrorMsg, scanChoice, false); }

    //Method To List All Todo's
    public void toDoLister() {
        //Initializations
        int size = Todo.size(); //Gets amount of Todos in the ArrayList
        int x = 0, y = 1;
        if (size == 0) {    //If there are no todos in the array list
            System.out.print(Category.RED.getColour() + "No Todo's to list..." + "\033[0m" + "\n");
        } else {
            do {            //Iterates through the Todo ArrayList and returns the Todos with their index + 1
                System.out.print(y + ". ");
                System.out.println(Todo.get(x).toString());
                x += 1;
                y += 1;
            } while(x < size);
            System.out.print("\n");
        }
    }

    //Method To Pick A Category For A ToDo
    public Category catPicker() {
        Category cat = null;  //Initializations
        //Gets user input via inputInt method
        int catChoice = inputInt(catPickerText, catErrorMsg, scanChoice, categoryChoices, false);

        //Switch Statement for the users choice
        switch (catChoice) {
            case 1 -> cat = Category.RED;
            case 2 -> cat = Category.WHITE;
            case 3 -> cat = Category.BLUE;
            case 4 -> cat = Category.PURPLE;
            case 5 -> cat = Category.YELLOW;
            case 6 -> cat = Category.GREEN;
        }
        return cat;
    }

    //Method To Pick A Importance For A ToDo
    public Importance impPicker() {
        Importance imp = null;  //Initializations
        //Gets user input via inputInt method
        int impChoice = inputInt(impPickerText, impErrorMsg, scanChoice, importanceChoices, false);

        //Switch Statement for the users choice after validation
        switch (impChoice) {
            case 1 -> imp = Importance.LOW;
            case 2 -> imp = Importance.NORMAL;
            case 3 -> imp = Importance.HIGH;
        }
        return imp;
    }

    //Method To Pick A Todo To Delete
    public void deleteTodo() {
        int size = Todo.size(); //Gets number of Todo's in the array list
        if (size == 0) {    //If there are no todos in the array list
            System.out.print(Category.RED.getColour() + "No Todo's to delete..." + "\033[0m" + "\n");
        } else {
            toDoLister();   //Lists todo's with numbers so user can select a todo to delete
            //Gets user input via inputInt method
            int deleteChoice = inputInt(deleteText, deleteErrorMsg, scanChoice, size, false);
            deleteChoice -= 1;
            Todo.remove(deleteChoice);  //Removes selected Todo from array list
            deleteChoice += 1;  //Adds one to deleteChoice to return arraylist index back to the selected number
            System.out.println("Todo " + deleteChoice + ": " + "deleted..." + "\n");
        }
    }

    //Utility Method For Getting String From The User & Validate The Input For Errors
    public String inputString(String prompt, String errorMsg, Scanner scanner, boolean inputDueDate) {
        //Initializations
        String choice = "";
        boolean exception = false, chosen = false;
        System.out.print(prompt + "\n");    //Message Prompt For User Input
        do {
            try {
                if (inputDueDate) {
                    choice = scanner.nextLine();    //Stores next string entered by the user
                    LocalDateTime.parse(choice);    //Parses the string into date format
                } else {
                    choice = scanner.nextLine(); //Stores next string entered by the user
                }
            } catch (Exception e) {
                exception = true;   //Catches any exception caused by improper input and returns this message
                System.out.println(Category.RED.getColour() + errorMsg + "\033[0m");
            }
            if (!exception) { chosen = true; }
            exception = false;  //Sets exception to false after every run of the do while loop
            scanner = new Scanner(System.in);
        } while (!chosen);
        return choice;
    }

    //Utility Method For Getting Int From The User & Validate The Input For Errors
    public int inputInt(String prompt, String errorMsg, Scanner scanner, int rangeEnd, boolean update) {
        //Initializations
        int choice = 0;
        boolean exception = false, chosen = false;
        System.out.print(prompt + "\n");    //Message Prompt For User Input
        do {
            try {
                if (update) {
                    choice = scanner.nextInt(); //Stores next integer entered by the user
                    choice -= 1; //Decrement choice to match array indexes as they start from 0
                    Todo.get(choice);   //This just checks if the choice exists it will cause an exception if not
                    choice += 1;
                } else {
                    choice = scanner.nextInt(); //Stores next integer entered by the user
                }
            } catch (Exception e) {
                exception = true;   //Catches any exception caused by improper input and returns this message
                System.out.println(Category.RED.getColour() + errorMsg + "\033[0m");
            }
            if (!exception) {
                if (choice > 0 && choice < rangeEnd + 1) {  //No exception was caught and answer fits requirements
                    chosen = true;
                } else {
                    //No exception was caught but answer doesn't fit requirements so returns this message
                    System.out.println(Category.RED.getColour() + errorMsg + "\033[0m");
                }
            }
            exception = false;  //Sets exception to false after every run of the do while loop
            scanner.nextLine(); //Clears scanner obj buffer for next input
        } while (!chosen);
        return choice;
    }
}
