/* This program is a demonstration of a usecase for the DataSet class where you store exercise logs, and where every log has
 * to be unique.
 *
 * Author @qpeano [created 2022-03-04 | last updated 2022-03-04]
 */

import log.ExerciseLogger;
import data.DataUnit;
import data.DataSet;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;

class Main {

    private static ExerciseLogger log; // contains file with logs
    private static Scanner in; // input device

    // start of program
    public static void main(String[] args) throws IOException, Exception {

        log = new ExerciseLogger("/workspaces/85860293/migration/JAVA/PROJECTS/LOG/logs/loggings.txt");
        in = new Scanner(System.in);

        // asks user to enter a number (1-4)
        System.out.println("GET WORKOUT (1)\nADD WORKOUT (2)\nREMOVE WORKOUT (3)\nADD EXERCISE (4)");
        System.out.println("----------");
        int ans = in.nextInt();
        System.out.println("----------");

        switch (ans) {

            case 1:
                getWorkOut(); // if 1 is chosen, prepare fetch an existing workout
                break;
            case 2:
                addWorkOut(); // if 2 is chosen, prepare to add a new workout
                break;
            case 3:
                removeWorkOut(); // if 3 is chosen, prepare for removal of existing workout
                break;
            case 4:
                addExercise(); // if 4 is chosen, prepare to add exercises to existing workout
                break;
            default:
                System.out.println("[FAULTY INPUT]"); // else, inform user
        }

        in.close();
    }

    // method is used when user wants to retrieve all exercises done in a workout
    // throws exception if date is formatted incorrectly or if retrival fails
    public static void getWorkOut() throws Exception {

        System.out.print("DATE: "); // asks for date
        String date = in.next();
        in.nextLine();
        String state = log.get(date); // gets exercises in a good format
        System.out.println(state); // prints exercises
        System.out.println("----------");
    }

    // method is used when user wants to add a new workout
    // throws exception if date is formatted incorrectly or if something happens while printing exercises to log file
    public static void addWorkOut() throws IOException, Exception {

        System.out.print("DATE: "); // asks for date
        String date = in.next();
        in.nextLine();

        ArrayList<String> exercises = new ArrayList<>(); // list for all exercises
        System.out.print("ENTER EXERCISES (terminate by ONLY pressing enter):\n"); // asks for exercises
        String text = in.nextLine();

        while (!text.equals("")) { // while user has typed out something

            exercises.add(text); // it is checked for incorrect format, and then added to list
            text = in.nextLine();
        }

        log.add(date, exercises); // added in log file
        System.out.println("[OPERATION SUCCESSFUL]");
        System.out.println("----------");
    }

    // method is used when user wants to remove a workout
    // throws exception if date is formatted incorrectly or if workout doesn't exist
    public static void removeWorkOut() throws IOException, Exception {

        System.out.print("DATE: "); // asks for date
        String date = in.next();
        in.nextLine();
        log.remove(date); // removes it from log file
        System.out.println("[OPERATION SUCCESSFUL]");
        System.out.println("----------");
    }

    // method is used when user wants to add an exercise to an existing workout
    // throws exception if date is formatted incorrectly or if something happens while printing exercises to log file
    public static void addExercise() throws IOException, Exception {

        System.out.print("DATE: "); // asks for date
        String date = in.next();
        in.nextLine();

        ArrayList<String> exercises = new ArrayList<>(); // list of new exercises
        System.out.print("ENTER ADDITIONAL EXERCISES (terminate by ONLY pressing enter):\n"); // asks for additional exercises
        String text = in.nextLine();

        while (!text.equals("")) { // while user has typed something

            exercises.add(text); // check for format error and then add to list
            text = in.nextLine();
        }

        for (String s : exercises) { // add every addional exercise to the existing workout

            log.addTo(date, s);
        }

        System.out.println("[OPERATION SUCCESSFUL]");
        System.out.println("----------");
    }
}
