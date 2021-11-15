import java.net.*;
import java.io.*;


public class BankServerThread extends Thread{
    private Socket bankSocket = null;
    private SharedBankState mySharedBankStateObject;
    private String mybankServerThreadName;
   // private double mySharedVariable;


    public BankServerThread(Socket bankSocket,String bankServerThreadName,SharedBankState sharedObject ) {
        super(bankServerThreadName);
        this.bankSocket = bankSocket;
        mySharedBankStateObject = sharedObject;
        mybankServerThreadName = bankServerThreadName;
    }

    public void run() {
        try {
            System.out.println(mybankServerThreadName + " initialising");
            PrintWriter out = new PrintWriter(bankSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(bankSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                try {
                    mySharedBankStateObject.acquireLock();
                    outputLine = mySharedBankStateObject.processInput(mybankServerThreadName, inputLine);
                    out.println(outputLine);
                    mySharedBankStateObject.releaseLock();

                } catch(InterruptedException e) {
                    System.err.println("Failed to get lock when reading:"+ e);
                }
            }
            out.close();
            in.close();
            bankSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
