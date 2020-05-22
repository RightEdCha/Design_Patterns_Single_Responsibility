import java.io.*;
import java.net.*;

class client{
    static Boolean clientTerminated = false;

    public static void main(String argv[]) throws Exception{
        String clientCommand;
        String serverResponse;
        String hostName = argv[0];
        int port = Integer.parseInt(argv[1]);

        //Set up socket and attach a buffer to read from and write to.
        Socket socket = new Socket(hostName, port);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

        //Read the inital hello message.
        serverResponse = inFromServer.readLine();
        printToClientTerminalBasedOnServerResponse(serverResponse);

        //Keep connection persistent from client side.
        while(!clientTerminated){
            clientCommand = userInput.readLine();
            outToServer.flush();
            outToServer.writeBytes(clientCommand + "\n");
            serverResponse = inFromServer.readLine();
            printToClientTerminalBasedOnServerResponse(serverResponse);
        }
        socket.close();
    }

    private static void printToClientTerminalBasedOnServerResponse(String response){
        String initialString = "receive: ";
        if(response.equals("-1")){
            System.out.println(initialString + "incorrect operation command.");
        }
        else if(response.equals("-2")){
            System.out.println(initialString + "number of inputs is less than two.");
        }
        else if(response.equals("-3")){
            System.out.println(initialString + "number of inputs is more than four.");
        }
        else if(response.equals("-4")){
            System.out.println(initialString + "one or more of the inputs contain(s) non-number(s).");
        }
        else if(response.equals("-5")){ 
            clientTerminated = true; 
            System.out.println(initialString + "exit.");
        } 
        else { 
            System.out.println(initialString + response); 
        }
    }

}