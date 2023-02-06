package client;

import java.lang.Thread;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import message.Message;
import message.MessageTypes;
import java.io.IOException;
import client.Client;
import utils.NetworkUtilities;

/**
 * Class [ClientSender] handles user input from command line, processes input,
 * and sends it to all peers
 *
 * @author Zachary M. Hallemeyer
 */
public class ClientSender extends Thread {

  // Runs an infinite and sends all user input to peers
  public void run() {
    try {
      // Init. variables
      Scanner scanner = new Scanner(System.in);
      String userInput = "";

      // Infinite loop to gather input to send to server
      while(true) {
        // Get user input
        userInput = scanner.nextLine();

        // Modify input into type Message
        Message message = craftMessage(userInput);

        // Send message to all peers
        Client.sendMessageToPeers(message);

        // Check if LEAVE/SHUTDOWN/SHUTDOWN_ALL
        if(   message.getType() == MessageTypes.LEAVE
           || message.getType() == MessageTypes.SHUTDOWN
           || message.getType() == MessageTypes.SHUTDOWN_ALL) {
             // Shutdown program
             Client.stopProgram();
           }
      }
    }
    catch(Exception error) {
      System.out.println("Could not send data to peers" + error);
    }
  }

  // Creates Message object with userInput and returns the Message Object
  private Message craftMessage(String userInput) {
    // Init. variables
    int type = MessageTypes.NOTE;
    String message = "";
    String firstWord = userInput.split(" ")[0];

    // Check if any valid command is used
    // If there is a command, set type accordingly to command
    // and delete command from input
    if(firstWord.contains("NOTE")){ // Check for NOTE command
      type = MessageTypes.NOTE;
      userInput = userInput.replace("NOTE", "");
    }
    else if(firstWord.contains("LEAVE")){ // Check for LEAVE command
      type = MessageTypes.LEAVE;
      userInput = userInput.replace("LEAVE", Client.port + " ");
    }
    else if(firstWord.contains("SHUTDOWN_ALL")){ // Check for SHUTDOWN_ALL command
      type = MessageTypes.SHUTDOWN_ALL;
      userInput = userInput.replace("SHUTDOWN_ALL", "");
    }
    else if(firstWord.contains("SHUTDOWN")){  // Check for SHUTDOWN command
      type = MessageTypes.SHUTDOWN;
      userInput = userInput.replace("SHUTDOWN", Client.port + " ");
    }
    else if(firstWord.contains("JOIN")){  // Check for JOIN
      type = MessageTypes.JOIN;
      userInput = userInput.replace("JOIN", "");
    }

    // Create new message and return it
    return new Message(type, Client.name + ": " + userInput);
  }

}
