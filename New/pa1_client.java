import java.io.*;
import java.net.*;

class ClientTCPConnectionResources{
    //Deals with creating, saving and deleting TCP connection resources.
    private String serverName;
    private int port;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientTCPConnectionResources(String serverName, int port){
        this.serverName = serverName;
        this.port = port;
        try{
            Socket socket = new Socket(serverName, port);
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        }
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    public String getServerName() {
        return serverName;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String toString(){
        return "Connected To: " + serverName + " On Port: " + port;
    }

    public void closeResources() {
        try{
            socket.close();
        }
        catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

class StringTCPHandler{
    //Handles sending and receiving information as strings.
    public void sendData(String data, OutputStream outputStream){
        PrintWriter writer = new PrintWriter(outputStream, true);
        writer.println(data);
    }

    public String readData(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String output = "";
        try{
            output = reader.readLine(); 
        }
        catch(Exception e){
            return "";
        }
        return output;
    }
}

class DataConvertor {
    //Specific conversion of output
    public String toString(String response){
        if(response.equals("-1")){
            return "incorrect operation command.";
        }
        else if(response.equals("-2")){
            return "number of inputs is less than two.";
        }
        else if(response.equals("-3")){
            return "number of inputs is more than four.";
        }
        else if(response.equals("-4")){
            return "one or more of the inputs contain(s) non-number(s).";
        }
        else if(response.equals("-5")){
            return "exit.";
        } 
        else { 
            return response;
        }
    }
}

class ClientApplication{
    //Runs the main application along with handling user inputs and printing to console.
    public static void main(String argv[]) throws Exception{
        String serverName = argv[0];
        int port = Integer.parseInt(argv[1]);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        try{
            ClientTCPConnectionResources TCPConnectionResources = new ClientTCPConnectionResources(serverName, port);
            System.out.println(TCPConnectionResources);
            StringTCPHandler handler = new StringTCPHandler();
            DataConvertor convertor = new DataConvertor();

            System.out.println(handler.readData(TCPConnectionResources.getInputStream()));

            while(true){
                handler.sendData(userInput.readLine(), TCPConnectionResources.getOutputStream());
                String response = handler.readData(TCPConnectionResources.getInputStream());
                System.out.println(convertor.toString(response));

                if(response.equals("-5")){
                    break;
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}