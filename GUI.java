//Imports
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

//GUI Class
public class GUI extends JFrame implements ListSelectionListener, ActionListener {
    //Declarations
    ArrayList<Todo> Todo = new ArrayList<>();
    private JFrame frm;
    private int width, height, updateIndex, updateType;
    private JButton Add, Update, Delete;
    private JButton Low, Normal, High;
    private JButton Pending, Started, Partial, Completed;
    private JButton Red, White, Blue, Purple, Yellow, Green;
    private JButton titleBtn, dueDateBtn, categoryBtn, importanceBtn, completionBtn;
    private JButton Ok, Cancel;
    private JButton importListBtn, importBtn, saveBtn;
    private JPanel p1, p2, p3, p4, p5;
    private JPanel cSelect, iSelect, sSelect, menuSelect, oSelect, updateSelect, loadSelect;
    private JLabel prompt, errorMsg, selectedTodo, saveTodo;
    private JList itemList;
    private DefaultListModel Todos;
    private JTextField textField;
    private JScrollPane itemsScroll;
    private Boolean add1, add2, exception, updateSet;
    private String TodoTitle;
    private LocalDateTime TodoDate;
    private String choice = "";
    private String enterTitle = "Enter Title For The Todo:";
    private String enterDueDate = "Enter A Due Date For The Todo In The Format (YYYY-MM-DDTHH:MM)";
    private String selectCategory = "Select A Category For The Todo:";
    private String selectImportance = "Select An Importance For The Todo:";
    private String dueDateErrorMsg = "Sorry, please enter date in the correct format.";
    private String noTodoSelected = "Select a Todo";
    private String emptyInput = "Sorry, your input cannot be empty.";
    private String confirmDelete = "Are you sure you want to delete Todo ";
    private String selectStatus = "Select Status";
    private String youHaveSelected = "You have selected: ";
    private String noFileToSave = "No Todos to save.";
    private String csvImported = "Todos Already Imported";
    private Category cat, listSelectCat;
    private Importance imp;
    private Status stat;
    private Todo toDoObj, listSelectTodo;

    public GUI(int w, int h) {
        //GUI Constructor
        //Initialize JFrame
        frm = new JFrame();
        width = w;
        height = h;
        //Initialize JPanels
        p1 = new JPanel();
        p2 = new JPanel();
        p3 = new JPanel();
        p4 = new JPanel();
        p5 = new JPanel();
        cSelect = new JPanel();
        iSelect = new JPanel();
        sSelect = new JPanel();
        menuSelect = new JPanel();
        oSelect = new JPanel();
        updateSelect = new JPanel();
        loadSelect = new JPanel();
        //Initialize JLabels
        prompt = new JLabel(enterTitle);
        errorMsg = new JLabel(dueDateErrorMsg);
        selectedTodo = new JLabel(noTodoSelected);
        saveTodo = new JLabel(noFileToSave);
        //Initialize JTextFields
        textField = new JTextField();
        //Initialize buttons
        categories(cSelect);
        importance(iSelect);
        status(sSelect);
        menu(menuSelect);
        options(oSelect);
        update(updateSelect);
        load(loadSelect);
        //Initialize Default List Model
        Todos = new DefaultListModel();
        //Initialize JList
        itemList = new JList(Todos);
        //Add list selection listener to itemList
        itemList.addListSelectionListener(this);
        //Initialize JScrollPane
        itemsScroll = new JScrollPane(itemList);
        itemsScroll.setMaximumSize(new Dimension(300, 200));
        //Sets GUI Layout/Colours for JPanels
        GUILayout();
        //Adds components to JPanels, and adds JPanels to JFrame
        addComponents();
        //Sets the startup state
        states(0);
    }

    public void states(int state) {
        switch (state) {
            //Startup State
            case 0 -> {
                //Hide Text Field/Labels
                textField.setVisible(false);
                prompt.setVisible(false);
                errorMsg.setVisible(false);

                //Hide Category Buttons/Panel
                Red.setVisible(false);
                White.setVisible(false);
                Blue.setVisible(false);
                Purple.setVisible(false);
                Yellow.setVisible(false);
                Green.setVisible(false);
                cSelect.setVisible(false);

                //Hide Importance Buttons/Panel
                Low.setVisible(false);
                Normal.setVisible(false);
                High.setVisible(false);
                iSelect.setVisible(false);

                //Hide Ok/Cancel Buttons/Panel
                Ok.setVisible(false);
                Cancel.setVisible(false);
                oSelect.setVisible(false);

                //Hide Status Buttons/Panel
                Pending.setVisible(false);
                Started.setVisible(false);
                Partial.setVisible(false);
                Completed.setVisible(false);
                sSelect.setVisible(false);

                //Hide Update Buttons/Panel
                titleBtn.setVisible(false);
                dueDateBtn.setVisible(false);
                categoryBtn.setVisible(false);
                importanceBtn.setVisible(false);
                completionBtn.setVisible(false);
                updateSelect.setVisible(false);

                //Hide import stuff on startup
                loadSelect.setVisible(false);
                saveTodo.setVisible(false);

                //Show On Startup
                Add.setVisible(true);
                Update.setVisible(true);
                Delete.setVisible(true);
                menuSelect.setVisible(true);
                importListBtn.setVisible(true);

                //Set Values On Startup
                prompt.setText(enterTitle);
                textField.setText("");
                selectedTodo.setForeground(Color.WHITE);
                selectedTodo.setText(noTodoSelected);
                updateSet = false;
                TodoTitle = "";
                TodoDate = null;
                cat = null;
                imp = null;
                stat = null;
                add1 = false;
                add2 = false;
                toDoObj = null;
                listSelectTodo = null;
                itemList.clearSelection();

                //Render JFrame
                frm.setVisible(true);
            }
            //Show Add Title Components
            case 1 -> {
                //Hide Menu Buttons/Panel
                Add.setVisible(false);
                Update.setVisible(false);
                Delete.setVisible(false);
                menuSelect.setVisible(false);

                //Show Option Buttons/Panel
                Ok.setVisible(true);
                Cancel.setVisible(true);
                oSelect.setVisible(true);

                //Show Text Field & Prompt
                textField.setVisible(true);
                prompt.setVisible(true);

                //Hide import panel
                loadSelect.setVisible(false);

                add1 = true;    //(Represents the first stage of adding a todo)
                //Render JFrame
                frm.setVisible(true);
            }
            //Title Entered So Show Add Due Date Components
            case 2 -> {
                //Set Prompt to Enter Due Date Prompt
                prompt.setText(enterDueDate);
                //Empty Text Field
                textField.setText("");
                //Set error message for Due Date
                errorMsg.setText(dueDateErrorMsg);
                errorMsg.setVisible(false);
                add1 = false;   //Stops the title entering event from being run.
                add2 = true;    //(Represents the second stage of adding a todo)
            }
            //Due Date Not In Correct Format Show Error Msg
            case 3 -> {
                //Show error message label
                errorMsg.setVisible(true);
                errorMsg.setText(dueDateErrorMsg);
                //Render JFrame
                frm.setVisible(true);
                exception = true;   //Marks exception as true
            }
            //Due Date Correct Format So Show Select Category Components
            case 4 -> {
                //Hide Error Message & Text Field
                errorMsg.setVisible(false);
                textField.setVisible(false);

                //Show Category Buttons/Panel
                Red.setVisible(true);
                White.setVisible(true);
                Blue.setVisible(true);
                Purple.setVisible(true);
                Yellow.setVisible(true);
                Green.setVisible(true);
                cSelect.setVisible(true);

                //Hide Ok button
                Ok.setVisible(false);
                //Set prompt to Category Picker Text
                prompt.setText(selectCategory);
                add2 = false;   //Stops the due date entering event from being run.
            }
            //Category Entered So Show Select Importance Components
            case 5 -> {
                //Hide Category Buttons/Panel
                Red.setVisible(false);
                White.setVisible(false);
                Blue.setVisible(false);
                Purple.setVisible(false);
                Yellow.setVisible(false);
                Green.setVisible(false);
                cSelect.setVisible(false);

                //Show Importance Buttons/Panel
                Low.setVisible(true);
                Normal.setVisible(true);
                High.setVisible(true);
                iSelect.setVisible(true);

                //Set prompt to select importance text
                prompt.setText(selectImportance);
                //Render JFrame
                frm.setVisible(true);
            }
            //Importance Entered So Add Todo
            case 6 -> {
                //Sets the status to Pending by default every time Todo is added
                stat = Status.PENDING;
                //Creates Todo Object
                toDoObj = new Todo(TodoTitle, TodoDate, cat, imp, stat);
                //Add Obj to Array List
                Todo.add(toDoObj);
            }
            //Formats Todo's in JList/DefaultListModel
            case 7 -> {
                //Clears default list model
                Todos.clear();
                //Loops through and adds the Todos to ListModel
                for (int i = 0; i < Todo.size(); i++) {
                    Todos.add(i, Todo.get(i));
                }
            }
            //Get Cat Colour
            case 8 -> {
                listSelectTodo = Todo.get(itemList.getSelectedIndex());
                listSelectCat = listSelectTodo.getCat();
                if (listSelectCat == Category.WHITE) {
                    selectedTodo.setForeground(Color.WHITE);
                } else if (listSelectCat == Category.RED) {
                    selectedTodo.setForeground(Color.RED);
                } else if (listSelectCat == Category.BLUE) {
                    selectedTodo.setForeground(Color.BLUE);
                } else if (listSelectCat == Category.PURPLE) {
                    selectedTodo.setForeground(new Color(102, 0, 153));
                } else if (listSelectCat == Category.YELLOW) {
                    selectedTodo.setForeground(Color.YELLOW);
                } else if (listSelectCat == Category.GREEN) {
                    selectedTodo.setForeground(Color.GREEN);
                }
                listSelectCat = null;
                listSelectTodo = null;
            }
            //Hide update Buttons
            case 9 -> {
                titleBtn.setVisible(false);
                dueDateBtn.setVisible(false);
                importanceBtn.setVisible(false);
                categoryBtn.setVisible(false);
                completionBtn.setVisible(false);
            }
            //Get update type
            case 10 -> {
                switch (updateType) {
                    //Update title
                    case 1 -> {
                        listSelectTodo = Todo.get(updateIndex);
                        TodoTitle = textField.getText();
                        TodoDate = listSelectTodo.getDue();
                        cat = listSelectTodo.getCat();
                        imp = listSelectTodo.getImportance();
                        stat = listSelectTodo.getCompletion();
                    }
                    //Update Due Date
                    case 2 -> {
                        listSelectTodo = Todo.get(updateIndex);
                        TodoTitle = listSelectTodo.getText();
                        cat = listSelectTodo.getCat();
                        imp = listSelectTodo.getImportance();
                        stat = listSelectTodo.getCompletion();
                    }
                    //Update Cat
                    case 3 -> {
                        listSelectTodo = Todo.get(updateIndex);
                        TodoTitle = listSelectTodo.getText();
                        TodoDate = listSelectTodo.getDue();
                        imp = listSelectTodo.getImportance();
                        stat = listSelectTodo.getCompletion();
                    }
                    //Update Importance
                    case 4 -> {
                        listSelectTodo = Todo.get(updateIndex);
                        TodoTitle = listSelectTodo.getText();
                        TodoDate = listSelectTodo.getDue();
                        cat = listSelectTodo.getCat();
                        stat = listSelectTodo.getCompletion();
                    }
                    //Update Completion
                    case 5 -> {
                        listSelectTodo = Todo.get(updateIndex);
                        TodoTitle = listSelectTodo.getText();
                        TodoDate = listSelectTodo.getDue();
                        imp = listSelectTodo.getImportance();
                        cat = listSelectTodo.getCat();
                    }
                }
            }
            //Set rest of updated todo
            case 11 -> {
                toDoObj = new Todo(TodoTitle, TodoDate, cat, imp, stat);
                Todo.remove(updateIndex);
                Todo.add(updateIndex, toDoObj);
                //Clears default list model
                Todos.clear();
                //Loops through and adds the Todos to ListModel
                for (int i = 0; i < Todo.size(); i++) {
                    Todos.add(i, Todo.get(i));
                }
            }
            //Show Completion Components For Update
            case 12 -> {
                //Show Completion Buttons/Panel
                Pending.setVisible(true);
                Started.setVisible(true);
                Partial.setVisible(true);
                Completed.setVisible(true);
                sSelect.setVisible(true);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        //Add Button Clicked
        if (e.getSource() == Add) {
            states(1);
        }

        //Cancel Button Clicked
        if (e.getSource() == Cancel) {
            states(0);
        }

        //Ok Button Clicked From Add Title State
        if (e.getSource() == Ok && add1 && !updateSet) {
            TodoTitle = textField.getText();
            if (!TodoTitle.equals("")) {
                errorMsg.setVisible(false);
                states(2);
            } else if (TodoTitle.equals("")) {
                errorMsg.setVisible(true);
                errorMsg.setText(emptyInput);
            }
        }

        //Ok Button Clicked From Add Due Date State
        if (e.getSource() == Ok && add2 && !add1 && !updateSet) {
            choice = textField.getText();
            if (!choice.equals("")) {
                try {
                    TodoDate = LocalDateTime.parse(textField.getText());
                } catch (Exception ex) {
                    states(3);
                }
                if (!exception) {
                    states(4);
                    errorMsg.setVisible(false);
                }
            }
            exception = false;
        }

        //Red Category Selected
        if (e.getSource() == Red) {
            cat = Category.RED;
            states(5);
        }

        //White Category Selected
        if (e.getSource() == White) {
            cat = Category.WHITE;
            states(5);
        }

        //Blue Category Selected
        if (e.getSource() == Blue) {
            cat = Category.BLUE;
            states(5);
        }

        //Purple Category Selected
        if (e.getSource() == Purple) {
            cat = Category.PURPLE;
            states(5);
        }

        //Yellow Category Selected
        if (e.getSource() == Yellow) {
            cat = Category.YELLOW;
            states(5);
        }

        //Green Category Selected
        if (e.getSource() == Green) {
            cat = Category.GREEN;
            states(5);
        }

        //Low Importance Selected
        if (e.getSource() == Low && !updateSet) {
            itemList.clearSelection();
            imp = Importance.LOW;
            //Adds Todo to Array List
            states(6);
            //Updates JList
            states(7);
            //Resets Todo Obj
            toDoObj = null;
            //Reverts back to startup state
            states(0);
        }

        //Normal Importance Selected
        if (e.getSource() == Normal && !updateSet) {
            itemList.clearSelection();
            imp = Importance.NORMAL;
            //Adds Todo to Array List
            states(6);
            //Updates JList
            states(7);
            //Resets Todo Obj
            toDoObj = null;
            //Reverts back to startup state
            states(0);
        }

        //High Importance Selected
        if (e.getSource() == High && !updateSet) {
            itemList.clearSelection();
            imp = Importance.HIGH;
            //Adds Todo to Array List
            states(6);
            //Updates JList
            states(7);
            //Resets Todo Obj
            toDoObj = null;
            //Reverts back to startup state
            states(0);
        }

        //Delete todo Clicked
        if (e.getSource() == Delete) {
            if (itemList.getSelectedValue() != null) {
                //Hide import panel
                loadSelect.setVisible(false);
                int response = JOptionPane.showConfirmDialog(null, confirmDelete + (itemList.getSelectedIndex() + 1) + " ?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    //Delete todo from array list
                    Todo.remove(itemList.getSelectedIndex());
                    Todos.remove(itemList.getSelectedIndex());
                    selectedTodo.setText(noTodoSelected);
                    selectedTodo.setForeground(Color.WHITE);
                }
            }
        }

        //Update todo Clicked
        if (e.getSource() == Update) {
            if (itemList.getSelectedValue() != null) {
                updateIndex = itemList.getSelectedIndex();
                titleBtn.setVisible(true);
                dueDateBtn.setVisible(true);
                categoryBtn.setVisible(true);
                importanceBtn.setVisible(true);
                completionBtn.setVisible(true);
                updateSelect.setVisible(true);
                Cancel.setVisible(true);
                oSelect.setVisible(true);
                Add.setVisible(false);
                Update.setVisible(false);
                Delete.setVisible(false);
                menuSelect.setVisible(false);
                loadSelect.setVisible(false);
            }
            frm.setVisible(true);
        }

        //Update title selected
        if (e.getSource() == titleBtn) {
            updateSet = true;
            updateType = 1;
            states(9);
            states(1);
        }

        //Update title confirmed
        if (e.getSource() == Ok && updateSet && updateType == 1) {
            choice = textField.getText();
            if (!choice.equals("")) {
                //Update title
                errorMsg.setVisible(false);
                states(10);
                states(11);
                states(0);
            } else {
                //Error msg
                errorMsg.setVisible(true);
                errorMsg.setText(emptyInput);
            }
        }

        //Update Due Date Selected
        if (e.getSource() == dueDateBtn) {
            updateSet = true;
            updateType = 2;
            textField.setVisible(true);
            prompt.setVisible(true);
            Ok.setVisible(true);
            states(9);
            states(2);
        }

        //Update Due Date Confirmed
        if (e.getSource() == Ok && updateSet && updateType == 2) {
            choice = textField.getText();
            if (!choice.equals("")) {
                try {
                    TodoDate = LocalDateTime.parse(textField.getText());
                } catch (Exception ex) {
                    states(3);
                }
                if (!exception) {
                    states(10);
                    states(11);
                    states(0);
                }
            }
            exception = false;
        }

        //Update Category Picked
        if (e.getSource() == categoryBtn) {
            updateSet = true;
            updateType = 3;
            states(9);
            states(4);
        }

        //Update Importance Picked
        if (e.getSource() == importanceBtn) {
            updateSet = true;
            updateType = 4;
            prompt.setVisible(true);
            states(9);
            states(5);
        }

        //Update Completion Picked
        if (e.getSource() == completionBtn) {
            prompt.setText(selectStatus);
            updateSet = true;
            updateType = 5;
            prompt.setVisible(true);
            states(9);
            states(12);
        }

        //Low Importance Picked For Update
        if (e.getSource() == Low && updateSet) {
            imp = Importance.LOW;
            states(10);
            states(11);
            states(0);
        }

        //Normal Importance Picked For Update
        if (e.getSource() == Normal && updateSet) {
            imp = Importance.NORMAL;
            states(10);
            states(11);
            states(0);
        }

        //High Importance Picked For Update
        if (e.getSource() == High && updateSet) {
            imp = Importance.HIGH;
            states(10);
            states(11);
            states(0);
        }

        //Red Category Selected For Update
        if (e.getSource() == Red && updateSet) {
            cat = Category.RED;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //White Category Selected For Update
        if (e.getSource() == White && updateSet) {
            cat = Category.WHITE;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Blue Category Selected For Update
        if (e.getSource() == Blue && updateSet) {
            cat = Category.BLUE;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Purple Category Selected For Update
        if (e.getSource() == Purple && updateSet) {
            cat = Category.PURPLE;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Yellow Category Selected For Update
        if (e.getSource() == Yellow && updateSet) {
            cat = Category.YELLOW;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Green Category Selected For Update
        if (e.getSource() == Green && updateSet) {
            cat = Category.GREEN;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Pending Completion Selected For Update
        if (e.getSource() == Pending && updateSet) {
            stat = Status.PENDING;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Started Completion Selected For Update
        if (e.getSource() == Started && updateSet) {
            stat = Status.STARTED;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Partial Completion Selected For Update
        if (e.getSource() == Partial && updateSet) {
            stat = Status.PARTIAL;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //Completed Completion Selected For Update
        if (e.getSource() == Completed && updateSet) {
            stat = Status.COMPLETED;
            states(9);
            states(10);
            states(11);
            states(0);
        }

        //If import List Btn Selected
        if (e.getSource() == importListBtn) {
            if (loadSelect.isVisible()) {
                loadSelect.setVisible(false);
            } else {
                loadSelect.setVisible(true);
            }
        }

        //If import Btn selected
        if (e.getSource() == importBtn) {
            if (Todo.size() == 0) {
                //Import csv
                try {
                    saveTodo.setVisible(false);
                    csvReader();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                //Todos already in array list so don't fill
                saveTodo.setText(csvImported);
                saveTodo.setVisible(true);
            }
        }

        //If save Btn selected
        if (e.getSource() == saveBtn) {
            if (Todo.size() == 0) {
                //Do nothing
                saveTodo.setText(noFileToSave);
                saveTodo.setVisible(true);
            } else {
                try {
                    saveTodo.setVisible(false);
                    save();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    public void csvReader() throws IOException {
        //Loads CSV File to fill Jlist and array list
        String path = "D:\\Todo.csv";
        String line = "";
        int todoCount = 0;
        int x = 0;
        String [] values;
        String thisTodo = null;
        String CSVTodoTitle;
        LocalDateTime CSVTodoDate;
        Importance CSVimp;
        Status CSVstat;
        Category CSVcat = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                //splits the todos by "," and by new line
                values = line.split(",");
                thisTodo = values[x];
                thisTodo = thisTodo.replaceAll("Due:", ",");
                thisTodo = thisTodo.replaceAll("Priority:", ",");
                thisTodo = thisTodo.replaceAll("Completion:", ",");
                thisTodo = thisTodo.replaceAll("Category:", ",");
                //Replaces all labels with commas
                thisTodo = thisTodo.replaceAll("\\[", "").replaceAll("\\]", "");
                //replaces all brackets with nothing
                String[] arrOfStr = thisTodo.split(",");
                //splits the string by commas into string array

                //fills each part of the todo before adding it
                CSVTodoTitle = arrOfStr[0];
                CSVTodoDate = LocalDateTime.parse(arrOfStr[1]);
                if (arrOfStr[2].equals("HIGH")) {
                    CSVimp = Importance.HIGH;
                } else if (arrOfStr[2].equals("NORMAL")) {
                    CSVimp = Importance.NORMAL;
                } else {
                    CSVimp = Importance.LOW;
                }
                if (arrOfStr[3].equals("PENDING")) {
                    CSVstat = Status.PENDING;
                } else if (arrOfStr[3].equals("STARTED")) {
                    CSVstat = Status.STARTED;
                } else if (arrOfStr[3].equals("PARTIAL")) {
                    CSVstat = Status.PARTIAL;
                } else {
                    CSVstat = Status.COMPLETED;
                }
                if (arrOfStr[4].equals("RED")) {
                    CSVcat = Category.RED;
                } else if (arrOfStr[4].equals("WHITE")) {
                    CSVcat = Category.WHITE;
                } else if (arrOfStr[4].equals("BLUE")) {
                    CSVcat = Category.BLUE;
                } else if (arrOfStr[4].equals("PURPLE")) {
                    CSVcat = Category.PURPLE;
                } else if (arrOfStr[4].equals("YELLOW")) {
                    CSVcat = Category.YELLOW;
                } else if (arrOfStr[4].equals("GREEN")) {
                    CSVcat = Category.GREEN;
                }
                //Creates Todo Object
                toDoObj = new Todo(CSVTodoTitle, CSVTodoDate, CSVcat, CSVimp, CSVstat);
                //Add Obj to Array List
                Todo.add(toDoObj);
                Todos.clear();
                //Loops through and adds the Todos to ListModel
                for (int i = 0; i < Todo.size(); i++) {
                    Todos.add(i, Todo.get(i));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        //Saves file to csv file
        String input = Todo.toString();
        input = input.substring(1, input.length() - 1); // get rid of brackets
        input = input.replaceAll(",", "//////");
        input = input.replaceAll("\\s+", "");
        input = input.replaceAll("//////", ",\n");
        String[] split = input.split(" ");
        FileWriter writer = new FileWriter("/Todo.csv");

        for(String s : split) {
            String[] split2 = s.split(",");
            writer.write(Arrays.asList(split2).stream().collect(Collectors.joining(",")));
            writer.write("\n"); // newline
        }
        writer.close();
    }

    public void GUILayout() {
        //Sets JPanel layouts and background colours
        p1.setLayout(new BorderLayout(15, 15));
        p2.setLayout(new BorderLayout(15, 15));
        p3.setLayout(new BorderLayout(15, 15));
        p4.setLayout(new BorderLayout(15, 15));
        p5.setLayout(new BorderLayout(15, 15));
        cSelect.setLayout(new FlowLayout());
        iSelect.setLayout(new FlowLayout());
        sSelect.setLayout(new FlowLayout());
        menuSelect.setLayout(new FlowLayout());
        oSelect.setLayout(new FlowLayout());
        updateSelect.setLayout(new FlowLayout());
        loadSelect.setLayout(new FlowLayout());
        prompt.setHorizontalAlignment(SwingConstants.CENTER);
        prompt.setForeground(Color.WHITE);
        prompt.setFont(new Font("Dialog", Font.BOLD, 13));
        errorMsg.setHorizontalAlignment(SwingConstants.CENTER);
        errorMsg.setForeground(Color.RED);
        errorMsg.setFont(new Font("Dialog", Font.BOLD, 13));
        saveTodo.setHorizontalAlignment(SwingConstants.CENTER);
        saveTodo.setForeground(Color.RED);
        saveTodo.setFont(new Font("Dialog", Font.BOLD, 13));
        p4.setBackground(Color.DARK_GRAY);
        p1.setBackground(Color.DARK_GRAY);
        p2.setBackground(Color.DARK_GRAY);
        p3.setBackground(Color.DARK_GRAY);
        p5.setBackground(Color.DARK_GRAY);
        itemList.setBackground(Color.decode("#1c2237"));
        itemList.setForeground(Color.WHITE);
        cSelect.setBackground(Color.DARK_GRAY);
        iSelect.setBackground(Color.DARK_GRAY);
        sSelect.setBackground(Color.DARK_GRAY);
        menuSelect.setBackground(Color.DARK_GRAY);
        oSelect.setBackground(Color.DARK_GRAY);
        updateSelect.setBackground(Color.DARK_GRAY);
        itemsScroll.setBackground(Color.DARK_GRAY);
        loadSelect.setBackground(Color.DARK_GRAY);
        selectedTodo.setForeground(Color.WHITE);
    }

    public void setUpGUI() {
        //Sets up GUI JFrame
        frm.setSize(width, height);
        frm.setTitle("Todo List");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setLocation(200, 200);
        ImageIcon img = new ImageIcon("shrek.png");
        frm.setIconImage(img.getImage());
        frm.setVisible(true);
    }

    public void addComponents() {
        //Arrange components to JPanels, and adds JPanels to JFrame
        p5.add(sSelect, BorderLayout.CENTER);
        p5.add(cSelect, BorderLayout.SOUTH);

        p4.add(p5, BorderLayout.NORTH);
        p4.add(oSelect, BorderLayout.CENTER);
        p4.add(selectedTodo, BorderLayout.SOUTH);

        p3.add(textField, BorderLayout.NORTH);
        p3.add(menuSelect, BorderLayout.SOUTH);
        p3.add(iSelect, BorderLayout.CENTER);

        p2.add(prompt, BorderLayout.NORTH);
        p2.add(errorMsg, BorderLayout.CENTER);
        p2.add(updateSelect, BorderLayout.SOUTH);

        p1.add(p2, BorderLayout.NORTH);
        p1.add(p3, BorderLayout.CENTER);
        p1.add(p4, BorderLayout.SOUTH);

        frm.add(itemsScroll, BorderLayout.CENTER);
        frm.add(loadSelect, BorderLayout.NORTH);
        frm.add(p1, BorderLayout.SOUTH);
    }

    public void valueChanged(ListSelectionEvent e) {
        //Gets selected value from list
        if (!updateSet) {
            if ((itemList.getSelectedIndex() + 1) > 0) {
                states(8);
                selectedTodo.setText(youHaveSelected + (itemList.getSelectedIndex() + 1));
            }
        }
    }

    public void menu(JPanel menuSelect) {
        //Sets up Menu buttons for menuSelect JPanel
        Add = new JButton("Add Todo");
        Update = new JButton("Update Todo");
        Delete = new JButton("Delete Todo");
        importListBtn = new JButton("Import List");
        Add.addActionListener(this);
        Update.addActionListener(this);
        Delete.addActionListener(this);
        importListBtn.addActionListener(this);
        menuSelect.add(Add);
        menuSelect.add(Update);
        menuSelect.add(Delete);
        menuSelect.add(importListBtn);
        Add.setFocusable(false);
        Add.setBackground(Color.decode("#848482"));
        Add.setFont(new Font("Dialog", Font.BOLD, 13));
        Update.setFocusable(false);
        Update.setBackground(Color.decode("#848482"));
        Update.setFont(new Font("Dialog", Font.BOLD, 13));
        Delete.setFocusable(false);
        Delete.setBackground(Color.decode("#848482"));
        Delete.setFont(new Font("Dialog", Font.BOLD, 13));
        importListBtn.setFocusable(false);
        importListBtn.setBackground(Color.decode("#848482"));
        importListBtn.setFont(new Font("Dialog", Font.BOLD, 13));
    }

    public void categories(JPanel cSelect) {
        //Sets up Categories buttons for cSelect JPanel
        Red = new JButton("Red");
        White = new JButton("White");
        Blue = new JButton("Blue");
        Purple = new JButton("Purple");
        Yellow = new JButton("Yellow");
        Green = new JButton("Green");
        Red.addActionListener(this);
        White.addActionListener(this);
        Blue.addActionListener(this);
        Purple.addActionListener(this);
        Yellow.addActionListener(this);
        Green.addActionListener(this);
        cSelect.add(Red);
        cSelect.add(White);
        cSelect.add(Blue);
        cSelect.add(Purple);
        cSelect.add(Yellow);
        cSelect.add(Green);
        Red.setFocusable(false);
        Red.setBackground(Color.RED);
        Red.setFont(new Font("Arial", Font.BOLD, 13));
        White.setFocusable(false);
        White.setBackground(Color.WHITE);
        White.setFont(new Font("Dialog", Font.BOLD, 13));
        White.setForeground(Color.BLACK);
        Blue.setFocusable(false);
        Blue.setBackground(Color.BLUE);
        Blue.setFont(new Font("Dialog", Font.BOLD, 13));
        Purple.setFocusable(false);
        Purple.setBackground(new Color(102, 0, 153));
        Purple.setFont(new Font("Dialog", Font.BOLD, 13));
        Yellow.setFocusable(false);
        Yellow.setBackground(Color.YELLOW);
        Yellow.setFont(new Font("Dialog", Font.BOLD, 13));
        Green.setFocusable(false);
        Green.setBackground(Color.GREEN);
        Green.setFont(new Font("Dialog", Font.BOLD, 13));
    }

    public void importance(JPanel iSelect) {
        //Sets up Importance buttons for iSelect JPanel
        Low = new JButton("Low");
        Normal = new JButton("Normal");
        High = new JButton("High");
        Low.addActionListener(this);
        Normal.addActionListener(this);
        High.addActionListener(this);
        iSelect.add(Low);
        iSelect.add(Normal);
        iSelect.add(High);
        Low.setFocusable(false);
        Low.setBackground(Color.decode("#848482"));
        Low.setFont(new Font("Dialog", Font.BOLD, 13));
        Normal.setFocusable(false);
        Normal.setBackground(Color.decode("#848482"));
        Normal.setFont(new Font("Dialog", Font.BOLD, 13));
        High.setFocusable(false);
        High.setBackground(Color.decode("#848482"));
        High.setFont(new Font("Dialog", Font.BOLD, 13));
    }

    public void status(JPanel sSelect) {
        //Sets up Status buttons for sSelect JPanel
        Pending = new JButton("Pending");
        Started = new JButton("Started");
        Partial = new JButton("Partial");
        Completed = new JButton("Completed");
        Pending.addActionListener(this);
        Started.addActionListener(this);
        Partial.addActionListener(this);
        Completed.addActionListener(this);
        sSelect.add(Pending);
        sSelect.add(Started);
        sSelect.add(Partial);
        sSelect.add(Completed);
        Pending.setFocusable(false);
        Pending.setBackground(Color.decode("#848482"));
        Pending.setFont(new Font("Dialog", Font.BOLD, 13));
        Started.setFocusable(false);
        Started.setBackground(Color.decode("#848482"));
        Started.setFont(new Font("Dialog", Font.BOLD, 13));
        Partial.setFocusable(false);
        Partial.setBackground(Color.decode("#848482"));
        Partial.setFont(new Font("Dialog", Font.BOLD, 13));
        Completed.setFocusable(false);
        Completed.setBackground(Color.decode("#848482"));
        Completed.setFont(new Font("Dialog", Font.BOLD, 13));
    }

    public void options(JPanel oSelect) {
        //Sets up Options buttons for oSelect JPanel
        Ok = new JButton("Ok");
        Cancel = new JButton("Cancel");
        Ok.addActionListener(this);
        Cancel.addActionListener(this);
        oSelect.add(Ok);
        oSelect.add(Cancel);
        Ok.setFocusable(false);
        Ok.setBackground(Color.decode("#848482"));
        Ok.setFont(new Font("Dialog", Font.BOLD, 13));
        Cancel.setFocusable(false);
        Cancel.setBackground(Color.decode("#848482"));
        Cancel.setFont(new Font("Dialog", Font.BOLD, 13));
    }

    public void update(JPanel updateSelect) {
        //Sets up Update buttons for updateSelect JPanel
        titleBtn = new JButton("Title");
        dueDateBtn = new JButton("Due Date");
        categoryBtn = new JButton("Category");
        importanceBtn = new JButton("Importance");
        completionBtn = new JButton("Completion");
        titleBtn.addActionListener(this);
        dueDateBtn.addActionListener(this);
        categoryBtn.addActionListener(this);
        importanceBtn.addActionListener(this);
        completionBtn.addActionListener(this);
        updateSelect.add(titleBtn);
        updateSelect.add(dueDateBtn);
        updateSelect.add(categoryBtn);
        updateSelect.add(importanceBtn);
        updateSelect.add(completionBtn);
        titleBtn.setFocusable(false);
        titleBtn.setBackground(Color.decode("#848482"));
        titleBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        dueDateBtn.setFocusable(false);
        dueDateBtn.setBackground(Color.decode("#848482"));
        dueDateBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        categoryBtn.setFocusable(false);
        categoryBtn.setBackground(Color.decode("#848482"));
        categoryBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        importanceBtn.setFocusable(false);
        importanceBtn.setBackground(Color.decode("#848482"));
        importanceBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        completionBtn.setFocusable(false);
        completionBtn.setBackground(Color.decode("#848482"));
        completionBtn.setFont(new Font("Dialog", Font.BOLD, 13));
    }

    public void load(JPanel loadSelect) {
        //Sets up Load buttons for loadSelect JPanel
        importBtn = new JButton("Import");
        saveBtn = new JButton("Save");
        importBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        loadSelect.add(importBtn);
        loadSelect.add(saveBtn);
        loadSelect.add(saveTodo, FlowLayout.RIGHT);
        importBtn.setFocusable(false);
        importBtn.setBackground(Color.decode("#848482"));
        importBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        saveBtn.setFocusable(false);
        saveBtn.setBackground(Color.decode("#848482"));
        saveBtn.setFont(new Font("Dialog", Font.BOLD, 13));
    }
}
