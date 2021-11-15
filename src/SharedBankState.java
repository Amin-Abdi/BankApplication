import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class SharedBankState {

    private SharedBankState mySharedObj;
    private String myThreadName;
    private HashMap<String, Integer> mySharedVariable;
    private boolean accessing = false; // true a thread has a lock, false otherwise
    private int threadsWaiting = 0; // number of waiting writers

    SharedBankState(HashMap<String, Integer> sharedVariable) {
        mySharedVariable = sharedVariable;
    }

    //getting a lock
    public synchronized void acquireLock() throws InterruptedException {
        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " is attempting to acquire a lock");
        ++threadsWaiting;

        while (accessing) {
            System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
            wait();
        }

        //Nobody has got a lock
        --threadsWaiting;
        accessing = true;
        System.out.println(me.getName() + " got a lock");
    }

    //releasing a lock
    public synchronized void releaseLock() {
        accessing = false;
        notifyAll();
        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " released the lock");
    }

    //Adding to bank account
    public synchronized String processInput(String myThreadName, String theInput) {
        System.out.println(myThreadName + " received " + theInput);
        String theOutput = null;

        String[] actions = theInput.split(" ");
        //System.out.println(Arrays.toString(actions));
        String operation = actions[0];
        int amount = Integer.parseInt(actions[1]);

        //For the addition
        if (operation.equalsIgnoreCase("add")) {
            switch (myThreadName) {
                case "BankServerThread1":
                    theOutput = performAddition("A", amount);
                    break;
                case "BankServerThread2":
                    theOutput = performAddition("B", amount);
                    break;
                case "BankServerThread3":
                    theOutput = performAddition("C", amount);
                    break;
            }
        }

        //For the subtraction
        if (operation.equalsIgnoreCase("subtract")) {
            if (myThreadName.equals("BankServerThread1")) {
                theOutput = performSubtraction("A", amount);
            } else if (myThreadName.equals("BankServerThread2")) {
                theOutput = theOutput = performSubtraction("B", amount);
            } else if (myThreadName.equals("BankServerThread3")) {
                theOutput = performSubtraction("C", amount);
            }
        }

        System.out.println(mySharedVariable);
        return theOutput;
    }

    //Adding money to the account
    public String performAddition(String k, int num) {
        int newAmount = mySharedVariable.get(k) + num;
        mySharedVariable.put(k, newAmount);
        return String.valueOf(newAmount);
    }

    //Subtracting from the account
    public String performSubtraction(String k, int num) {
        int newAmount = mySharedVariable.get(k) - num;
        mySharedVariable.put(k, newAmount);
        return String.valueOf(newAmount);
    }

}
