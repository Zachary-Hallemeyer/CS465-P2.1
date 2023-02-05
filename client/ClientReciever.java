package client;

import java.lang.Thread;
import java.net.Socket;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import message.Message;
import message.MessageTypes;
import utils.NetworkUtilities;

/**
 * Class [ClientReciever] is created when a peer attempts to connect to this
 * client. This class then processes the info and prints peer messsage to
 * console
 *
 * @author Zachary M. Hallemeyer
 */
public class ClientReciever extends Thread {

  Socket socket;

  public ClientReciever(Socket socket) {
    this.socket = socket;
  }

  // Accepts peer data and uses parseMessage() to process the info
  public void run() {
    try {
      // Create a new input stream for client
      ObjectInputStream inputStream = new ObjectInputStream(
                                            socket.getInputStream());

      Message newMessage = (Message) inputStream.readObject();

      // Check if join message
        // Send active client list
      parseMessage(newMessage);


      inputStream.close();
      socket.close();
    }
    catch(Exception error) {
      System.out.println("Could not process message from peer: " + error);
    }
  }

  // Processes message depending on the COMMAND associated with the message
  // and prints message to console
  private void parseMessage(Message message) {
    String name = message.getContent().toString().split(": ")[0];
    // Check which command is used

    // Check if join command
    if(message.getType() == MessageTypes.JOIN) {
      try {
        Client.users.add((NodeInfo) message.getContent());
        ObjectOutputStream outputStream = new ObjectOutputStream(
                                            socket.getOutputStream());
        outputStream.writeObject(new Message(MessageTypes.JOIN, Client.users));
        outputStream.flush();
        outputStream.close();
      }
      catch(Exception error) {
        System.out.println("Something went wrong: " + error);
      }
    }
    // Check if Shutdown command
    // Check if LEAVE command
    else if(message.getType() == MessageTypes.SHUTDOWN
            || message.getType() == MessageTypes.LEAVE) {

      // Split string with name (index of 0) and message content (index of 1)
      String[] messageArray = message.getContent().toString().split(": ");
      // Get remote port by spliting message content with " "
      String remotePortString = messageArray[1].split(" ")[0];
      // Get rest of message content and delete port number from string
      String messageString = messageArray[1].replace(remotePortString, "");
      // Get int port from string port
      int remotePort = Integer.valueOf(remotePortString);
      // Get remote ip and remove remote port and "/" from string
      String remoteIP = socket.getRemoteSocketAddress().toString()
                                            .replace("/", "").split(":")[0];

      // Remove user from user list
      Client.removeUserFromUserList(remoteIP, remotePort);

      // Print user message
      System.out.println(messageString);

      // Print user left message
      System.out.println(name + " has left the chat");
    }
    // Check if Shutdown all command
    else if(message.getType() == MessageTypes.SHUTDOWN_ALL) {
      // Print user message
      System.out.println(message.getContent());

      // Terminate program
      Client.stopProgram();
    }
    // Otherwise assume note command
    else {
      // Print message
      System.out.println(message.getContent());
    }
  }
}
