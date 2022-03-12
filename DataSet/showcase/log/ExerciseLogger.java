package log;
import data.DataSet;
import data.DataUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;

public class ExerciseLogger {

    private DataSet logFile; // file with all logs

    /* CONSTRUCTOR */

    // takes path to file
    // throws exception if someting happens while setting up DataSet
    public ExerciseLogger(String pathName) throws IOException {

        this.logFile = new DataSet(pathName);
    }

    /* INTERNAL */

    // method is used to check if date is in right format (yyyy-mm-dd)
    // throws exception if that isn't the case
    private void checkFormatDate(String label) throws Exception {

        Pattern dateFormat = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d"); // format of date
        Matcher matchFormat = dateFormat.matcher(label);

        if (matchFormat.matches()) { // if argument is formatted like the regex above, proceed

            int month = Integer.parseInt(label.substring(5, 7)); // gets month
            int day = Integer.parseInt(label.substring(8)); // gets day

            if (month < 1 || month > 12) { // if given month is greater than 12 or 0, inform user

                throw new Exception("THERE ONLY ARE 12 MONTHS, AND THERE IS NO 0TH MONTH");
            }
            else if (day < 1 || day > 31) { // if given day is greater than 31 or 0, inform user

                throw new Exception("THERE ONLY ARE 28-31 DAYS IN A MONTH, AND THERE IS NO 0TH DAY");
            }
        }
        else { // else, inform user about the accepted format

            throw new Exception("DATE SHOULD BE FORMATTED [YYYY]-[MM]-[DD]");
        }
    }

    // method is used to see if formatting on an exercise is correct
    // throws exception if it isn't
    private void checkFormatExercise(String fragment) throws Exception {

        Pattern format = Pattern.compile("\\d+\\*\\d+ [a-zA-Z_-]+ @\\d+[a-zA-Z]*"); // exercise format
        Matcher matchFormat = format.matcher(fragment);

        if (!matchFormat.matches()) { // if argument isn't in given format, inform user

            throw new Exception("FORMAT OF EXERCISES IS [SETS]*[REPS] [NAME] @ [WEIGHT/INTESITY]");
        }
    }

    // overloaded version of the method above
    // uses it to check an array of strings
    private void checkFormatExercise(ArrayList<String> fragments) throws Exception {

        for (String s : fragments) {

            this.checkFormatExercise(s);
        }
    }

    /* USER INTERFACE */

    // method is used to add a workout with date and an exercise as argument
    // throws exception if formatting is incorrect or if something goes wrong while writing to file
    public void add(String date, String exercise) throws IOException, Exception {

        this.checkFormatDate(date); // checks format of date arg
        this.checkFormatExercise(exercise); // checks format of exercise arg
        this.logFile.add(date, exercise); // adds a unit with date as its label (if label already exists, this is simply ignored)
    }

    // method is overloaded version of the one above, adds a list of exercises instead of one
    // throws exception if formatting is incorrect or if something goes wrong while writing to file
    public void add(String date, ArrayList<String> exercises) throws IOException, Exception {

        this.checkFormatDate(date); // checks format of date arg
        this.checkFormatExercise(exercises); // checks format of exercises arg
        this.logFile.add(date, exercises); // adds a unit with date as its label (if label already exists, this is simply ignored)
    }

    // method is used to remove an workout using a date argument to find it
    // throws exception if formatting is incorrect or if workout with specified date doesn't exist
    public void remove(String date) throws IOException, Exception {

        this.checkFormatDate(date); // checks format of date arg
        this.logFile.remove(date); // tries to remove workout
    }

    // method is used to add an exercise to an exsisting workout using date as argument to find it
    // throws exception if formatting is incorrect or if workout with specified date doesn't exist
    public void addTo(String date, String addon) throws IOException, Exception {

        this.checkFormatDate(date); // checks format of date arg
        this.checkFormatExercise(addon); // checks format of additional exercise arg
        this.logFile.addTo(date, addon); // tries to add new fragment to unit with specified label
    }

    // this method is used for getting the data out of a data unit, and displaying it in a tangible way
    // throws exception if something happens while retrieving the data
    public String get(String date) throws Exception {

        ArrayList<String> exercises = this.logFile.get(date); // gets all exercises of a workout
        StringBuilder state = new StringBuilder(); // used because it is faster than string concat

        for (int i = 0; i < exercises.size(); i++) { // adds all exercises to state

            if (i == exercises.size() - 1) {

                state.append(exercises.get(i));
            }
            else {

                state.append(exercises.get(i));
                state.append("\n");
            }
        }

        return state.toString(); // returns state
    }
}
