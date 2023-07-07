package data;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class DataStorage {

    /* Inner class definition */

    private class DataUnit { // inner class representing a a unit that can store data, and other units

        /* Inner class fields */

        private ArrayList<DataUnit> innerUnits; // all the units in current unit
        private ArrayList<String> fragments; // all the string content the unit holds
        private DataUnit outerUnit; // name of the unit that current unit is in
        private String label; // name of current unit of data

        /* Inner class methods - constructor */

        /**
         * constructor
         *
         * @param label what is used to get data of a unit
         */
        public DataUnit(String label) {

            this.label = this.formatLabel(label);
            this.outerUnit = null;
            this.fragments = new ArrayList<>();
            this.innerUnits = new ArrayList<>();
        }

        /* Inner class methods - internal */

        /**
         * method removes special characters that are used by an extraction algorithm,
         * leave only the label that was used when unit was defined
         *
         * @param label the unformatted version of what is used to get data of a unit
         * @return formatted label, without special characters
         */
        private String formatLabel(String label) {

            String formattedLabel = label.replaceAll( "\\{", "").replaceAll( " ", ""); // formats label
            return formattedLabel;
        }

        /**
         * method removes special characters that are used by an extraction algorithm,
         * leave only the fragment that was used when unit was defined¨
         *
         * @param fragment the unformatted version of the data that can be found in a unit
         * @return formatted fragment , without special characters
         */
        private String formatFragment(String fragment) {

            String newFragment = fragment.replaceAll("    ", ""); // removes tabs
            return newFragment;
        }

        /**
         * method makes a string representation of what a unit looks like, resembles the file tree,
         * where tab (four spaces) tell you in what level a unit and/or fragment is
         *
         * @return a stringbuilder containing the string representation, without the last "}"
         */
        private StringBuilder makeString() {

            StringBuilder content = new StringBuilder(); // contains the string rep, used bcuz it's faster than string concat
            int indexOfInnerUnitList = 0; // keeps count on the next inner that the method needs to extract a string rep. from

            // adds the label of the top-level unit to string rep
            String formedLabel = this.label + " {\n";
            content.append(formedLabel);

            for (int count = 0; count < this.fragments.size(); count++) { // goes through all the fragments

                String fragment = this.fragments.get(count);
                if (fragment == null) { // null => fragment is an inner unit

                    // gets the next inner unit, and gets the string rep. from it => makes a list of all the lines
                    String[] arrayOfElements = this.innerUnits.get(indexOfInnerUnitList).makeString().toString().split("\n");

                    for (int lineIndex = 0; lineIndex < arrayOfElements.length; lineIndex++) { // goes through each line of the string rep.

                        String formattedLine = "    " + arrayOfElements[lineIndex] + "\n"; // adds back formatting that was removed
                        if (lineIndex == arrayOfElements.length - 1) { // adds end of unit marker back (formatting)

                            formattedLine += "    " + "}\n";
                        }

                        content.append(formattedLine);
                    }
                    indexOfInnerUnitList++; // next unit
                }
                else {

                    String formattedFragment = "    " + fragment + "\n"; // adds back formatting that was removed
                    content.append(formattedFragment);
                }
            }

            return content; // returns the entire string rep, without last end-of-unit marker
        }

        /**
         * method is used to get the reference of current unit, so that other units have an outer unit
         *
         * @return reference to current unit
         */
        private DataUnit getThisUnit() {

            return this;
        }

        /**
         * method checks if a label for a new inner unit is unique (has been used or not)
         *
         * @param label the label
         * @return if it is unique up to layer or not
         */
        private boolean isLabelUniqueUptoLayer(String label) {

            ArrayList<String> innerLabels = this.getInnerLabels();
            return !(innerLabels.contains(label));
        }

        /* Inner class methods - interface */

        /**
         * method creates and returns an inner unit to current unit
         *
         * @param label what is used to get data of a unit
         * @return a data unit, that is inside current unit
         */
        public DataUnit newInnerUnit(String label) {

            String formattedLabel = this.formatLabel(label);
            if (this.isLabelUniqueUptoLayer(formattedLabel)) {

                DataUnit innerUnit = new DataUnit(formattedLabel);
                innerUnit.outerUnit = this.getThisUnit(); // the unit that holds the new unit is the current unit
                this.innerUnits.add(innerUnit);
                this.fragments.add(null); // assigns space for a unit in file
                return innerUnit;
            }

            return null; // returns nothing if there already exists a unit in layer with given label
        }

        /**
         * method adds a fragment to the current unit
         *
         * @param fragment a piece of text
         */
        public void add(String fragment) {

            String newFragment = this.formatFragment(fragment);
            this.fragments.add(newFragment);
        }

        /**
         * method is used to check if a unit with specified label exists in this unit
         *
         * @param label
         * @return true if exists, false if not
         */
        public boolean hasInnerUnit(String label) {

            ArrayList<String> innerLabels = this.getInnerLabels();
            String formattedLabel = this.formatLabel(label);
            boolean result = innerLabels.contains(formattedLabel);
            return result;
        }

        /**
         * method is used to get label of current unit
         *
         * @return the label of current unit
         */
        public String getLabel() {

            return this.label;
        }

        /**
         * method is used for retrieving all labels of the units that are in the layer below to this unit,
         * that are in this unit
         *
         * @return labels of all units inside this units, that are in this unit
         */
        public ArrayList<String> getInnerLabels() {

            ArrayList<String> labels = new ArrayList<>();
            for (DataUnit du : this.innerUnits) { // goes through all inner units and gets their labels

                labels.add(du.getLabel());
            }

            return labels;
        }

        /**
         * method is used to get the inner unit with specified label of current unit
         *
         * @param label the label of the inner unit
         * @return the data unit with specified label
         */
        public DataUnit getInnerUnit(String label) {

            for (int i = 0; i < this.innerUnits.size(); i++) {

                if (this.innerUnits.get(i).getLabel().equals(label)) { // goes through all all units

                    return this.innerUnits.get(i); // returns the unit with label that matches argument
                }
            }

            return null; // if no label matches, then return null
        }

        /**
         * method deletes current unit
         */
        public void deleteThis() {

            int unitsToSkip = this.outerUnit.innerUnits.indexOf(this.getThisUnit()); // gets the index of current unit
            for (int i = 0; i < this.outerUnit.fragments.size(); i++) { // goes through the fragments

                String unit = this.outerUnit.fragments.get(i);
                if (unit == null) { // checks if fragment is null (placeholder for unit)

                    if (unitsToSkip == 0) { // if it is the right unit, remove placeholder and exit loop

                        this.outerUnit.fragments.remove(i);
                        break;
                    }

                    unitsToSkip--; // else, keep in loop and mark it
                }
            }
            this.outerUnit.innerUnits.remove(this.getThisUnit()); // remove the acutal unit
        }

        /**
         * method clears current unit of all its fragments and inner units
         */
        public void clearThis() {

            this.innerUnits.clear();
            this.fragments.clear();
        }

        /* Inner class methods - diagnostics & others */

        /**
         * method is used by other units to get the unit it is in, i.e their outer unit.
         *
         * @return reference to outer unit
         */
        public DataUnit getOuterUnit() {

            return this.outerUnit;
        }

        /**
         * method is used to get the full string representation of the current unit,
         * with the end-of-unit character which belongs to the top-level unit
         *
         * @return string representation of entire unit
         */
        public String toString() {

            StringBuilder content = this.makeString();
            content.append("}");
            return content.toString();
        }
    }

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
        DataUnit currentUnit = null; // the unit a line is in, if that line is in a unit at all

        while ((line = br.readLine()) != null) {  // do the following if the line doesn't hold an empty value

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++; // incremeted with every new line that is read in

            if (matchStart.matches()) { // if start character is found in line, add that line as a new unit

                int spaces = (line.replaceAll("[^ ]", "").length() - 1) / 4; // counts number of tabs => correlates to layer
                if (layer == 0) { // if line isn't already in a unit, create and add new unit with line

                    currentUnit = new DataUnit(line);
                    u.add(currentUnit);
                    layer++;
                }
                else if (layer < spaces + 1) { // if line is in a unit, create a new inner unit, and add that with the line

                    currentUnit = currentUnit.newInnerUnit(line);
                    layer++;
                }
            }
            else if (matchEnd.matches()) { // if end character is found in line, exit current unit (goto outer unit)

                if (layer > 1) { // if there exists outer units, exit current unit

                    layer--;
                    currentUnit = currentUnit.getOuterUnit();
                }
                else if (layer == 1) { // if there are no outer units, just alter layer field to match situation

                    layer--;
                }
                if (layer < 0) { // if a end character that belongs to no unit exists tell user, terminate program

                    String msg = "Formatting Error In Line: " + lineCounter + "\nIn Collection :" + this.getPath();
                    throw new IOException(msg);
                }
            }
            else { // if line has no special character, add it to the fragments of current unit

                if (layer == 0) { // if text is found outside a unit, tell user, terminate program

                    String msg = "Formatting Error In Line: " + lineCounter + "\nIn Collection :" + this.getPath();
                    throw new IOException(msg);
                }
                currentUnit.add(line); // add line if not
            }
        }

        br.close(); // close connection to file
    }

    /**
     * Method prints out all data units and their content to a file (the DataStorage)
     *
     * @param f the DataStorage file
     * @throws IOException if something goes wrong while writing to file
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

        bw.close(); // closed connection to file
    }

    /**
     * Method searches for the unit with specified label
     *
     * @param pathLabel the specified label formatted xxx.xxx.xxx
     * @return unit with specified label
     * @throws Exception if unit doesn't exist in data storage
     */
    private DataUnit search(String pathLabel) throws Exception {

        String[] labels = pathLabel.split("\\."); // gets all the labels where lower index, means higher layer

        DataUnit unit = null;
        boolean unitExists = false;
        String fullLabelName = labels[0]; // for exception
        int layerIndex = 0; // counts layer

        for (int i = 0; i < this.size(); i++) { // goes through layer 0 (top layer) to check for unit

            unitExists = this.units.get(i).getLabel().equals(fullLabelName);
            if (unitExists) { // if it is found, keep it's reference, and exit loop

                unit = this.units.get(i);
                layerIndex++;
                break;
            }
        }

        if (labels.length == 1 && unitExists) { // if pathLabel has no periods => unit is in top layer => return unit

            return unit;
        }
        else if (labels.length > 1 && unitExists) { // if unit is not in top layer, look for it with labels

            for (int i = 1; i < labels.length; i++) { // goes through the labels

                boolean hasInnerUnit = unit.hasInnerUnit(labels[layerIndex]);
                fullLabelName += "." + labels[layerIndex];
                if (hasInnerUnit) { // if unit exists with label, keep that unit's reference, go to next layer

                    unit = unit.getInnerUnit(labels[layerIndex]);
                    layerIndex++;
                }
                else { // if not, inform user

                    String message = "DataUnit with label: " + fullLabelName + " does not exist\nIn Collection :" + this.getPath();
                    throw new Exception(message);
                }
            }

            return unit; // return the unit ( reference to the deepest unit)
        }

        // if unit does not exist in top layer, inform user
        String message = "DataUnit with label: " + labels[0] + " does not exist\nIn Collection :" + this.getPath();
        throw new Exception(message);
    }

    /* Methods - interface */

    /**
     * Method adds new unit with a fragment to top layer of data storage
     *
     * @param label the label of unit
     * @param fragment the fragment that the unit will contain
     * @throws IOException if something happens while writing to file
     */
    public void addNewUnit(String label, String fragment) throws IOException {

        DataUnit newUnit = new DataUnit(label);
        newUnit.add(fragment);
        this.units.add(newUnit);
        this.printDataUnits(this.file);
    }

    /**
     * Method adds new unit to top layer of data storage
     *
     * @param label the label of unit
     * @throws IOException if something happens while writing to file
     */
    public void addNewUnit(String label) throws IOException {

        this.units.add(new DataUnit(label));
        this.printDataUnits(this.file);
    }

    /**
     * Method adds new unit to an existing unit in data storage
     *
     * @param pathLabel the path of label to location of new unit
     * @param label label of new unit
     * @throws IOException if something happens while writing to file
     * @throws Exception if any unit in pathLabel is not found
     */
    public void addNewUnitTo(String pathLabel, String label) throws IOException, Exception {

        DataUnit outerUnit = this.search(pathLabel);
        outerUnit.newInnerUnit(label);
        this.printDataUnits(this.file);
    }

    /**
     * Method adds new unit with fragment to an existing unit in data storage
     *
     * @param pathLabel the labels that lead to a unit which will be the outer unit of the new unit
     * @param label label of new unit
     * @param fragment the fragment of the new unit
     * @throws IOException if something happens while writing to file
     * @throws Exception if any unit in pathLabel is not found
     */
    public void addNewUnitTo(String pathLabel, String label, String fragment) throws IOException, Exception {

        DataUnit outerUnit = this.search(pathLabel);
        outerUnit.newInnerUnit(label);
        DataUnit unit = outerUnit.getInnerUnit(label);
        unit.add(fragment);
        this.printDataUnits(this.file);
    }

    /**
     * Method adds a new fragment to a specified unit
     *
     * @param pathLabel the labels that lead to the unit that will get a new fragment
     * @param fragment a new fragment
     * @throws IOException if something happens while writing to file
     * @throws Exception if any unit in pathLabel is not found
     */
    public void addFragmentTo(String pathLabel, String fragment) throws IOException, Exception {

        DataUnit unit = this.search(pathLabel);
        unit.add(fragment);
        this.printDataUnits(this.file);
    }

    /**
     * Method deletes unit with specified label
     *
     * @param pathLabel the labels that lead to the unit that is to be deleted
     * @throws IOException if something happens while writing to file
     * @throws Exception if any unit in pathLabel is not found
     */
    public void deleteUnit(String pathLabel) throws IOException, Exception {

        String[] labels = pathLabel.split("\\.");
        DataUnit unit = this.search(pathLabel);
        if (labels.length == 1) { // if unit is on layer 0, remove it from list

            this.units.remove(unit);
        }
        else { // else, delete using inner methods

            unit.deleteThis();
        }

        this.printDataUnits(this.file);
    }

    /**
     * Method clears unit with specified label
     *
     * @param pathLabel the labels that lead to the unit that is to be cleared
     * @throws IOException if something happens while writing to file
     * @throws Exception if any unit in pathLabel is not found
     */
    public void clearUnit(String pathLabel) throws IOException, Exception {

        DataUnit unit = this.search(pathLabel);
        unit.clearThis();
        this.printDataUnits(this.file);
    }

    /* Methods - other */

    /**
     * Method makes a string representation of the entire DataStorage.
     * Used as a diagnostics tool to see if all other methods are working
     *
     * @return string representation of DataStorage
     */
    @Override // flag for complier
    public String toString() {

        if (!this.isEmpty) {

            String state = this.getPath() + ":\n"; // adds the path of collection

            for (int i = 0; i < this.size(); i++) { // goes through every unit and add their content to the string

                if (i == this.size() - 1) {

                    state += this.units.get(i).toString();
                }
                else {

                    state += this.units.get(i).toString() + "\n";
                }
            }

            return state;
        }
        else { // if Datastorage is empty, return nothing

            return null;
        }
    }

    /**
     * Method gives the number of top-level units that exists in a DataStorage
     *
     * @return number of top-level units
     */
    public int size() {

        return this.units.size();
    }

    /**
     * Method gives the string representation of the path of the DataStorage file
     *
     * @return path to DataStorage file
     */
    public String getPath() {

        return this.file.toString();
    }

    /**
     * Method checks if the file is empty
     *
     * @return value signaling emptiness
     */
    public boolean isEmpty() {

        return this.isEmpty;
    }
}
