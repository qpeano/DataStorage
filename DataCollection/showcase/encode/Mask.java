/* This class is a demonstatration of a use case for the DataCollection class that was completed a few weeks ago.
 * It uses the DataCollection class to store encryption schemes in a file so that decoding and encoding the same file
 * could be done on different runs and yeald the same result when using the same scheme.
 *
 * Author @qpeano [created 2022-02-21 | last updated 2022-02-23]
 */

package hide;
import data.DataUnit;
import data.DataCollection;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Mask {

    private File file; // connection to a file
    private ArrayList<Character> charInOrder; // list of standard characters
    private ArrayList<Character> charShuffled; // shuffled list of standard characters
    private final DataCollection keyStorage; // stores all keys to use in next run of program

    private boolean isEncoded; // to check if file is encoded (trashed)
    private boolean isEmpty; // to check if file is empty, empty => encoding is not possible
    private final boolean AUTOENCODE; // if true => encodes file when new content is added
    private ArrayList<String> fileContent; // content of effected file
    private final String SEA = "&xXzZqQjJ"; // (= Static Encryption Artifact) a thing at the end of file to mark encryption

    /* CONSTRUCTORS */

    // ctor 1, takes file to be effected and setting of autoencoding
    // throws exception if something goes wrong while setting up storage or file
    public Mask(File f, boolean setting) throws IOException, Exception {

        this.AUTOENCODE = setting;
        this.file = f;
        this.file.createNewFile(); // creates file if not already created

        this.charInOrder = this.makeCharList(); // generates list of characters
        this.fileContent = this.extract(this.file); // gets contents of connected file
        this.isEmpty = !(this.hasContent(this.file)); // checks if file has content, negated to suit use of field
        this.keyStorage = new DataCollection("/workspaces/85860293/migration/JAVA/PROJECTS/ENCODE/KEY/keys.txt"); // storage
        this.fetchKey(0); // gets data from the first data unit in storage to be the key for encoding/decoding
    }

    // ctor 2, takes file path, autoencoding is off
    // throws exception if something goes wrong while setting up storage or file
    public Mask(File f) throws IOException, Exception {

        this.AUTOENCODE = false;
        this.file = f;
        this.file.createNewFile(); // creates file if not already created

        this.charInOrder = this.makeCharList(); // generates list of characters
        this.fileContent = this.extract(this.file); // gets contents of connected file
        this.isEmpty = !(this.hasContent(this.file)); // checks if file has content, negated to suit use of field
        this.keyStorage = new DataCollection("/workspaces/85860293/migration/JAVA/PROJECTS/ENCODE/KEY/keys.txt"); // storage
        this.fetchKey(0); // gets data from the first data unit in storage to be the key for encoding/decoding
    }

    /* INTERNAL */

    // method is used for checking if file has textcontent or not
    // throws exception if something goes wrong with the reading
    private boolean hasContent(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism
        String line; // stores every line that is read from file
        boolean result = false; // indicator to check if file has content

        while ((line = br.readLine()) != null) { // checks if each line contains something

            result = true;
            break; // breaks loop so that it doesn't have to go through whole file
        }

        // state of isEncoded depends on if auto encoding is on or if files has content => chance of finding SEA
        this.isEncoded = (result) ? this.hasArtifact(f) : this.AUTOENCODE;
        br.close(); // close connection to file
        return result;
    }

    // method is used for checking if file is encoded or not by seeking a static encryption artifact at end of file
    // throws exception if reading file goes wrong
    private boolean hasArtifact(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism
        String line; // stores every line that is read from file
        boolean result = false; // indicator to check if SEA is found

        while ((line = br.readLine()) != null) { // checks if each line contains something

            if (line.equals(this.SEA)) { // checks if line ONLY contains the SEA

                result = true;
            }
        }

        return result;
    }

    // method gets all lines of text from file and adds it to the list of lines that is used for printing
    // throws exception if something goes wrong while reading lines of file
    private ArrayList<String> extract(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism
        String line; // stores every line that is read from file
        ArrayList<String> list = new ArrayList<>(); // new list for storing contents

        while ((line = br.readLine()) != null) {

            if (!line.equals(this.SEA)) { // checker so that SEA isn't included as content

                list.add(line);
            }
        }

        return list;
    }

    // method is used for getting a list of all standard ascii character
    // used to set the charInOrder field
    private ArrayList<Character> makeCharList() {

        ArrayList<Character> list = new ArrayList<>(); // new list
        char ch = ' '; // starting character
        for (int i = 32; i < 127; i++) { // goes through characters, adding them to the list

            list.add(ch); // adds character
            ch++; // increments to next chararcter value
        }

        return list;
    }

    // method is used to get previously used keys for encoding by accessing storage
    // throws exception if something goes wrong while getting data from storage
    private void fetchKey(int num) throws IOException, Exception {

        if (!this.keyStorage.isEmpty()) { // if storage is NOT empty, proceed

            ArrayList<String> labels = this.keyStorage.getAllLabels(); // gets all names of units
            ArrayList<String> list = this.keyStorage.get(labels.get(num)); // gets unit with specified index

            this.charShuffled = new ArrayList<>(); // shuffled chars has been assigned an empty list

            for (String key : list) { // goes through all content of the data unit with specified index

                int val = Integer.parseInt(key); // gets integer value of fragment (line of text)
                char ch = (char) this.truncate(val + ""); // alters it so that it is a legal ascii value for a character
                this.charShuffled.add(ch); // adds character to list of shuffled chars
            }

            if (this.charShuffled.size() != this.charInOrder.size()) { // is two list isn't the same size, inform user

                String msg = "NOT ENOUGH CHARACTER IN UNIT TO BIND (" + this.charShuffled.size() + "/95)\n";
                this.charShuffled = new ArrayList<>(); // reassigns shuffled list to an empty list
                throw new Exception(msg);
            }
        }
        else { // else, just assign shuffled list to an empty list

            this.charShuffled = new ArrayList<>();
        }
    }

    // method used for generating and storing new a key for encoding and decoding data
    // throws exception if problem with storing data occurs
    private void generateKey() throws IOException {

        this.charShuffled = new ArrayList<>(charInOrder); // shuffled list is a copy of in-order list
        Collections.shuffle(this.charShuffled); // shuffled list is shuffled

        ArrayList<String> fragmentList = new ArrayList<>(); // list of altered numeric values of characters to be stored
        for(Character c : this.charShuffled) { // goes through whole list of shuffled characters

            int value = (int) c.charValue(); // gets the ascii value of char
            int extendedValue = this.extend(value + ""); // alters value so that it isn't easy to know what char it represents
            fragmentList.add(extendedValue + ""); // adds value to list of values
        }

        String label = this.keyStorage.size() + ""; // label for new unit is the size of the list of units in storage
        this.keyStorage.add(label, fragmentList); // saves new unit with key
    }

    // method is used for adding "noise" to the ascii value of a character while storing it
    // inverse operator of truncate
    private int extend(String num) {

        int a = Integer.parseInt(num); // gets numeric value of string
        a += 123456789;
        a *= 13;
        return a;
    }

    // method is used for removing "noise" from the ascii value of a character while retrieving value from storage
    // inverse operator of extend
    private int truncate(String num) {

        int a = Integer.parseInt(num); // gets numeric value of string
        a /= 13;
        a -= 123456789;
        return a;
    }

    // this method is used for encoding pieces of text
    // throws exception if generation and storing of new key is compromised
    private String mask(String txt) throws Exception {

        if (this.charShuffled.isEmpty()) { // if shuffled list is empty, generate a new list

            this.generateKey();
        }

        char[] txtArr = txt.toCharArray(); // users text is now a char array
        String newTxt = ""; // encoded text
        for (char txtChar : txtArr) { // goes through all characters of char array

            for (int i = 0; i < this.charInOrder.size(); i++) { // goes through whole in-order list

                if (txtChar == this.charInOrder.get(i).charValue()) { // if character of char array is found in in-order list

                    newTxt += this.charShuffled.get(i).charValue(); // increment encoded text with a character of shuffled list
                    break; // placed so that it goes to next character in char array
                }
            }
        }

        return newTxt;
    }

    // this method is overloaded version of the one above, used for encoding lists of text
    // uses method above to decrease lines and sorrow
    // throws exception if generation and storing of new key is compromised
    private ArrayList<String> mask(ArrayList<String> txtList) throws Exception {

        ArrayList<String> newTxtList = new ArrayList<String>(); // encoded list of text lines
        for (String txt : txtList) { // goes through whole list from user and encodes every single line of the list

            newTxtList.add(this.mask(txt));
        }

        return newTxtList;
    }

    // this method is used for decoding pieces of text
    // throws exception if generation and storing of new key is compromised
    private String unmask(String txt) throws Exception {

        char[] txtArr = txt.toCharArray(); // users text is now a char array
        String newTxt = ""; // decoded text
        for (char txtChar : txtArr) { // goes through all characters of char array

            for (int i = 0; i < this.charShuffled.size(); i++) { // goes through whole shuffled list

                if (txtChar == this.charShuffled.get(i).charValue()) { // if character of char array is found in in-order list

                    newTxt += this.charInOrder.get(i).charValue(); // increment encoded text with a character of in-order list
                    break; // placed so that it goes to next character in char array
                }
            }
        }

        return newTxt;
    }

    // this method is overloaded version of the one above, used for decoding lists of text
    // uses method above to decrease lines and sorrow
    // throws exception if generation and storing of new key is compromised
    private ArrayList<String> unmask(ArrayList<String> txtList) throws Exception {

        ArrayList<String> newTxtList = new ArrayList<String>(); // encoded list of text lines
        for (String txt : txtList) { // goes through whole list from user and encodes every single line of the list

            newTxtList.add(this.unmask(txt));
        }

        return newTxtList;
    }

    // method is used for printing out content to effected file
    // throws exception if something happens while writing
    private void printContent(File f) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // writing mechanism to file
        String content = ""; // the entire chunk of text of the file
        for (int i = 0; i < this.fileContent.size(); i++) { // goes through all lines of text in list

            if (i == this.fileContent.size() - 1) { // if its the last line of text, only add that line

                content += this.fileContent.get(i);
            }
            else { // else, add line and a newline

                content += this.fileContent.get(i) + "\n";
            }
        }

        if (this.AUTOENCODE || this.isEncoded) { // if autoencoding is on or if file is supposed to be encoded, add SEA

            content += "\n" + this.SEA;
        }

        bw.write(content); // write out content to file
        bw.close(); // close writing mechanism
    }

    /* USER INTERFACE */

    // method is used for adding a line of text to file
    // throws exception if writing process goes wrong
    public void add(String txt) throws IOException, Exception {

        if (txt != "") { // if user doesn't give an empty string, proceed

            if (txt != this.SEA) { // if user doesn't give a string ONLY containg the SEA

                if (this.AUTOENCODE) { // if auto encoding is on, encode user input

                    txt = this.mask(txt);
                }

                this.fileContent.add(txt); // add user input to the file content list
                this.printContent(this.file); // write out the contents list to the file
            }
            else { // else, inform user about them only giving SEA

                String msg = "CANNOT ADD STATIC ENCRYPTION ARTIFACT TO FILE:";
                throw new Exception(msg);
            }
        }
    }

    // method is overloaded verison of the one above, used for adding multiple lines of text to file
    // uses method above to decrease lines and sorrow
    // throws exception if writing process goes wrong
    public void add(ArrayList<String> txtList) throws IOException, Exception {

        for (String txt : txtList) { // goes through provided list and writes all lines out to file

            this.add(txt);
        }
    }

    // method is used for copying contents from one file to another
    // throws exception if something happens while reading or writing between files
    public void getContentOf(File f) throws IOException, Exception {

        ArrayList<String> list; // new list, represents content of other file (f)

        if (this.AUTOENCODE) { // if auto encoding is on on current file, encode content of other file before adding

            list = this.mask(this.extract(f));
        }
        else { // else, don't encode

            list = this.extract(f);
        }

        this.fileContent.addAll(list); // adds content of second file to current file's content
        this.printContent(this.file); // writes new content out in current file
    }

    // method used to encode all text contents of the file effected by Mask
    // throws exception if something goes wrong while reading or overwriting file, or if file is already encoded
    public void encode() throws IOException, Exception {

        if (!this.isEncoded && !this.AUTOENCODE) { // if files ISN'T encoded, proceed

            if (!this.AUTOENCODE) { // if auto encoding is off, proceed

                this.fileContent = this.mask(this.fileContent); // encode all text of file
                this.isEncoded = true; // set encoding setting to true
                this.printContent(this.file); // write encoded content to file
            }
            else { // else inform user

                String msg = "OPERATION NOT OPTIMAL IF FILE IS AUTOENCODED\n";
                throw new Exception(msg);
            }
        }
        else { // else inform user

            String msg = "FILE ALREADY ENCODED\n";
            throw new Exception(msg);
        }
    }

    // method used to decode all text contents of the file effected by Mask
    // throws exception if something goes wrong while reading or overwriting file, or if file is already decoded
    public void decode() throws IOException, Exception {

        if (this.isEncoded && !this.AUTOENCODE) { // if file is encoded, proceed

            if (!this.AUTOENCODE) { // if auto encoding is off, proceed

                this.fileContent = this.unmask(this.fileContent); // decode all text fo file
                this.isEncoded = false; // set encoding state to false
                this.printContent(this.file); // write decoded content to file
            }
            else { // else inform user

                String msg = "OPERATION NOT OPTIMAL IF FILE IS AUTOENCODED\n";
                throw new Exception(msg);
            }
        }
        else { // else inform user

            String msg = "FILE NOT ENCODED\n";
            throw new Exception(msg);
        }
    }

    // method is used for changing key (encryption scheme) to another from the storage
    // method also changes settings of isEncoded, is now false
    // throws exception if problem with storing data occurs
    public void changeKeyTo(int num) throws IOException, Exception {

        if (!this.AUTOENCODE) { // proceed if files isn't auto encoded

            if (num < 0 || num > this.keyStorage.size()) { // inform user if specified index is out of bounds

                String msg = "GIVEN INDEX NOT IN RANGE: [0, " + (this.keyStorage.size() - 1) + "]\n";
                throw new Exception(msg);
            }

            this.fetchKey(num); // get the key with specified index, set it to shuffled list
            this.isEncoded = false; // file is now considered NOT encoded
            this.printContent(this.file); // write out content to file, now without the SEA
        }
        else { // else inform user that file is auto encoded and chanig key is kinda stoopid

            String msg = "CANNOT CHANGE KEY, CURRENT FILE IS AUTOENCODED\n";
            throw new Exception(msg);
        }
    }

    // method used to make a new key for encoding text, changes current key and settings
    // throws exception if something happens while writing new key to storage
    public void newKey() throws IOException, Exception {

        this.generateKey(); // makes new key
        int key = this.keyStorage.size() - 1; // index of the new key
        this.changeKeyTo(key); // shuffled list now has new content in the form of a new key
    }

    // method only works iff file is autoencoded, used for making a new file with the decoded contents of effected file
    // throws exception if something happens when writing to file
    public File makeDecodedCopy(String pathName) throws IOException, Exception {

        if (this.AUTOENCODE) { // proceed if current file is auto encoded

            File f = new File(pathName); // new file for decoded content
            ArrayList<String> list = this.unmask(this.fileContent); // decoded content of current file
            BufferedWriter bw = new BufferedWriter(new FileWriter(f)); // writing mechanism to file
            String content = ""; // content of new file

            for (int i = 0; i < list.size(); i++) {

                if (i == list.size() - 1) { // if it is the last line of text, only add the text line

                    content += list.get(i);
                }
                else { // else, add text line and a new line

                    content += list.get(i) + "\n";
                }
            }

            bw.write(content); // writes out the decoded content to new file
            bw.close(); // closes writing mechanism
            return f;
        }

        // if file isn't autoencoded, inform user
        String msg = "METHOD CANNOT BE APPLIED TO FILE WITHOUT AUTOENCODING\n";
        throw new Exception(msg);
    }
    
    @Override
    // method is used as a diagnostics tool to see if methods are working properly,
    // displays state of file and a snapshot of the settings of an instance of the Mask class
    public String toString() {

        String state = "Mask Effecting: " + this.file.toString() + "\n";
        state += "Auto-encoding: " + this.AUTOENCODE + "\n";
        state += "Encoded: " + this.isEncoded;
        return state;
    }
}
