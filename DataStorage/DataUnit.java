package data;
import java.util.ArrayList;

public class DataUnit {

    /* Fields */

    private ArrayList<DataUnit> innerUnits; // all the units in current unit
    private ArrayList<String> fragments; // all the string content the unit holds
    private DataUnit outerUnit; // name of the unit that current unit is in
    private String label; // name of current unit of data

    /* Methods - constructor */

    /**
     * constructor
     *
     * @param name what is used to get data of a unit
     */
    public DataUnit(String name) {

        this.label = this.formatLabel(name);
        this.outerUnit = null;
        this.fragments = new ArrayList<>();
        this.innerUnits = new ArrayList<>();
    }

    /* Methods - internal */

    /**
     * method removes special characters that are used by an extraction algorithm,
     * leave only the label that was used when unit was defined
     *
     * @param name the unformatted version of what is used to get data of a unit
     * @return formatted label, without special characters
     */
    private String formatLabel(String name) {

        String newName = name.replaceAll( "\\{", "").replaceAll( " ", "");
        return newName;
    }

    /**
     * method removes special characters that are used by an extraction algorithm,
     * leave only the fragment that was used when unit was defined¨
     *
     * @param info the unformatted version of the data that can be found in a unit
     * @return formatted fragment , without special characters
     */
    private String formatFragment(String info) {

        String newInfo = info.replaceAll("    ", ""); // removes tabs
        return newInfo;
    }

    /**
     * method makes a string representation of what a unit looks like, resembles the file tree,
     * where tab (four spaces) tell you in what level a unit and/or fragment is
     *
     * @return a stringbuildler containing the string representation, without the last "}"
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
     * @return if it is unique or not
     */
    private boolean isLabelUniqueUptoLayer(String label) {

        ArrayList<String> innerLabels = this.getInnerLabels();
        return !(innerLabels.contains(label));
    }

    /* Methods - interface */

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
     * @param info a piece of text
     */
    public void add(String info) {

        String newFragment = this.formatFragment(info);
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

    // forgotten method
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
        for (DataUnit du : this.innerUnits) {

            labels.add(du.getLabel());
        }

        return labels;
    }

    // risky method
    public DataUnit getInnerUnit(String label) {

        for (int i = 0; i < this.innerUnits.size(); i++) {

            if (this.innerUnits.get(i).getLabel().equals(label)) {

                return this.innerUnits.get(i);
            }
        }

        return null;
    }

    // risky method
    public void delete(String label) {

        this.innerUnits.remove(label);
    }

    public void clear() {

        this.innerUnits.clear();
        this.fragments.clear();
    }

    /* Methods - diagnostics & others */

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
