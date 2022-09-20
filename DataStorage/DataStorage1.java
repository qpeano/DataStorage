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

        while ((line = br.readLine()) != null) { // do the following if the line doesn't hold an empty value

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++; // incremeted with every new line that is read in

            if (matchStart.matches()) { // if start character is found in line, add that line as a new unit

                if (layer > 0) { // if the layer is not in 0th, every extraction is made by method from DataUnit

                    layer = currentUnit.extract(line, layer);
                }
                else { // if a new unit exists in 0th layer, it is added to the unit list (u)

                    currenUnit = new DataUnit(line);
                    u.add(currentUnit);
                    layer++;
                }
            }
            else if (matchEnd.matches()) {

                layer = currentUnit.extract(line, layer);
            }
            else if (layer == 0) { // to check if formatting is correct, if text is found outside unit => informs user
                String msg = "Formatting Error In Line: " + lineCounter + "\nIn Collection :" + this.getPath();
                throw new IOException(msg);
            }
            else {

                layer = currentUnit.extract(line, layer);
            }
        }

        br.close(); // closes input connection to file
    }

    /**
     * Method prints out all data units, formatted, to the storage file
     *
     * @param f is the storage file
     * @throws IOException something goes wrong with writing to file
     */
    private void printDataUnits(File f) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // writing mechanism to file
        for (int i = 0; i < this.size(); i++) { // writes all data units to storing file

            if (i == this.size() - 1) {

                bw.write(this.units.get(i).toString());
            }
            else {

                bw.write(this.units.get(i).toString() + "\n");
            }
        }

        bw.close(); // closed ouput connection to file
    }

    /* Methods - user-interface */

    /**
     * Methods adds a new empty data unit to the 0th layer in file storage
     *
     * @param label is the name of the unit
     * @throws IOException if something goes wrong with writing to file
     */
    public void add(String label) throws IOException {

        this.units.add(new DataUnit(label)); // adds new unit
        this.printDataUnits(this.file); // prints all units out to file
        this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
    }

    /**
     * Methods removes a data unit on the 0th layer with specified label from file storage
     *
     * @param targetLabel is the name of the unit that is to be removed
     * @throws IOException if something goes wrong with writing to file
     */
    public void remove(String targetLabel) throws IOException, Exception {

        if (this.contains(targetLabel)) { // if collection contains a unit with specified label, the unit is removed

            for (int i = 0; i < this.size(); i++) { // goes through all units

                if (this.units.get(i).getLabel().equals(targetLabel)) { // tests if each units label matches specifed label

                    this.units.remove(i); // removes unit with specified label, if found
                }
            }

            this.printDataUnits(this.file); // prints out all remaining units
        }
        else { // if not in collection, method throws a message informing the user

            String msg = "DataUnit With Label \"" + targetLabel + "\" Does Not Exist In Collection: " + this.getPath();
            throw new Exception(msg);
        }

        this.isEmpty = !(this.hasContent(this.file)); // checks if file without removed unit is now empty
    }
}
