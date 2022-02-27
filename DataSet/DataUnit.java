/* This class is used in conjuntion with a class that is yet to be created called DataCollection.
 * This class represents a unit of data that consists of a label and content. The content consists of a list
 * of strings. Each string is called a fragment.
 *
 * This version of the DataUnit is used in the DataSet class
 * where the collection only has unique units with unique fragments
 *
 *  Author @qpeano [created 2022-01-28 | last updated 2022-02-27]
 */


package data;
import java.util.ArrayList;

public class DataUnit {

    private String label; // name of specific unit of data
    private ArrayList<String> fragments; // all the content the unit holds

    /* CONSTRUCTORS */

    // ctor 1, accepts a label and a premade list of content
    public DataUnit(String name, ArrayList<String> info) {

        this.label = this.formatLabel(name);
        this.fragments = this.formatFragment(info);
    }

    // ctor 2, accepts a label and a singular value
    public DataUnit(String name, String info) {

        this.label = this.formatLabel(name);
        String newInfo = this.formatFragment(info);
        this.fragments = new ArrayList<>();
        this.fragments.add(newInfo);
    }

    // ctor 3, accepts only label
    public DataUnit(String name) {

        this.label = this.formatLabel(name);
        this.fragments = new ArrayList<>();
    }

    /* INTERNAL */

    // this method is used to remove any formatting characters ({, }, and \t) from the label of a unit
    private String formatLabel(String name) {

        String newName = name.replaceAll("[ {]", "");
        return newName;
    }

    // this method is used to remove any formatting characters from all the content of a unit
    private ArrayList<String> formatFragment(ArrayList<String> info) {

        ArrayList<String> newInfo = new ArrayList<>();
        String tmp;

        for (String str : info) {

            tmp = str.replaceAll("    ", ""); // removes tab
            newInfo.add(str);
        }

        return this.getUniqueFragments(newInfo);
    }

    // this method is overloaded, this version is used to remove formatting characters from content of
    // of a unit if it was instantiated with ctor 3
    private String formatFragment(String info) {

        String newInfo = info.replaceAll("    ", ""); // removes tab
        return newInfo;
    }

    // this method is used to get a list of only unique elements from another list
    private ArrayList<String> getUniqueFragments(ArrayList<String> list) {

        ArrayList<String> newList = new ArrayList<>();
        for (String str : list) {

            if (!newList.contains(str)) {

                newList.add(str);
            }
        }

        return newList;
    }

    /* USER INTERFACE */

    // this method is used to add a new fragment of data to unit
    public void addTo(String fragment) {

        String newFragment = this.formatFragment(fragment);

        if (!this.fragments.contains(newFragment)) { // checks so that additional fragment is unique

            this.fragments.add(newFragment);
        }
    }

    // this method is used to remove a fragment from unit, throws Exception if fragment doesn't exist
    public void remove(String targetFragment) throws Exception {

        if (this.hasFragment(targetFragment)) { // if collection has a fragment

            this.fragments.remove(targetFragment); // removes fragment
        }
        else {

            String msg = "Data Fragment \"" + targetFragment + "\" Does Not Exist In Unit: " + this.label;
            throw new Exception(msg);
        }
    }

    // this method is used to reveal label for a unit
    public String getLabel() {

        return this.label;
    }

    // this method is used to get a copy of the content of a unit
    public ArrayList<String> getFragments() {

        ArrayList<String> fragmentsCopy = new ArrayList<String>(this.fragments);
        return fragmentsCopy;
    }

    @Override
    // diagnostics tool to see if everything works and printing tool for the DataCollection class
    public String toString() {

        String state = this.label + " {\n"; // label of unit

        for (int i = 0; i < this.fragments.size(); i++) { // goes through whole list of fragment and gets their content

            if (i == this.fragments.size() - 1) { // if it's the last fragment, dont add newline char

                state += "    " + this.fragments.get(i); // adds a tab
            }
            else { // else, add a newline char

                state += "    " + this.fragments.get(i) + "\n"; // adds a tab and newline
            }
        }

        state += "\n}";
        return state;
    }

    // method is used to check if a unit contains a specific fragment
    public boolean hasFragment(String target) {

        return this.fragments.contains(target);
    }

    // method is used to see how many data fragment a unit contains
    public int size() {

        return this.fragments.size();
    }

    // method is used to clear a data unit of all its fragment
    public void clear() {

        this.fragments = new ArrayList<>();
    }
}
