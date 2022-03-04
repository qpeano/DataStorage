/* This is the third iteration of a device that stores data between executions of any program. This iteration is specialized
 * for cases where you want to save unique sets of data. It also has a simple encryption system built in as a showcase in
 * the actual design.
 *
 * Author @qpeano [created 2022-02-26 | last updated 2022-03-03]
 */

package data;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;

public class DataSet {

    private File file; // file representation of data set
    private ArrayList<DataUnit> units; // the content of a set
    private boolean isEmpty; // indicator to check if file is empty
    private boolean isEncrypted; // indicator to check if file is encrypted
    private String encryptedContent; // encryted version of text content in file

    /* CONSTRUCTOR */

    // ctor, takes file path
    // throws exception if something goes wrong while creating or reading file
    public DataSet(String pathName) throws IOException {

        this.file = new File(pathName); // connect to file
        this.file.createNewFile(); // create file if it does not exist
        this.units = new ArrayList<>(); // new unit list
        this.isEmpty = !(this.hasContent(this.file)); // checks if file has content or not, negated to suite use of filed

        if (!this.isEmpty()) { // if file isn't empty, get all info out and store it in unit list

            this.extract();
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
        this.isEncrypted = this.hasArtifact(f); // checks if file is encrypetd
        return result;
    }

    // this method is used for setting the isEncrypted field by seeing if an artifact of length 3 chars (127) is present in file
    // throws exception if something goes wrong with reading file
    private boolean hasArtifact(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file

        String line; // a line in the file
        boolean result = false;

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

            String first3Letters = line.substring(0, 3); // first 3 chars in file
            result = first3Letters.equals("127"); // checks if first line STARTS with 127
            break; // go out of loop cuz 127 is not found anywhere else in file other than in first line
        }

        br.close(); // close connection
        return result;
    }

    // this method is used to extract all information from a file and format it to fit the data units
    // if file is encrypted, another specialized method is called
    // throws exception if something goes wrong while writing to file
    private void extract() throws IOException {

        if (this.isEncrypted) { // if encrypted, assign content of file to encrypted content

            this.encryptedContent = this.extractEncrypted();
        }
        else { // else, bind units to content of file

            this.extractDecrypted(this.file, this.units);
        }
    }

    // this method extracts all data from a file and fills the sets data units with said data
    // throws exception if something goes wrong while writing to file
    private void extractDecrypted(File f, ArrayList<DataUnit> u) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file
        Pattern start = Pattern.compile("[a-zA-Z0-9_)]+ \\{"); // marker for beginning of a new unit
        Pattern end = Pattern.compile("\\}"); // marker for end of a unit
        String line; // a line in the file
        Matcher matchStart; // matches a line from file with start charcter
        Matcher matchEnd; // matches a line from file with end charcter
        boolean inDataUnit = false; // checks if a line is in a data unit or not (between {, })
        int lineCounter = 0;

        while ((line = br.readLine()) != null) { // do the following if the line doesn't hold an empty value

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++;

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
                String msg = "Formatting Error In Line: " + lineCounter + "\nIn Set:" + this.getPath();
                throw new IOException(msg);
            }
            else { // else add line as a new data fragment to the last added unit

                int lastAddedUnit = u.size() - 1; // get index of last added unit
                u.get(lastAddedUnit).addTo(line); // access last unit, added new fragment
            }
        }

        br.close(); // closed connection to file
    }

    // this method is used to get content of a file if that file appears encrypted
    // throws exception if some formatting is incorrect
    private String extractEncrypted() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(this.file)); // reading mechanism
        StringBuilder content = new StringBuilder(); // used because concatinating strings take longer
        String line;

        while ((line = br.readLine()) != null) { // while line doesn't hold an empty value, add line to contents

            content.append(line);
            content.append("\n");
        }

        br.close();
        this.extractContent(content.toString(), this.units); // extracts data units from encrypted contents
        return content.toString();
    }

    // this method is used to fill data units with data from the encrypted content by first decrypting it
    // and then formatting it
    // throws exception if some formatting is incorrect
    private void extractContent(String source, ArrayList<DataUnit> u) throws IOException {

        String decryptedContent = this.internalDecrypt(source); // decrypts contents
        String[] content = decryptedContent.split("\\n"); // every line is now its own string
        Pattern start = Pattern.compile("[a-zA-Z0-9_)]+ \\{"); // marker for beginning of a new unit
        Pattern end = Pattern.compile("\\}"); // marker for end of a unit
        Matcher matchStart; // matches a line from content list with start charcter
        Matcher matchEnd; // matches a line from content list with end charcter
        boolean inDataUnit = false; // checks if a line is in a data unit or not (between {, })
        int lineCounter = 0;

        for (String line : content) {

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++;

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
                String msg = "Formatting Error In Line: " + lineCounter + "\nIn Set:" + this.getPath();
                throw new IOException(msg);
            }
            else { // else add line as a new data fragment to the last added unit

                int lastAddedUnit = u.size() - 1; // get index of last added unit
                u.get(lastAddedUnit).addTo(line); // access last unit, added new fragment
            }
        }
    }

    // this method is used to decrypt content without decrypting anything in the file
    // line 208: starts at 2nd word because 1st word is 127, the encryption marker
    private String internalDecrypt(String str) {

        StringBuilder newStr = new StringBuilder(); // SB is faster at combing strings
        String[] arr0 = str.split("\\s"); // splits string from user into words (no white-space inbetween)
        for (String s : arr0) { // adds every word to the stringbuilder, there is still no white-space
            newStr.append(s);
        }

        String[] arr = this.splitToNChar(newStr.toString(), 3); // string in stringbuilder into list of strings w/ length 3ch
        int val; // the ascii value each string represents
        newStr = new StringBuilder(); // assigned to another stringbuilder

        for (int i = 1; i < arr.length; i++) { // goes through every 3ch word and parses the numerical value from it

            val = Integer.parseInt(arr[i]); // parses the ascii value for a character
            char c = (char) val; // converts value to character
            newStr.append(c); // adds character to stringbuilder
        }

        return newStr.toString();
    }

    // this method is used by the one above to seperate a string of words into a list of 3 character-strings
    // source:
    // stack overflow,"Splitting a string every n-th character", answer by Simon Nickerson [19-02-2010]
    // https://stackoverflow.com/questions/2297347/splitting-a-string-at-every-n-th-character
    private String[] splitToNChar(String text, int size) {

        ArrayList<String> parts = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }

        return parts.toArray(new String[0]);
    }

    // this method is used for printing out all data units and their content to a file
    // throws exception if something goes wrong with writing to file
    private void printDataUnits(File f) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // writing mechanism to file
        if (this.isEncrypted) {

            this.encryptedContent = this.updateEncryptedContent(); // assigns new value to encrypted content
            bw.write(this.encryptedContent); // prints encrypted content to file
        }
        else {

            for (int i = 0; i < this.size(); i++) { // writes all data units to storing file

                if (i == this.size() - 1) {

                    bw.write(this.units.get(i).toString());
                }
                else {

                    bw.write(this.units.get(i).toString() + "\n");
                }

            }
        }
        bw.close(); // closed connection to file
    }

    // this method is used by internal methods an UI methods to encrypt the data of the data units
    private String internalEncrypt(String str) {

        char[] arr = str.toCharArray(); // turns users string to char array
        StringBuilder newStr = new StringBuilder(); // uses SB cuz it's faster than concatination
        Random rand = new Random(); // used to get a random length for line
        int n = (rand.nextInt(10) + 1) * 3; // first line shorter to account for 127 marker
        int ind = 1;

        for (char c : arr) { // goes through char array and encrypts and formats each character, and adds it to SB

            String code = ascii(c);
            if (ind % n  == 0) {

                newStr.append(code);
                newStr.append("\n");
                n = (rand.nextInt(10) + 1) * 5;
            }
            else {

                newStr.append(code);
            }
            ind++;
        }

        return ("127" + newStr.toString()); // adds encryption marker
    }

    // this method is used to format encrypted content so that its easy to decrypt it
    private String ascii(char c) {

        int val = (int) c; // get ascii value
        String valStr; // holds string representation of ascii value

        if (val < 100) { // if value is less than 100, proceed

            if (val < 10) { // if value is less than 10, add 2 0's and then value, to string rep

                valStr = "00" + val;
            }
            else { // else add a 0 and then value, to string rep

                valStr = "0" + val;
            }
        }
        else { // else, add value to string rep

            valStr = val + "";
        }

        return valStr;
    }

    // this method is used to update the encrypted content so that data is not lost
    // throws exception if
    private String updateEncryptedContent() {

        StringBuilder encrypted = new StringBuilder(); // SB cuz it's fast
        for (int i = 0; i < this.size(); i++) { // adds all data to SB

            if (i == this.size() - 1) {

                encrypted.append(this.units.get(i).toString());
            }
            else {

                encrypted.append(this.units.get(i).toString());
                encrypted.append("\n");
            }
        }

        return this.internalEncrypt(encrypted.toString()); // returns encrypted version of all data units
    }

    // this method is used for getting the size of the set (number of units)
    // not apart of UI because it can be used to get info about an encrypted file
    private int size() {

        return this.units.size();
    }

    /* USER INTERFACE */

    // this method is used for adding a new unit to set
    // throws exception if something goes wrong with writing to file
    public void add(String label, String content) throws IOException, Exception {

        if (!this.contains(label)) { // check to ensure that all labels are unique

            this.units.add(new DataUnit(label, content)); // adds new unit
            this.printDataUnits(this.file); // prints all units out to file
            this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
        }
    }

    // this is an overloaded method of the one above and is used for adding a new unit to set
    // throws exception if something goes wrong with writing to file
    public void add(String label, ArrayList<String> content) throws IOException, Exception {

        if (!this.contains(label)) { // check to ensure that all labels are unique

            this.units.add(new DataUnit(label, content)); // adds new unit
            this.printDataUnits(this.file); // prints all units out to file
            this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
        }
    }

    // this is an overloaded method of the one above and is used for adding a new EMPTY unit to set
    // throws exception if something goes wrong with writing to file
    public void add(String label) throws IOException, Exception {

        if (!this.contains(label)) { // check to ensure that all labels are unique

            this.units.add(new DataUnit(label)); // adds new unit
            this.printDataUnits(this.file); // prints all units out to file
            this.isEmpty = false; // changes status to NOT EMPTY, if file was empty before
        }
    }

    // this method is used for removing all occurrences a data unit with specific label
    // throws exception if a unit with specific label is not found
    public void remove(String targetLabel) throws IOException, Exception {

        if (this.contains(targetLabel)) { // if set contains a unit with specified label, the unit is removed

            for (int i = 0; i < this.size(); i++) { // goes through all units

                if (this.units.get(i).getLabel().equals(targetLabel)) { // tests if each units label matches specifed label

                    this.units.remove(i); // removes unit with specified label, if found
                }
            }

            this.printDataUnits(this.file); // prints out all remaining units
        }
        else { // if not in set, method throws a message informing the user

            String msg = "DataUnit With Label \"" + targetLabel + "\" Does Not Exist In Collection: " + this.getPath();
            throw new Exception(msg);
        }

        this.isEmpty = !(this.hasContent(this.file)); // checks if file without removed unit is now empty
    }

    // this method is used in the one above and is used for checking if a unit with specific label exists in set
    // throws exception if set is empty or if hasContent() throws an exception
    public boolean contains(String label) throws IOException, Exception {

        if (this.hasContent(this.file)) { // checks if set is empty

            for (DataUnit unit : this.units) { // tests if each units label matches specifed label

                if (unit.getLabel().equals(label)) { // unit is found, -> returns true

                    return true;
                }
            }

            return false;
        }
        return false; // perhaps set is empty (?)
    }

    // this method is used to see if a set (file representng a set) is empty
    public boolean isEmpty() {

        return this.isEmpty;
    }

    // this method is used to see where the set (text file representing a set) is stored on the computer
    public String getPath() {

        return this.file.toString();
    }

    // this method is used for clearing a set clean. All data is lost forever if not copied to another set
    // throws exception if something goes wrong while writing
    public void clear() throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(this.file)); // connection to file, writing mechanism
        bw.write(""); // overwrite everything with an empty string
        bw.close(); // closed connection

        this.units.clear(); // deletes all units from list
        this.isEmpty = true; // sets status to EMPTY
        this.encryptedContent = ""; // set encrypted content to nothing
        this.isEncrypted = false; // encryption is off as a default
    }

    // this method is used to copy over all data units from another set, it will result in duplicates
    // throws exception if something happens while reading or writing between sets
    public void addContentsOf(DataSet ds) throws IOException {

        if (!ds.isEmpty()) { // checks if other set is NOT empty

            for (DataUnit unit : ds.units) { // if so, goes through all units

                this.units.add(unit); // adds units to this set
            }

            this.printDataUnits(this.file); // prints out all units
        }
    }

    @Override
    // this method checks is this sets is identical to another
    public boolean equals(Object other) {

        if (other instanceof DataSet) { // checks if argument is a set

            DataSet ds = (DataSet) other; // casted to access behaviour and fields of a set

            return (this.units.equals(ds.units)); // uses ArrayList.equals to see if contents of sets are identical
        }

        return false;
    }

    @Override
    // this method is used as a diagnostics tool to see if all other methods are working
    // also used in equals to get string representations of entire sets
    public String toString() {

        if (!this.isEmpty) {

            String state = this.getPath() + ":\n"; // adds the path of set

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
        else {

            return null;
        }
    }

    // this method is used for adding a data fragment to an existing data unit in set
    // throws exception if unit with specified label does not exist
    public void addTo(String label, String fragment) throws IOException, Exception {

        if (this.contains(label)) { // if set contains a unit with specified label

            for (int i = 0; i < this.size(); i++) { // goes through all all units

                if (this.units.get(i).getLabel().equals(label)) { // do the following if data unit is found

                    this.units.get(i).addTo(fragment); // adds fragment to unit
                }
            }

            this.printDataUnits(this.file); // prints all data units with their content to file
        }
        else {
            // do the following if set doesn't contain a unit with specified label
            String msg = "DataUnit With Label \"" + label + "\" Does Not Exist In Set: " + this.getPath();
            throw new Exception(msg);
        }
    }

    // this method is used to get rid of all content/ fragments from units with a specific label
    // throws exception if something goes wrong while searching for unit, or printing all remaining units
    public void clearDataUnit(String label) throws IOException, Exception {

        if (this.contains(label)) { // if set contains a unit with specified label

            for (DataUnit unit : this.units) { // goes through all all units

                if (unit.getLabel().equals(label)) { // do the following if data unit is found

                    unit.clear(); // clears data unit
                }
            }

            this.printDataUnits(this.file); // prints all remaining units
        }
        else {
            // do the following if set doesn't contain a unit with specified label
            String msg = "DataUnit With Label \"" + label + "\" Does Not Exist In Set: " + this.getPath();
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
        String msg = "DataUnit With Label \"" + label + "\" Does Not Exist In Set: " + this.getPath();
        throw new Exception(msg);
    }

    // diagnostics tool to check if all units are in the set
    // throws exception if a set is empty
    public ArrayList<String> getAllLabels() throws Exception {

        if (!this.isEmpty()) { // if NOT empty, proceed

            ArrayList<String> labels = new ArrayList<String>(); // make a list for all labels
            for (DataUnit unit : this.units) { // go through all units...

                labels.add(unit.getLabel()); //... and get their lables
            }

            return labels; // return list of labels
        }

        throw new Exception("DataSet Is Empty"); // else, inform user
    }

    // this method is used by the user if they want to encrypt the content of the set
    // throws exception if something happens while writing stuff to file
    public void encrypt() throws IOException {

        this.isEncrypted = true; // turns on encryption
        this.printDataUnits(this.file); // prints encrypted version
    }

    // this method is used by the user if they want to decrypt the content of the set
    // throws exception if something happens while writing stuff to file
    public void decrypt() throws IOException {

        this.isEncrypted = false; // turns off encryption
        this.printDataUnits(this.file); // prints decrypted version
    }
}
