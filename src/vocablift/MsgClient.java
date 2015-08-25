package vocablift;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgClient implements Runnable {

	// create the client socket
	private static Socket clientSocket = null;
	// create the output stream
	private static PrintStream outStream = null;
	// create teh input stream
	private static DataInputStream inStream = null;
	private static BufferedReader inputLine = null;

	// the status of the connection
	private static boolean closed = false;
	
        public MsgClient(){}
        
	//public static void main(String[] args) {
        public MsgClient(String host){
		// the default port number
		//////// maybe change this such that the user supplies the number
		int portNumber = 2222;
                System.out.println("Usage : java MultiThreadedChatClient <host> <portNumber>\n" + "Now using host = "+ host +", portNumber = "+ portNumber);

		/*
		 * Open a socket on a given host and port. Open an input and output stream
		 */
		try{
			clientSocket = new Socket(host,portNumber);
			inputLine = new BufferedReader(new InputStreamReader(System.in));
			outStream = new PrintStream(clientSocket.getOutputStream());
			inStream = new DataInputStream(clientSocket.getInputStream());
		} catch(UnknownHostException e){
			System.err.println("Don't know about host " + host);
		} catch(IOException e){
			System.err.println("Couldn't get I/O for the connection to the host " + host);
		}

		/*
		 * If all goes well in initiation, we want to write some stuff to the socket we have opened a
		 * connection to on port number portNumber
		 */
		if(clientSocket != null && outStream != null && inStream != null){
			try{
				/* Create a thread to read from the server */
				new Thread(new MsgClient()).start();
				while (!closed){
					outStream.println(inputLine.readLine().trim());
				}
				/*Close te output stream, close the input stream, close the socket*/
				outStream.close();
				inStream.close();
				clientSocket.close();
			} catch(IOException e){System.err.println("IOException: " + e);}
		}
	}
	
	/*
	 * Create a thread to read from the server
	 */
	public void run(){
		/*
		 * Keep on reading from the socket till we recieve "Bye" from the server, then we can break.
		 */
		String responseLine;
		try{
			while ((responseLine = inStream.readLine()) != null){
				System.out.println(responseLine);
				if(responseLine.indexOf("*** Bye") != -1)
					break;
			}
			closed = true;
		} catch(IOException e){System.err.println("IOException: " + e);}
	}
}
