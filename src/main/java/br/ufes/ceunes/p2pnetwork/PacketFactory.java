package br.ufes.ceunes.p2pnetwork;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class PacketFactory {

  public static DatagramPacket createSendJoin(InetAddress address, int port, byte code, Integer id) {
    byte[] data = new byte[5];
    data[0] = code;
    System.arraycopy(Converter.intToBytes(id), 0, data, 1, 4);
    // code, Converter.intToBytes(id)
    DatagramPacket packet = new DatagramPacket(data,5,address,port);
    // conteúdo - numero de bytes - ip - porta
    return packet;
  }
}
