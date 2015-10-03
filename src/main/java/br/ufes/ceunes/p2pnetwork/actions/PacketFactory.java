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

	public static DatagramPacket createAnswerJoin(InetAddress address, int port, byte status, int successorId,
			InetAddress successorIp, int antecessorId, InetAddress antecessorIp) {
		byte[] data = new byte[18];
		data[0] = (byte) 128;
		data[1] = status;
		System.arraycopy(Converter.intToBytes(successorId), 0, data, 2, 4);
		System.arraycopy(successorIp.getAddress(), 0, data, 6, 4);
		System.arraycopy(Converter.intToBytes(antecessorId), 0, data, 10, 4);
		System.arraycopy(antecessorIp.getAddress(), 0, data, 14, 4);

		DatagramPacket packet = new DatagramPacket(data, 18, address, port);
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

	public static DatagramPacket createAnswerLookUp(InetAddress address, int port, int lookingId, int successorId,
			InetAddress successorIp) {
		byte[] data = new byte[13];
		data[0] = (byte) 130;
		System.arraycopy(Converter.intToBytes(lookingId), 0, data, 1, 4);
		System.arraycopy(Converter.intToBytes(successorId), 0, data, 5, 4);
		System.arraycopy(successorIp.getAddress(), 0, data, 9, 4);
		DatagramPacket packet = new DatagramPacket(data, 13, address, port);
		return packet;
	}

	public static DatagramPacket createRequireLookUp(InetAddress address, int port, int originId, InetAddress originIp,
			int lookingId) {
		byte[] data = new byte[13];
		data[0] = 2;
		System.arraycopy(Converter.intToBytes(originId), 0, data, 1, 4);
		System.arraycopy(originIp.getAddress(), 0, data, 5, 4);
		System.arraycopy(Converter.intToBytes(lookingId), 0, data, 9, 4);
		DatagramPacket packet = new DatagramPacket(data, 13, address, port);
		return packet;
	}

	public static DatagramPacket createSendUpdate(InetAddress address, int port, int originId, int sucessorId,
			InetAddress sucessorIp) {
		byte[] data = new byte[13];
		data[0] = (byte) 3;
		System.arraycopy(Converter.intToBytes(originId), 0, data, 1, 4);
		System.arraycopy(Converter.intToBytes(sucessorId), 0, data, 5, 4);
		System.arraycopy(sucessorIp.getAddress(), 0, data, 9, 4);
		DatagramPacket packet = new DatagramPacket(data, 13, address, port);
		return packet;
	}

	public static DatagramPacket createAnswerUpdate(InetAddress address, int port, byte confirmation, int originId) {
		byte[] data = new byte[6];
		data[0] = (byte) 131;
		data[1] = confirmation;
		System.arraycopy(Converter.intToBytes(originId), 0, data, 2, 4);
		DatagramPacket packet = new DatagramPacket(data, 6, address, port);
		return packet;
	}

	public static DatagramPacket createSendLeave(InetAddress address, int port, int leavingId, int leavingSucessorId,
			InetAddress leavingSucessorIp, int leavingAntecessorId, InetAddress leavingAntecessorIp) {
		byte[] data = new byte[21];
		data[0] = (byte) 1;
		System.arraycopy(Converter.intToBytes(leavingId), 0, data, 1, 4);
		System.arraycopy(Converter.intToBytes(leavingSucessorId), 0, data, 5, 4);
		System.arraycopy(leavingSucessorIp.getAddress(), 0, data, 9, 4);
		System.arraycopy(Converter.intToBytes(leavingAntecessorId), 0, data, 13, 4);
		System.arraycopy(leavingAntecessorIp.getAddress(), 0, data, 17, 4);
		DatagramPacket packet = new DatagramPacket(data, 21, address, port);
		return packet;
	}

	public static DatagramPacket createAnswerLeave(InetAddress address, int port, int originId) {
		byte[] data = new byte[5];
		data[0] = (byte) 129;
		System.arraycopy(Converter.intToBytes(originId), 0, data, 1, 4);
		DatagramPacket packet = new DatagramPacket(data, 5, address, port);
		return packet;
	}
}
