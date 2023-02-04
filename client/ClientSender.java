package client;

import java.lang.Thread;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import message.Message;
import message.MessageTypes;
import java.io.IOException;
import client.Client;

public class ClientSender extends Thread {

  public ClientSender() {

  }

  public void run() {
    try {
      // Init. variables
      Scanner scanner = new Scanner(System.in);
      // ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
      String userInput = "";

      // Infinite loop to gather input to send to server
      while(true) {
        // Get user input
        userInput = scanner.nextLine();


        // Modify input into type Message
        Message message = craftMessage(userInput);

        // Send message to all peers
        Client.sendMessageToPeers(message);
      }
    }
    catch(Exception error) {
      System.out.println("I/O error: " + error);
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
    if(firstWord.contains("NOTE")){
      type = MessageTypes.NOTE;
      userInput = userInput.replace("NOTE", "");
    }
    else if(firstWord.contains("LEAVE")){
      type = MessageTypes.LEAVE;
      userInput = userInput.replace("LEAVE", "");
    }
    else if(firstWord.contains("SHUTDOWN_ALL")){
      type = MessageTypes.SHUTDOWN_ALL;
      userInput = userInput.replace("SHUTDOWN_ALL", "");
    }
    else if(firstWord.contains("SHUTDOWN")){
      type = MessageTypes.SHUTDOWN;
      userInput = userInput.replace("SHUTDOWN", "");
    }
    else if(firstWord.contains("JOIN")){
      type = MessageTypes.JOIN;
      userInput = userInput.replace("JOIN", "");
    }

    // Create new message and return it
    // TODO: ADD NAME
    return new Message(type, ":" + userInput);
  }

}
