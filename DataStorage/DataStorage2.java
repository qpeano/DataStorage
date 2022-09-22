package data;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DataStorage {

    /* Fields */

    private File file; // where all data units are stored
    private ArrayList<DataUnit> units; // all the individual units
    private boolean isEmpty; // indicator for a few methods

    /* Methods - constructor */

    /**
     * constructor
     *
     * @param path string rep. of the path to the storage file
     * @throws IOException if something goes wrong while retrieving data units from storage file,
     * or if something happens while creating file
     */
    public DataStorage(String path) throws IOException {

        this.file = new File(path); // makes connection to file
        this.file.createNewFile(); // creates file if it does not exist
        this.units = new ArrayList<>();
        this.isEmpty = !(this.hasContent(this.file)); // to check if file has content, negated to suit use of field

        if (!this.isEmpty()) { // if file is not empty all data is extracted and written to list of units

            this.extract(this.file, this.units);
        }
    }

    /* Methods - internal */

    /**
     * Method is used check if a file has any content whatsoever, used in conjuntion with this.extract()
     *
     * @param f is the file used to store the data units
     * @throws IOException is something goes wrong while reading file
     */
    private boolean hasContent(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file

        String line; // a line in the file
        boolean result = false;

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

             result = true;
        }

        br.close(); // closes input connection to file
        return result;
    }

    /**
     * Method extracts all data from a file and fills a list of data units with said data
     *
     * @param f is the file used to store the data units
     * @param u is a list of data units
     * @throws IOException is something goes wrong while reading file, or if formatting is wrong
     */
    private void extract(File f, ArrayList<DataUnit> u) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file
        Pattern start = Pattern.compile("\s*[a-zA-Z0-9_)]+ \\{"); // marker for beginning of a new unit
        Pattern end = Pattern.compile("\s*\\}"); // marker for end of a unit
        String line; // a line in the file
        Matcher matchStart; // matches a line from file with start charcter
        Matcher matchEnd; // matches a line from file with end charcter
        int layer = 0; // the layer a line is in, layer being the number of nested data units a line is in
        int lineCounter = 0; // the number of a line in file
        DataUnit currentUnit; // the unit a line is in, if that line is in a unit at all

        while ((line = br.readLine()) != null) {  // do the following if the line doesn't hold an empty value

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++; // incremeted with every new line that is read in

            if (matchStart.matches()) {

                int spaces = (line.replaceAll("[^ ]", "").length() - 1) / 4;
                if (layer == 0) {

                    currentUnit = new DataUnit(line);
                    u.add(currentUnit);
                    layer++
                }
                else if (layer < spaces + 1) { // edited 2022-09-21 09:50

                    currentUnit = currentUnit.newInnerUnit(line);
                    layer++;
                }
                // else if (layer == spaces) {

                //     currentUnit.newInnerUnit(line);
                //     layer++;
                // }
            }
            else if (matchEnd.matches()) {

                if (layer > 1) {

                    layer--;
                    currentUnit = currentUnit.outerUnit();
                }
                else if (layer == 1) {

                    layer--;
                }
            }
            // else if (layer == 0) { // to check if formatting is correct, if text is found outside unit => informs user
            //     String msg = "Formatting Error In Line: " + lineCounter + "\nIn Collection :" + this.getPath();
            //     throw new IOException(msg);
            // }
            else {

                currentUnit.add(line);
            }
        }
    }
}
