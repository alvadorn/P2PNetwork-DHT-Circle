package br.ufes.ceunes.p2pnetwork.actions;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Queue;

import br.ufes.ceunes.p2pnetwork.model.Host;

public class PacketReceiver {

	private Host host;
	private Host successor;
	private Host antecessor;
	private Queue<DatagramPacket> packets;
	private final int port = 12345;

	public PacketReceiver(Host host, Host successor, Host antecessor, Queue<DatagramPacket> queue) {
		this.host = host;
		this.successor = successor;
		this.antecessor = antecessor;
		packets = queue;
	}

	public void requireLookUp(ByteArrayInputStream stream) {
		byte[] buffer = new byte[4];
		stream.read(buffer, 0, 4);
		long originId = Converter.bytesToUnsignedInt(buffer);
		stream.read(buffer, 0, 4);
		InetAddress originIp = null;
		try {
			originIp = InetAddress.getByAddress(buffer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stream.read(buffer, 0, 4);
		long lookingId = Converter.bytesToUnsignedInt(buffer);

		boolean make = false;

		/*
		 * if (host.getId() > successor.getId()) { if (host.getId() <= lookingId
		 * || successor.getId() > lookingId) { make = true; } } else if
		 * (host.getId() >= lookingId && successor.getId() < lookingId) { make =
		 * true; }
		 */

		if (host.isNext(lookingId, successor.getId())) {
			packets.add(PacketFactory.createAnswerLookUp(originIp, port, (int) lookingId, (int) successor.getId(),
					successor.getIp()));
		} else {
			packets.add(PacketFactory.createRequireLookUp(successor.getIp(), port, (int) originId, originIp,
					(int) lookingId));
		}

	}

	public void requireJoin(ByteArrayInputStream stream, InetAddress fromIp) {
		byte buffer[] = new byte[4];
		stream.read(buffer, 0, 4);
		long lookingId = Converter.bytesToUnsignedInt(buffer);
		byte failure;
		if (host.isNext(lookingId, successor.getId()) && host.getId() != lookingId) {
			failure = 0;
		} else {
			failure = 1;
		}
		packets.add(PacketFactory.createAnswerJoin(fromIp, port, failure, (int) successor.getId(), successor.getIp(),
				(int) antecessor.getId(), antecessor.getIp()));
	}

	public void requireLeave(ByteArrayInputStream stream) {

	}

	public void requireUpdate(ByteArrayInputStream stream) {

	}

	public static void answerLookUp() {

	}

	public static void answerJoin() {

	}

	public static void answerLeave() {

	}

	public static void answerUpdate() {

	}

}
