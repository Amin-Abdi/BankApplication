import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class BankServer {

    public static void main(String[] args) throws IOException {

        ServerSocket bankServerSocket = null;
        boolean listening = true;
        String bankServerName = "bankServer";
        int portNum = 4500;

        //double sharedVariable = 100;
        HashMap<String, Integer> sharedVariable = new HashMap<>();
        sharedVariable.put("A", 450);
        sharedVariable.put("B", 1000);
        sharedVariable.put("C", 1000);

        //Accounts
        SharedBankState ourSharedBankStateObj = new SharedBankState(sharedVariable);


        try {
            bankServerSocket = new ServerSocket(portNum);
        } catch (IOException e) {
            System.err.println("Couldn't start " + bankServerName + " at port: " + portNum);
            System.exit(-1);
        }
        System.out.println(bankServerName + " started...");

        while (listening) {
            new BankServerThread(bankServerSocket.accept(), "BankServerThread1", ourSharedBankStateObj).start();
            new BankServerThread(bankServerSocket.accept(), "BankServerThread2", ourSharedBankStateObj).start();
            new BankServerThread(bankServerSocket.accept(), "BankServerThread3", ourSharedBankStateObj).start();

        }

        bankServerSocket.close();

    }

}
