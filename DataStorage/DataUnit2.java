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

        String newInfo = info.replaceAll("    ", ""); // removes tab
        return newInfo;
    }

    public DataUnit newInnerUnit(String label, /* DataUnit outerUnit */) {

        String formattedLabel = this.formatLabel(label);
        DataUnit innerUnit = new DataUnit(formattedLabel);
        // innerUnit.outerUnit = this; // does this work?
        innerUnit.outerUnit = this.getThisUnit();
        this.innerUnits.add(innerUnit);
        return innerUnit;
    }

    private DataUnit getThisUnit() {

        return this;
    }

    public DataUnit getOuterUnit() {

        return this.outerUnit;
    }
}
