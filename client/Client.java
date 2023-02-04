package client;

import java.util.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import message.Message;
import message.MessageTypes;
import utils.PropertyHandler;

public class Client {

  static List<NodeInfo> users = new ArrayList<NodeInfo>();

  public static void main(String[] args) {
    // Init. variables
    Scanner scanner = new Scanner(System.in);

    // Create server socket


    // Prompt user for action of create new chat or join existing chat
      // Loop until user input is valid

    // If join existing chat
      // joinExistingChat()

    // Otherwise, create existing chat
      // createNewChat()

  runServer();
    // Infinite loop
      // Accept incoming requests
        // Create new ClientReciever thread for each request

    // Start sender and reciever threads
      // createSenderAndRecieverThreads()
  }

  private static void runServer() {
    try {
      ServerSocket serverSocket = new ServerSocket(8888);
      Socket clientConnection = null;

      new ClientSender().start();
      // new ClientReciever().start();

      while(true) {
        clientConnection = serverSocket.accept();

        new ClientReciever(clientConnection).start();
      }

    }

    catch(Exception error) {

    }
  }

  private static void joinExistingChat() {
    // Get IP and port from config file
    // Attempt to connect to existing chat network
    try {
      // Init PropertyHandler and get ip and port
      PropertyHandler propertyHandler = new PropertyHandler(
                                              "client/ClientConfig.txt");
      String ip = propertyHandler.getProperty("IP");
      int port = Integer.parseInt(propertyHandler.getProperty("PORT"));

      // Connect to known ip and port
      Socket peerConnection = new Socket(ip, port);
      ObjectOutputStream outputStream = new ObjectOutputStream(
                                              peerConnection.getOutputStream());
      ObjectInputStream inputStream = new ObjectInputStream(
                                              peerConnection.getInputStream());
      Message joinMessage = new Message(MessageTypes.JOIN, "");
      outputStream.writeObject(joinMessage);
      outputStream.flush();

      // TODO: Find out how to get list
      // users = (List<NodeInfo>)((Message)inputStream.readObject()).getContent();
    }
    catch (Exception error){

    }

    // Get list of active users
  }

  public static void sendMessageToPeers(Message message) {

  }

  private static void createNewChat() {
    // Get port from config file
  }

  private static void createSenderAndRecieverThreads() {
    // Start Client reciever thread
    // Start Client sender thread
  }

  // Close Program
  private static void stopProgram() {
    System.out.println("Shuting down chat program");
    System.exit(0);
  }
}
