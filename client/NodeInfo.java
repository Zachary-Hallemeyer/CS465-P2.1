package client;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Class [NodeInfo] defines an object to containing the necessary information
 * to keep track as well as send data to clients from server
 *
 * @author Zachary M. Hallemeyer
 */
public class NodeInfo implements Serializable{
  String ip;
  int port;

  public NodeInfo(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }

  public String getIP() {
    return ip;
  }

  public int getPort() {
    return port;
  }
}
