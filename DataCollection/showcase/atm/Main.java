/*  This project demonstrate a usecase for the DataCollection class where all the account information is stored in
 *  the individual data units in the collection. The data is fetched on each run and can also be altered
 *  this altering is visible on the next run of the program
 *
 * Author @qpeano [created 2022-02-08 | last updated 2022-02-12]
 */

import data.DataCollection;
import data.DataUnit;
import bank.Account;
import java.util.Scanner;
import java.util.ArrayList;

class Main {

    public static char runAgain; // to see if user wants to run program again
    public static Scanner in; // input device
    public static DataCollection dc; // data storing device
    public static String id; // name of current bank account
    public static String password; // password of current bank account
    public static double balance; // balance of current bank account

    // main method, where everything starts
    public static void main(String[] args) {

        try {

            runAgain = 'y'; // initiallly set to y (= re-run program)
            dc = new DataCollection("bank.txt");
            in = new Scanner(System.in);

            do { // do the following while runAgain is y

                // ask user if they want to log in to an existing account or make a new one
                System.out.println("LOGIN: 1\nADD ACCOUNT: 2");
                System.out.println("----------");
                int inputChoice = in.nextInt();
                System.out.println("----------");

                if (inputChoice == 1) {

                    runLogin(); // runs log in function
                }
                else if (inputChoice == 2) {

                    runAddAccount(); // runs account creation function
                }
                else {

                    // informs user if they chose an alternative that wasn't previously stated
                    System.out.println("[FAULTY INPUT]");
                }
            }
            while(runAgain == 'y');

            in.close(); // close input device

        }
        catch (Exception e) {

            // if something goes wrong, the user is informed with origin of problem
            System.out.println("Something went wrong :/\nmaybe no account stored in collection..?\nDetails:\n");
            e.printStackTrace();
        }
    }

    // this method is called if chose 1, and wants to log in to existing account
    // redirects exception that is called from other methods, to main
    public static void runLogin() throws Exception {

        // ask user for account id and password
        System.out.print("ID: ");
        id = in.next();
        in.nextLine();
        System.out.print("PASSWORD: ");
        String password = in.next();
        in.nextLine();
        System.out.println("----------");

        // if collection contains id AND password is correct, then binding process begins
        if (dc.contains(id)) {

            ArrayList<String> frags = dc.get(id);
            if (frags.get(0).equals(password)) {

                bind(id);
            }
            else { // if collection doesn't contain stated password, user is informed

                System.out.println("[WRONG PASSWORD]");
                cont(); // asks user if they want to do something else
            }
        }
        else { // if collection doesn't contain account with specified id, user is informed

            System.out.println("[NO SUCH ACCOUNT EXISTS]");
            cont(); // asks user if they want to do something else
        }
    }

    // method is called if user chose 2, and wants to create a new account
    // redirects exception that is called from other methods, to main
    public static void runAddAccount() throws Exception {

        if (!dc.isEmpty()) {

            // do the following while user gives id that is in collection
            // this is to prevent duplicate account id's
            do {

                System.out.print("ENTER AN ID: ");
                id = in.next();
                in.nextLine();
            }
            while (dc.contains(id));
        }
        else { // if collection is empty, that check isn't needed

            System.out.print("ENTER AN ID: ");
            id = in.next();
            in.nextLine();
        }

        // asks for password for new account
        System.out.print("ENTER A PASSWORD: ");
        String password = in.next();
        in.nextLine();

        // writes account information to collection, informs user about success
        dc.add(id, password);
        dc.addTo(id, "0.0");
        System.out.println("ACOOUNT SUCCESSFULLY CREATED");

        System.out.println("----------");
        cont(); // asks user if they want to do something else
    }

    // method binds data from the user, or existing data in collection to a new account object
    // redirects exception that is called from other methods, to main
    public static void bind(String id) throws Exception {

        password = dc.get(id).get(0); // first fragment of certain unit (account) is the password
        balance = Double.parseDouble(dc.get(id).get(1)); // second fragment of certain unit (account) is the password
        Account acc = new Account(id, password); // new account is created with data from collection
        acc.setBalance(balance); // balance is set
        runMenu(acc); // runs the menu, where user can check balance and change it
    }

    // method is called after binding process, and gives user options to get data from account or alter it (ONLY the balance)
    // redirects exception that is called from other methods, to main
    public static void runMenu(Account acc) throws Exception {

        // asks user if they want to withdraw from account, deposit to account or just check balance
        System.out.println("WITHDRAW: 1\nDEPOSIT: 2\nCHECK BALANCE: 3");
        System.out.println("----------");
        int choice = in.nextInt();
        System.out.println("----------");

        if (choice == 1) {

            runWithdraw(acc); // starts withdrawing process
        }
        else if (choice == 2) {

            runDeposit(acc); //starts depositing process
        }
        else if (choice == 3) {

            runCheck(acc); //starts process of printing balance to console
        }
        else {

            // if other option is selected, user is informed
            System.out.println("[FALUTY INPUT]");
            cont(); // asks user if they want to do something else
        }
    }

    // this method is called if user chose 1 in runMenu, used to withdraw cash from current account and also
    // changes entry in collection
    // throws exception if overwriting file (collection) goes wrong
    public static void runWithdraw(Account acc) throws Exception {

        // asks user for the amount that is to be withdrawn from account
        System.out.print("WRITE AMOUNT IN DOLLARS: ");
        double amount = in.nextDouble();

        if (amount <= acc.getBalance()) { // if the balance is big enough, do this

            double newAmount = acc.getBalance() - amount; // set a new balance
            acc.withdraw(amount); // withdraw from account
            dc.clearDataUnit(id); // clear account representation in collection
            dc.addTo(id, password); // re-add account info to collection
            dc.addTo(id, ("" + newAmount)); // add new balance to collection
        }
        else { // if balance is too small, inform user

            System.out.println("[INSUFFICIENT FUNDS]");
        }

        System.out.println("----------");
        cont(); // asks user if they want to do something else
    }

    // this method is called if user chose 2 in runMenu, used to deposit cash to current account and also
    // changes entry in collection
    // throws exception if overwriting file (collection) goes wrong
    public static void runDeposit(Account acc) throws Exception {

        // asks user for the amount that is to be deposited to account
        System.out.print("WRITE AMOUNT IN DOLLARS: ");
        double amount = in.nextDouble();

        if (amount > 0) { // if amount isn't negative, which means that depositing would be withdrawing, then do this

            double newAmount = acc.getBalance() + amount; // sets new amount
            acc.deposit(amount); // makes a deposit
            dc.clearDataUnit(id); // clear account representation in collection
            dc.addTo(id, password); // re-add account info to collection
            dc.addTo(id, ("" + newAmount)); // add new balance to collection
        }
        else { // else, inform user

            System.out.println("[DEPOSIT MUST BE GREATER THAN ZERO]");
        }

        System.out.println("----------");
        cont(); // asks user if they want to do something else
    }

    // prints out the balance of current account in dollars
    public static void runCheck(Account acc) {

        System.out.println("$" + acc.getBalance()); // prints balance
        cont(); // asks user if they want to do something else
    }

    // this method is used to change the state of the runAgain field
    // this is the only method that keeps program from creating a infinite loop
    public static void cont() {

        // asks if user wants to do something else
        System.out.println("DO YOU WISH TO DO SOMETHING ELSE?(y/n)");
        System.out.println("----------");
        runAgain = Character.toLowerCase(in.next().charAt(0)); // input from user changes runAgain's state
        System.out.println("----------");
    }
}
