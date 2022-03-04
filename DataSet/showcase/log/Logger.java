package log;
import data.DataUnit;
import data.DataSet;
import java.util.ArrayList;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Logger {

    private DataSet logFile; // loggs

    /* CONSTRUCTORS */

    // ctor 1, takes string of path to file representing the data storage
    public Logger(String pathName) throws IOException {

        this.logFile = new DataSet(pathName);
    }

    /* INTERAL */

    private String processDate(String date) throws Exception; // formats a date to YYYY-MM-DD, if not date => inform
    private ArrayList<String> processData(ArrayList<String> data);

    /* UI */

    public add(String date, String data) throws IOException, Exception;
    public add(String date, ArrayList<String> data) throws IOException, Exception;
    public remove(String date) throws Exception;
    public addTo(String date, String data) throws IOException, Exception;
    public addTo(String date, ArrayList<String> data) throws IOException, Exception;
}
