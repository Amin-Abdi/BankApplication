import java.io.*;
import java.net.*;

public class BankClientA {

    public static void main(String[] args) throws IOException {

        Socket bankClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int bankSocketNumber = 4500;
        String bankServerName = "localhost";
        String bankClientId = "bankClient1";

        try {
            bankClientSocket = new Socket(bankServerName, bankSocketNumber);
            out = new PrintWriter(bankClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(bankClientSocket.getInputStream()));

        }catch (UnknownHostException e) {
            System.err.println("Don't know about: " + bankServerName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ bankServerName);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromUser, fromServer;
        System.out.println("Initialised " + bankClientId + " client and IO connections");

        while (true) {
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println(bankClientId + " sending " + fromUser + " to bankServer");
                out.println(fromUser);
            }

            fromServer = in.readLine();
            System.out.println(bankClientId + " received " + fromServer + " from bankServer");

        }
    }
}
