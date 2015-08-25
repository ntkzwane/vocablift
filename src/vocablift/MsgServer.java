package vocablift;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

/*
 * A chat server that deliveres public and private messages
 */

public class MsgServer{
    // create the server socket
    private static ServerSocket serverSocket = null;
    // create the client socket
    private static Socket clientSocket = null;

    // this chat server can accept up to maxClientsCount clients' connections
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount];

    //public static void main(String args[]){
    public MsgServer(){
        int portNumber = 2222;
        System.out.println("Usaage: java MultiThreadedChatServer <portNumber>\n" + "Now using port number = " + portNumber);

        /*
         * Open a server socket on the portNumber (default 2222)
         */
        try{
                serverSocket = new ServerSocket(portNumber);
        }
        catch(IOException e){System.out.println(e);}

        /*
         * Create a client socket for each connection and pass and pass it to a new client
         * thread 
         */
        while(true){
                try{
                        clientSocket = serverSocket.accept();
                        int i = 0;
                        for(i = 0; i < maxClientsCount; i++){
                                if(threads[i] == null){
                                        threads[i] = new clientThread(clientSocket, threads);
                                        threads[i].start();
                                        break;
                                }
                        }
                        if(i == maxClientsCount){
                                PrintStream outStream = new PrintStream(clientSocket.getOutputStream());
                                outStream.println("The server is too busy. Try again later.");
                                outStream.close();
                                clientSocket.close();
                        }
                }catch(IOException e){System.err.println(e);}
        }

    }
}

class clientThread extends Thread{
    private String clientName = null;
    private DataInputStream inStream = null;
    private PrintStream outStream = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;

    public clientThread(Socket clientSocket, clientThread[] threads){
            this.clientSocket = clientSocket;
            this.threads = threads;
            maxClientsCount = threads.length;
    }

    public void run(){
            int maxClientsCount = this.maxClientsCount;
            clientThread[] threads = this.threads;

            try{
                    /*
                     * create the input and output streams for this speceific client
                     */
                    inStream = new DataInputStream(clientSocket.getInputStream());
                    outStream = new PrintStream(clientSocket.getOutputStream());
                    String name; // the current client's name
                            // keep requesting the name till a valid one is entered
                    while(true){
                            outStream.println("Enter your name.");
                            name = inStream.readLine().trim();
                            if(name.indexOf('@') == -1){
                                    break;				
                            }
                            else{outStream.println("The name should not contain the '@' character");}
                    }

                    /* welcome the new client to the chat service */
                    outStream.println("Welcome " + name + "to our chat room. \nTo leave, enter /quit in a new line.");
                    synchronized(this){
                    // store the client's name
                            for(int i = 0; i < maxClientsCount; i++){
                                    if(threads[i] != null && threads[i] == this){
                                            clientName = "@" + name;
                                            break;
                                    }
                            }
                    }
                    for(int i = 0; i < maxClientsCount; i++){
                            if(threads[i] != null && threads[i] != this){
                                    threads[i].outStream.println("*** A new user " + name + " entered the chat room !!! ***");
                            }
                    }
                    /* start the conversation */
                    while(true){
                            String line = inStream.readLine();
                            if(line.startsWith("/quit"))
                                    break;
                            /* if the message is private, send it to the respective client */
                            if(line.startsWith("@")){
                                    String[] words = line.split("\\s",2); // split the line on the spaces
                                    if(words.length > 1 && words[1] != null){
                                            words[1] = words[1].trim();
                                            if(!words[1].isEmpty()){
                                                    synchronized(this){
                                                            for(int i = 0; i < maxClientsCount; i++){
                                                                    if(threads[i] != null && threads[i] != this && threads[i].clientName != null && threads[i].clientName.equals(words[0])){
                                                                            threads[i].outStream.println("<"+name+">" + words[1]);
                                                                    /* echo this back to the client to confirm that it was sent */
                                                                    this.outStream.println(">"+name+">" + words[1]);
                                                                    break;
                                                                    }
                                                            }
                                                    }
                                            }
                                    }				
                            }
                            else{
                                    /* the message is publicised to all other clients */
                                    synchronized(this){
                                            for(int i = 0; i < maxClientsCount; i++){
                                                    if(threads[i] != null && threads[i].clientName != null){
                                                            threads[i].outStream.println("<" + name + ">" + line);
                                                    }
                                            }
                                    }
                            }
                    }
                    // if the code exits the while loop, then the client has exited the chat room
                    synchronized(this){
                            for(int i = 0; i < maxClientsCount; i++){
                                    if(threads[i] != null && threads[i] != this  && threads[i].clientName != null){
                                            threads[i].outStream.println("*** The user " + name + " is leaving the chat room !!! ***");
                                    }
                            }
                    }
                    outStream.println("*** Bye " + name + " ***");

                    /*
                     * clean up. set current thread to null os that it can be available for a new client,
                     */
                     synchronized(this){
                             for(int i = 0; i < maxClientsCount; i++){
                                     if(threads[i] == this)
                                             threads[i] = null;
                             }
                     }

                     /*
                      * close the I/O streams, also close the socket created for this client
                      */
                    inStream.close();
                    outStream.close();
                    clientSocket.close();
            }catch(IOException e){System.err.println(e);}
    }
}
