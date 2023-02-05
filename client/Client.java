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
import utils.NetworkUtilities;


public class Client {

  static List<NodeInfo> users = new ArrayList<NodeInfo>();
  static String name;
  static int port;

  public static void main(String[] args) {
    // Init. variables
    Scanner scanner = new Scanner(System.in);
    boolean joinChat;
    int string;

    // Prompt user for action of create new chat or join existing chat
      // Print join options and get options
    System.out.println(NetworkUtilities.getMyIP());
    System.out.println("Enter 1 for use join existing chat"
                       + "\nEnter 2 to create new chat");
    if(scanner.nextLine().equals("1")) {
      joinChat = true;
    }
    else {
      joinChat = false;
    }

    // Prompt user for name
    System.out.println("Enter Name");
    name = scanner.nextLine();

    // Prompt user for port number to use to listen on
    System.out.println("Enter Port");
    port = Integer.valueOf(scanner.nextLine());

    // If join existing chat
    if(joinChat) {
      joinExistingChat(port);
    }
    // Otherwise, create existing chat
    else {
      createNewChat(port);
    }

    // Run server and listen for requests
    runServer(port);
  }

  // Starts a server socket
  // When a request is accepted a ClientReciever thread is created to process
  // request
  private static void runServer(int port) {
    System.out.println("Server started");
    try {
      // Start server and init. variables
      ServerSocket serverSocket = new ServerSocket(port);
      Socket clientConnection = null;

      // Start ClientSender thread to handle user input
      new ClientSender().start();

      // Infinite loop to listen to incoming requests
      while(true) {
        // Accept new request
        clientConnection = serverSocket.accept();

        // Starts ClientReciever thread
        new ClientReciever(clientConnection).start();
      }

    }

    catch(Exception error) {
      System.out.println("Something went wrong: " + error);
    }
  }

  @SuppressWarnings("unchecked")
  private static void joinExistingChat(int port) {
    // Attempt to connect to existing chat network
    try {
      // Init PropertyHandler and get ip and port
      PropertyHandler propertyHandler = new PropertyHandler(
                                              "client/ClientConfig.txt");
      // Get IP and port from config file
      String ip = propertyHandler.getProperty("IP");
      int connectPort = Integer.parseInt(propertyHandler.getProperty("PORT"));

      // Connect to known ip and port
      System.out.println("Attempting connection to " + ip + " at " + connectPort);
      Socket peerConnection = new Socket(ip, connectPort);
      ObjectOutputStream outputStream = new ObjectOutputStream(
                                              peerConnection.getOutputStream());
      System.out.println("Connection successful!");

      // Send join message with this client's ip and port info
      NodeInfo thisClientInfo = new NodeInfo(NetworkUtilities.getMyIP(), port);
      Message joinMessage = new Message(MessageTypes.JOIN, thisClientInfo);
      outputStream.writeObject(joinMessage);
      outputStream.flush();

      // Accept the join data containing user list
      ObjectInputStream inputStream = new ObjectInputStream(
                                              peerConnection.getInputStream());
      Message newMessage = (Message)inputStream.readObject();
      // Get list of active users
      users = (List<NodeInfo>)newMessage.getContent();
    }
    catch (Exception error){
      System.out.println("Something went wrong: " + error);
    }
  }

  // Add this client's ip and port to users list
  private static void createNewChat(int port) {
    // Get port from config file
    System.out.println("Creating new chat");
    users.add(new NodeInfo(NetworkUtilities.getMyIP(),port));
  }

  // Sends message to all peers
  public static void sendMessageToPeers(Message message) {
    // Init. variables
    String thisIP = NetworkUtilities.getMyIP();
    ObjectOutputStream outputStream;
    Socket socket;

    // Loop through user list
    for(int index = 0; index < users.size(); index++) {
      // Check if current user is not this client
      if( !users.get(index).getIP().equals(thisIP)
          ||
          users.get(index).getPort() != port) {
        try {
          // Connect to client
          socket = new Socket(users.get(index).getIP(), users.get(index).getPort());

          // Send message to client
          outputStream = new ObjectOutputStream(socket.getOutputStream());
          outputStream.writeObject(message);
          outputStream.flush();

          // Close connection
          outputStream.close();
          socket.close();
        }
        catch(Exception error) {
          System.out.println("Something went wrong: " + error);
        }
      }
    }
  }

  // Removes user from list based on the ip and port provided
  public static void removeUserFromUserList(String userIP, int userPort) {
    for(int index = 0; index < users.size(); index++) {
      if(   users.get(index).getIP().equals(userIP)
         && users.get(index).getPort() == userPort) {
           users.remove(users.get(index));
      }
    }
  }

  // Prints closing message and closes program
  public static void stopProgram() {
    System.out.println("Shuting down chat program");
    System.exit(0);
  }
}
