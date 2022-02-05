/* This class is designed to act as a data storing unit for my projects. It writes information to
 * files and then extracts that information in other runs. The information can be changed through deletion,
 * incrementation. You can also get contents of other DataCollection files, and can clear current file
 * The information is divided into units called DataUnits, that consists of key-value (label, content) pairs.
 *
 * This is a proof of concept for makeshift databases using files
 *
 * Author @qpeano [created 2022-01-29 | last updated: 2022-02-02]
 */


package data;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DataCollection {

    private File file; // where all data units are stored
    private ArrayList<DataUnit> units; // all the individual units
    private boolean isEmpty; // indicator for a few methods

    /* CONSTRUCTORS */

    // ctor 1, connects to a file with specified name, content is extracted if file is NOT empty
    // throws IOException if something goes wrong with file creation/ connection
    public DataCollection(String path) throws IOException {

        this.file = new File(path); // makes connection to file
        this.file.createNewFile(); // creates file if it does not exist
        this.units = new ArrayList<>();
        this.isEmpty = !(this.hasContent(this.file)); // to check if file has content, negated to suit use of field

        if (!this.isEmpty()) { // if file is not empty all data is extracted and written to list of units

            this.extract(this.file, this.units);
        }
    }

    // ctor 2, connects to a file with specified name, list of units is given some starting values from user
    // content from file is extracted if file is NOT empty
    // throws IOException if something goes wrong with file creation/ connection
    public DataCollection(String path, ArrayList<DataUnit> startingUnits) throws IOException {

        this.file = new File(path); // makes connection to file
        this.file.createNewFile(); // creates file if it does not exist
        this.units = startingUnits;
        this.isEmpty = !(this.hasContent(this.file)); // to check if file has content, negated to suit use of field

        if (!this.isEmpty()) { // if file is not empty all data is extracted and written to list of units

            this.extract(this.file, this.units);
        }
    }

    /* INTERNAL */

    // this method is used to check if a file has any content whatsoever, used in conjuntion with this.extract
    // throws exception if something goes wrong with reading file
    private boolean hasContent(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file

        String line; // a line in the file
        boolean result = false;

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

             result = true;
        }

        br.close();
        return result;
    }

    // this method extracts all data from a file and fills the collections data units with said data
    // throws exception if something goes wrong while writing to file
    private void extract(File f, ArrayList<DataUnit> u) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file
        Pattern start = Pattern.compile("[a-zA-Z0-9_)] + \\{"); // marker for beginning of a new unit
        Pattern end = Pattern.compile("\\}"); // marker for end of a unit
        String line; // a line in the file
        Matcher matchStart; // matches a line from file with start charcter
        Matcher matchEnd; // matches a line from file with end charcter
        boolean inDataUnit = false; // checks if a line is in a data unit or not (between {, })

        while ((line = br.readLine()) != null) { // do the following if the line doesn't hold an empty value

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);

            if (matchStart.find()) { // if start character is found in line, add that line as a new unit

                u.add(new DataUnit(line));
                inDataUnit = true;
                continue;
            }
            else if (matchEnd.find()) { // if end character is found in line, go to next line

                inDataUnit = false;
                continue;
            }
            else if (!inDataUnit) { // to check if formatting is correct, if text is found outside unit => informs user

                throw new IOException("Formatting Error in Collection");
            }
            else { // else add line as a new data fragment to the last added unit

                int lastAddedUnit = u.size() - 1; // get index of last added unit
                u.get(lastAddedUnit).addTo(line); // access last unit, added new fragment
            }
        }

        br.close(); // closed connection to file
    }

    // this method is used for printing out all data units and their content to a file
    // throws exception if something goes wrong with writing to file
    private void printDataUnits(File f) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // writing mechanism to file
        for (int i = 0; i < this.size(); i++) { // writes all data units to storing file

            if (i == this.size() - 1) {

                bw.write(this.units.get(i).toString()); // last unit 
            }
            else {

                bw.write(this.units.get(i).toString() + "\n"); // not last unit
            }
        }

        bw.close(); // closed connection to file
    }

    /* USER INTERFACE */

    // this method is used for adding a new unit to collection
    // throws exception if something goes wrong with writing to file
    public void add(String label, String content) throws IOException {

        this.units.add(new DataUnit(label, content)); // adds new unit
        this.printDataUnits(this.file); // prints all units out to file
        this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
    }

    // this is an overloaded method of the one above and is used for adding a new unit to collection
    // throws exception if something goes wrong with writing to file
    public void add(String label, ArrayList<String> content) throws IOException {

        this.units.add(new DataUnit(label, content)); // adds new unit
        this.printDataUnits(this.file); // prints all units out to file
        this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
    }

    // this is an overloaded method of the one above and is used for adding a new EMPTY unit to collection
    // throws exception if something goes wrong with writing to file
    public void add(String label) throws IOException {

        this.units.add(new DataUnit(label)); // adds new unit without fragments
        this.printDataUnits(this.file); // prints all units out to file
        this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
    }

    // this method is used for removing all occurrences a data unit with specific label
    // throws exception if a unit with specific label is not found
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

    // this method is used in the one above and is used for checking if a unit with specific label exists in collection
    // throws exception if collection is empty or if hasContent() throws an exception
    public boolean contains(String label) throws IOException, Exception {

        if (this.hasContent(this.file)) { // checks if collection is empty

            for (DataUnit unit : this.units) { // tests if each units label matches specifed label

                if (unit.getLabel().equals(label)) { // unit is found, -> returns true

                    return true;
                }
            }

            return false;
        }

        throw new Exception("DataCollection Is Empty"); // returns a message informing user about the emptiness of collection
    }

    // this method is used to see if a collection (file representng a collection) is empty
    public boolean isEmpty() {

        return this.isEmpty;
    }

    // this method is used to see where the collection (text file representing a collection) is stored on the computer
    public String getPath() {

        return this.file.toString();
    }

    // this method is used for getting the size of the collection (number of units)
    public int size() {

        return this.units.size();
    }

    // this method is used for clearing a collection clean. All data is lost forever if not copied to another collection
    // throws exception if something goes wrong while writing
    public void clear() throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(this.file)); // connection to file, writing mechanism
        bw.write(""); // overwrite everything with an empty string
        bw.close(); // closed connection

        this.units.clear(); // deletes all units from list
        this.isEmpty = true; // sets status to EMPTY
    }

    // this method is used to copy over all data units from another collection, it will result in duplicates
    // throws exception if something happens while reading or writing between collections
    public void addContentsOf(DataCollection dc) throws IOException {

        if (!dc.isEmpty()) { // checks if other collection is NOT empty

            for (DataUnit unit : dc.units) { // if so, goes through all units

                this.units.add(unit); // adds units to this collection
            }

            this.printDataUnits(this.file); // prints out all units
        }
    }

    @Override
    // this method checks is this collections is identical to another
    public boolean equals(Object other) {

        if (other instanceof DataCollection) { // checks if argument is a collection

            DataCollection dc = (DataCollection) other; // casted to access behaviour and fields of a collection

            return (this.units.equals(dc.units)); // uses ArrayList.equals to see if contents of collections are identical
        }

        return false;
    }

    @Override
    // this method is used as a diagnostics tool to see if all other methods are working
    // also used in equals to get string representations of entire collections
    public String toString() {

        if (!this.isEmpty) {

            String state = this.getPath() + ":\n"; // adds the path of collection

            for (int i = 0; i < this.size(); i++) { // goes through every unit and add their content to the string

                if (i == this.size() - 1) {

                    state += this.units.get(i).toString(); // last unit 
                }
                else {

                    state += this.units.get(i).toString() + "\n"; // not last unit
                }
            }

            return state; // returns state
        }
        else {

            return null; // returns empty value
        }
    }

    // this method is used for adding a data fragment to an existing data unit in collection
    // throws exception if unit with specified label does not exist
    public void addTo(String label, String fragment) throws IOException, Exception {

        if (this.contains(label)) { // if collection contains a unit with specified label

            for (int i = 0; i < this.size(); i++) { // goes through all all units

                if (this.units.get(i).getLabel().equals(label)) { // do the following if data unit is found

                    this.units.get(i).addTo(fragment); // adds fragment to unit
                }
            }

            this.printDataUnits(this.file); // prints all data units with their content to file
        }
        else {
            // do the following if collection doesn't contain a unit with specified label
            String msg = "DataUnit With Label \"" + label + "\" Does Not Exist In Collection: " + this.getPath();
            throw new Exception(msg);
        }
    }

    // this method is used to get rid of all content/ fragments from units with a specific label
    // throws exception if something goes wrong while searching for unit, or printing all remaining units
    public void clearDataUnit(String label) throws IOException, Exception {

        if (this.contains(label)) { // if collection contains a unit with specified label

            for (DataUnit unit : this.units) { // goes through all all units

                if (unit.getLabel().equals(label)) { // do the following if data unit is found

                    unit.clear(); // clears data unit
                }
            }

            this.printDataUnits(this.file); // prints all remaining units
        }
        else {
            // do the following if collection doesn't contain a unit with specified label
            String msg = "DataUnit With Label \"" + label + "\" Does Not Exist In Collection: " + this.getPath();
            throw new Exception(msg);
        }
    }

    // this method is used to get the content of a the first found unit with specified label
    public ArrayList<String> get(String label) throws Exception {

        if (this.contains(label)) { // if a unit with specific label exists

            for (DataUnit unit : this.units) { // go through whole list

                if (unit.getLabel().equals(label)) { // if a first instance of unit with specific label is found

                    return unit.getFragments(); // return its fragments as a list
                }
            }
        }
        // else inform the user about the non-existence of a unit with specified label
        String msg = "DataUnit With Label \"" + label + "\" Does Not Exist In Collection: " + this.getPath();
        throw new Exception(msg);
    }

    // diagnostics tool to check if all units are in the collection
    // throws exception if a collection is empty
    public ArrayList<String> getAllLabels() throws Exception {

        if (!this.isEmpty()) { // if NOT empty, proceed

            ArrayList<String> labels = new ArrayList<String>(); // make a list for all labels
            for (DataUnit unit : this.units) { // go through all units...

                labels.add(unit.getLabel()); //... and get their lables
            }

            return labels; // return list of labels
        }

        throw new Exception("DataCollection Is Empty"); // else, inform user
    }
}
