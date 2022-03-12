/*  This class is used to represent a bank account in a program that simulates a simple ATM
 *
 * Author @qpeano [created: 2022-02-08 | last updated: 2022-02-12]
 */

package bank;

public class Account {

    private String id; // identification of the account
    private String password; // the password for the account
    private double balance; // the account balance

    /* CONSTRUCTORS */

    //ctor 1, takes values for all fields
    public Account(String id, String password, double balance) {

        this.id = id;
        this.password = password;
        this.balance = balance;
    }

    // ctor 2, takes only id of account and a password
    public Account(String id, String password) {

        this.id = id;
        this.password = password;
    }

    // changes account balance
    public void setBalance(double balance) {

        this.balance = balance;
    }

    // returns balance
    public double getBalance() {

        return this.balance;
    }

    // for adding to balance
    public void deposit(double balancePlus) {

        this.balance += balancePlus;
    }

    // for taking away from balance
    public void withdraw(double balanceMinus) {

        this.balance -= balanceMinus;
    }
}
