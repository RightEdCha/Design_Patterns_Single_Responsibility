import java.io.*;
import java.net.*;

class server{
    static Boolean serverTerminated = false;
    static Boolean connectionTerminated = false;

    public static void main(String argv[]) throws Exception  {
        String clientCommand;
        int serverResponse;

        //Have the server listen for a connection request at a socket.
        ServerSocket socket = new ServerSocket(Integer.parseInt(argv[0]));

        while(!serverTerminated){
            //Accept the connection request and create a buffer to read from and write to.
            Socket connectionSocket = socket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            connectionTerminated = false; //Flag for client connection termination.

            System.out.println("get connection from ... (" + connectionSocket.getRemoteSocketAddress().toString() + ")");
            outToClient.writeBytes("Hello!\n");

            //Keeps the connection with the client persistent - this method only allows for one client connection.
            while(!connectionTerminated){                
                String commandInputs[];
                String serverTerminalOutput;

                clientCommand = inFromClient.readLine();
                commandInputs = parseCommand(clientCommand);
                serverTerminalOutput = createServerTerminalOutputString(commandInputs);
                serverResponse = respond(commandInputs);
                serverTerminalOutput += ", return: " + serverResponse;
                System.out.println(serverTerminalOutput);
                outToClient.flush();
                outToClient.writeBytes(serverResponse + "\n");
            }
            connectionSocket.close(); 
        }   
        socket.close(); 
    }

    private static String[] parseCommand(String clientCommand){
        //Use the split command to seperate the client's commands by spaces.
        clientCommand.trim();
        String[] commandInputs = clientCommand.split("\\s");

        return commandInputs;
    }

    private static String createServerTerminalOutputString(String commandInputs[]){
                String output = "get:";

                for(int i = 0; i < commandInputs.length; i++){
                    output += " " + commandInputs[i];
                }

                return output;
    }

    private static int respond(String commandInputs[]) {
        //Check the parsed command inputs. Index 0 should be the operand, past index 0 inputs. Check for errors first.
        if(commandInputs[0].toUpperCase().equals("TERMINATE")){
            serverTerminated = true;
            connectionTerminated = true;
            return -5;
        }
        else if(commandInputs[0].toUpperCase().equals("BYE")){
            connectionTerminated = true;
            return -5;
        }
        else if(!commandInputs[0].toUpperCase().equals("ADD") && !commandInputs[0].toUpperCase().equals("SUBTRACT") && !commandInputs[0].toUpperCase().equals("MULTIPLY") && !commandInputs[0].toUpperCase().equals("TERMINATE") && !commandInputs[0].toUpperCase().equals("BYE")){
            return -1;
        }
        else if(commandInputs.length < 3){
            return -2;
        }
        else if(commandInputs.length > 5){
            return -3;
        }
        else if(commandInputs.length == 3 && (!isValidInteger(commandInputs[1]) || !isValidInteger(commandInputs[2]))){
            return -4;
        }
        else if(commandInputs.length == 4 && (!isValidInteger(commandInputs[1]) || !isValidInteger(commandInputs[2]) || !isValidInteger(commandInputs[3]))){
                return -4;
        }
        else if(commandInputs.length == 5 && (!isValidInteger(commandInputs[1]) || !isValidInteger(commandInputs[2]) || !isValidInteger(commandInputs[3]) || !isValidInteger(commandInputs[4]))){
                return -4;
        }
        else if(commandInputs[0].toUpperCase().equals("ADD")){
            int sum = Integer.parseInt(commandInputs[1]);
            for(int i = 2; i < commandInputs.length; i++){
                sum += Integer.parseInt(commandInputs[i]);
            }
            return sum;
        }
        else if(commandInputs[0].toUpperCase().equals("SUBTRACT")){
            int total = Integer.parseInt(commandInputs[1]);
            for(int i = 2; i < commandInputs.length; i++){
                total -= Integer.parseInt(commandInputs[i]);
            }
            return total;
        }
        else if(commandInputs[0].toUpperCase().equals("MULTIPLY")){
            int product = Integer.parseInt(commandInputs[1]);
            for(int i = 2; i < commandInputs.length; i++){
                product *= Integer.parseInt(commandInputs[i]);
            }
            return product;
        }
        //Using a negative number for errors, if the commands do not fall under any of the above commands, then return -6 error. This is not handled on the client side.
        return -6;
    }

    public static boolean isValidInteger(String s) {
        //Function to check if the string is an integer or not. Code from https://learn-java-by-example.com/java/check-java-string-integer/.
        boolean isValidInteger = false;
        try
        {
           Integer.parseInt(s);
           isValidInteger = true;
        }
        catch (NumberFormatException ex)
        {                               }
        return isValidInteger;
    }
}