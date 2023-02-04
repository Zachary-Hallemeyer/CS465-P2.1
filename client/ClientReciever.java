package client;

import java.lang.Thread;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.util.Scanner;
import message.Message;
import message.MessageTypes;
import java.io.IOException;


public class ClientReciever extends Thread {

  Socket socket;
  // NodeInfo clientInfo;

  public ClientReciever(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      // Create a new input stream for client
      ObjectInputStream inputStream = new ObjectInputStream(
                                            socket.getInputStream());

      Message newMessage = (Message) inputStream.readObject();
      System.out.print(newMessage.getContent());

      // Check if join message
        // Send active client list


      inputStream.close();
      socket.close();
    }
    catch(Exception error) {

    }
  }

  // FOR SONI
  private void parseMessage(Message message) {
    // Check which command is used

    // Print message
  }
}
