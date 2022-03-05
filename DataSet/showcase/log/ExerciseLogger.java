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

        Pattern dateFormat = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d");
        Matcher matchFormat = dateFormat.matcher(label);

        if (matchFormat.find()) {

            int month = Integer.parseInt(label.substring(5, 7));
            int day = Integer.parseInt(label.substring(8));

            if (month < 1 || month > 12) {

                throw new Exception("THERE ONLY ARE 12 MONTHS, AND THERE IS NO 0TH MONTH");
            }
            else if (day < 1 || day > 31) {

                throw new Exception("THERE ONLY ARE 28-31 DAYS IN A MONTH, AND THERE IS NO 0TH DAY");
            }
        }
        else {

            throw new Exception("DATE SHOULD BE FORMATTED [YYYY]-[MM]-[DD]");
        }
    }

    // method is used to see if formatting on an exercise is correct
    // throws exception if it isn't
    private void checkFormatExercise(String fragment) throws Exception {

        Pattern format = Pattern.compile("[\\d+]\\*[\\d+] [\\w+] @ [\\d+][\\w*]");
        Matcher matchFormat = format.matcher(fragment);

        if (!matchFormat.find()) {

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

    public void add(String date, String exercise) throws IOException, Exception {

        this.checkFormatDate(date);
        this.checkFormatExercise(exercise);
        this.logFile.add(date, exercise);
    }

    public void add(String date, ArrayList<String> exercises) throws IOException, Exception {

        this.checkFormatDate(date);
        this.checkFormatExercise(exercises);
        this.logFile.add(date, exercises);
    }

    public void remove(String date) throws IOException, Exception {

        this.checkFormatDate(date);
        this.logFile.remove(date);
    }

    public void addTo(String date, String addon) throws IOException, Exception {

        this.checkFormatDate(date);
        this.checkFormatExercise(addon);
        this.logFile.addTo(date, addon);
    }

    // this method is used for getting the data out of a data unit, and displaying it in a tangible way
    // throws exception if something happens while retrieving the data
    public String get(String date) throws Exception {

        ArrayList<String> exercises = this.logFile.get(date);
        StringBuilder state = new StringBuilder();

        state.append(date);
        state.append(":");
        state.append("\n");

        for (int i = 0; i < exercises.size(); i++) {

            if (i == exercises.size() - 1) {

                state.append(exercises.get(i));
            }
            else {

                state.append(exercises.get(i));
                state.append("\n");
            }
        }

        return state.toString();
    }
}
