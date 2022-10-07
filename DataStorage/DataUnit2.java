package data;
import java.util.ArrayList;

public class DataUnit {

    private ArrayList<DataUnit> innerUnits;
    private ArrayList<String> fragments;
    private DataUnit outerUnit;
    private String label;

    public DataUnit(String name) {

        this.label = this.formatLabel(name);
        this.outerUnit = null;
        this.fragments = new ArrayList<>();
        this.innerUnits = new ArrayList<>();
    }

    private String formatLabel(String name) {

        String newName = name.replaceAll( "\\{", "").replaceAll( "    ", "");
        return newName;
    }

    private String formatFragment(String info) {

        String newInfo = info.replaceAll("    ", ""); // removes tabs
        return newInfo;
    }

    public DataUnit newInnerUnit(String label) {

        String formattedLabel = this.formatLabel(label);
        DataUnit innerUnit = new DataUnit(formattedLabel);
        // innerUnit.outerUnit = this; // does this work?
        innerUnit.outerUnit = this.getThisUnit();
        this.innerUnits.add(innerUnit);
        this.fragments.add(null); // assigns space for a unit in file
        return innerUnit;
    }

    private DataUnit getThisUnit() {

        return this;
    }

    public DataUnit getOuterUnit() {

        return this.outerUnit;
    }

    public void add(String fragment) {

        String newFragment = this.formatFragment(fragment);
        this.fragments.add(newFragment);
    }

    private StringBuilder makeString() {

        StringBuilder content = new StringBuilder();
        String formedLabel = this.label + "{\n";
        content.append(formedLabel);
        int indexOfInnerUnitList = 0;

        for (int count = 0; count < this.fragments.size(); count++) {

            String fragment = this.fragments.get(count);
            if (fragment == null) {

                String[] arrayOfElements = this.innerUnits.get(indexOfInnerUnitList).makeString().toString().split("\n");

                for (int j = 0; j < arrayOfElements.length; j++) {
                    String formattedLine;
                    if (j == arrayOfElements.length - 1) {

                        formattedLine = "   " + arrayOfElements[j] + "\n";
                        formattedLine += "   " + "}\n";
                    }
                    else {

                        formattedLine = "   " + arrayOfElements[j] + "\n";
                    }
                    content.append(formattedLine);
                }
                indexOfInnerUnitList++;
            }
            else {

                String formattedFragment;
                if (count == this.fragments.size() - 1) {

                    formattedFragment =  "    " + fragment;
                }
                else {

                    formattedFragment =  "    " + fragment + "\n";
                }
                content.append(formattedFragment);
            }
        }

       // content.append("}"); // duplicates } onto every last element because it is run for every unit
       // addition of last bracket is done in getString();
        return content;
    }

    public String toString() {

        StringBuilder content = this.makeString();
        content.append("}");
        return content.toString();
    }
}
