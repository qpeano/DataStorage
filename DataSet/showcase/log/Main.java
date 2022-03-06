import log.ExerciseLogger;
import data.DataUnit;
import data.DataSet;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;

class Main {

    private static ExerciseLogger log;
    private static Scanner in;

    public static void main(String[] args) throws IOException, Exception {

        log = new ExerciseLogger("loggings.txt");
        in = new Scanner(System.in);

        System.out.println("GET WORKOUT (1)\nADD WORKOUT (2)\nREMOVE WORKOUT (3)\nADD EXERCISE (4)");
        System.out.println("----------");
        int ans = in.nextInt();
        System.out.println("----------");

        switch (ans) {

            case 1:
                getWorkOut();
                break;
            case 2:
                addWorkOut();
                break;
            case 3:
                removeWorkOut();
                break;
            case 4:
                addExercise();
                break;
            default:
                System.out.println("[FAULTY INPUT]");
        }

        in.close();
    }

    public static void getWorkOut() throws Exception {

        System.out.print("DATE: ");
        String date = in.next();
        in.nextLine();
        String state = log.get(date);
        System.out.println(state);
        System.out.println("----------");
    }

    public static void addWorkOut() throws IOException, Exception {

        System.out.print("DATE: ");
        String date = in.next();
        in.nextLine();

        ArrayList<String> exercises = new ArrayList<>();
        System.out.print("ENTER EXERCISES (terminate by ONLY pressing enter):\n");
        String text = in.nextLine();

        while (text != "") {

            exercises.add(text);
            text = in.nextLine();
        }

        log.add(date, exercises);
        System.out.println("OPERATION SUCCESSFUL");
        System.out.println("----------");
    }

    public static void removeWorkOut() throws IOException, Exception {

        System.out.print("DATE: ");
        String date = in.next();
        in.nextLine();
        log.remove(date);
        System.out.println("OPERATION SUCCESSFUL");
        System.out.println("----------");
    }

    public static void addExercise() throws IOException, Exception {

        System.out.print("DATE: ");
        String date = in.next();
        in.nextLine();

        ArrayList<String> exercises = new ArrayList<>();
        System.out.print("ENTER ADDITIONAL EXERCISES (terminate by ONLY pressing enter):\n");
        String text = in.nextLine();

        while (text != "") {

            exercises.add(text);
            text = in.nextLine();
        }

        for (String s : exercises) {

            log.addTo(date, s);
        }

        System.out.println("OPERATION SUCCESSFUL");
        System.out.println("----------");
    }
}
