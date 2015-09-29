package br.ufes.ceunes.p2pnetwork.actions;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class PacketFactory {

	public static DatagramPacket createSendJoin(InetAddress address, int port, Integer id) {
		byte[] data = new byte[5];
		data[0] = 0;
		System.arraycopy(Converter.intToBytes(id), 0, data, 1, 4);
		// code, Converter.intToBytes(id)
		DatagramPacket packet = new DatagramPacket(data, 5, address, port);
		// conteúdo - numero de bytes - ip - porta
		return packet;
	}

	public static DatagramPacket createSendLookUp(InetAddress hostIP, int port, Integer originId, InetAddress originIP,
			Integer searchId) {
		byte[] data = new byte[13];
		data[0] = 2;
		System.arraycopy(Converter.intToBytes(originId), 0, data, 1, 4);
		System.arraycopy(originIP.getAddress(), 0, data, 5, 4);
		System.arraycopy(Converter.intToBytes(searchId), 0, data, 9, 4);

		DatagramPacket packet = new DatagramPacket(data, 13, hostIP, port);

		return packet;
	}
}
