public enum Category {
    //Category enum
    RED("\033[0;31m"), WHITE("\033[0;37m"), BLUE("\033[0;34m"), PURPLE("\033[0;35m"), YELLOW("\033[0;33m"), GREEN("\033[0;32m");

    private String colour;

    //Constructor
    Category(String colour) {
        this.colour = colour;
    }

    //Getter for colour
    public String getColour() {
        return colour;
    }
}
