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

	public void requireJoin(ByteArrayInputStream stream, InetAddress fromIp) {
		byte buffer[] = new byte[4];
		stream.read(buffer, 0, 4);
		long lookingId = Converter.bytesToUnsignedInt(buffer);
		byte failure;
		int oldAntecessorId = (int) antecessor.getId();
		InetAddress oldAntecessorIp = antecessor.getIp();
		if (host.getId() == lookingId) {
			failure = 0;
		} else {
			failure = 1;
			antecessor.setId((int) lookingId);
			antecessor.setIp(fromIp);

		}
		packets.add(PacketFactory.createAnswerJoin(fromIp, port, failure, (int) host.getId(), host.getIp(),
				oldAntecessorId, oldAntecessorIp));
	}

	public void answerJoin(ByteArrayInputStream stream) {
		byte confirmation = (byte) stream.read();
		byte buffer[] = new byte[4];
		stream.read(buffer, 0, 4);
		long successorId = Converter.bytesToUnsignedInt(buffer);
		stream.read(buffer, 0, 4);
		InetAddress successorIp = null;
		InetAddress antecessorIp = null;
		long antecessorId = 0;
		try {
			successorIp = InetAddress.getByAddress(buffer);
			stream.read(buffer, 0, 4);
			antecessorId = Converter.bytesToUnsignedInt(buffer);
			stream.read(buffer, 0, 4);
			antecessorIp = InetAddress.getByAddress(buffer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (confirmation == 1) {
			InetAddress oldAntecessorIp = antecessor.getIp();
			antecessor.setId((int) antecessorId);
			antecessor.setIp(antecessorIp);
			successor.setIp(successorIp);
			successor.setId((int) successorId);
			packets.add(PacketFactory.createSendUpdate(antecessor.getIp(), port, (int) host.getId(), (int) host.getId(),
					host.getIp()));
		} else {
			int newId = Util.generateId();
			host.setId(newId);
			packets.add(PacketFactory.createRequireLookUp(successorIp, port, newId, host.getIp(), newId));
		}

	}

	public void requireLeave(ByteArrayInputStream stream, InetAddress address) {
		long selfId = host.getId();
		byte buffer[] = new byte[4];

		stream.read(buffer, 0, 4);

		long id = Converter.bytesToUnsignedInt(buffer);
		stream.read(buffer, 0, 4);
		long nextId = Converter.bytesToUnsignedInt(buffer);
		stream.read(buffer, 0, 4);
		InetAddress nextIp = null;
		try {
			nextIp = InetAddress.getByAddress(buffer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stream.read(buffer, 0, 4);
		long beforeId = Converter.bytesToInt(buffer);
		stream.read(buffer, 0, 4);
		InetAddress beforeIp = null;
		try {
			beforeIp = InetAddress.getByAddress(buffer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Suc: " + successor.getId() + " Ant: " + antecessor.getId() + " id: " + host.getId()
				+ " nextId: " + nextId + " beforeId: " + beforeId);

		if (nextId == beforeId) {
			successor.setId((int) nextId);
			successor.setIp(nextIp);
			antecessor.setId((int) beforeId);
			antecessor.setIp(beforeIp);

		} else if (selfId != nextId) {
			successor.setId((int) nextId);
			successor.setIp(nextIp);
		} else {
			antecessor.setId((int) beforeId);
			antecessor.setIp(beforeIp);
		}

		/*
		 * if (id > successor.getId() || id > selfId) { successor.setId((int)
		 * nextId); successor.setIp(nextIp); } else { antecessor.setId((int)
		 * beforeId); antecessor.setIp(beforeIp); }
		 */

		packets.add(PacketFactory.createAnswerLeave(address, port, (int) selfId));
	}

	public void answerLeave(ByteArrayInputStream stream) {

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

	public void answerLookUp(ByteArrayInputStream stream) {
		byte buffer[] = new byte[4];
		stream.read(buffer, 0, 4);
		stream.read(buffer, 0, 4);
		stream.read(buffer, 0, 4);
		InetAddress successorIp = null;
		try {
			successorIp = InetAddress.getByAddress(buffer);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		packets.add(PacketFactory.createSendJoin(successorIp, port, (int) host.getId()));
	}

	public void requireUpdate(ByteArrayInputStream stream, InetAddress fromIp) {
		byte buffer[] = new byte[4];
		stream.read(buffer, 0, 4);
		long originId = Converter.bytesToUnsignedInt(buffer);
		stream.read(buffer, 0, 4);
		long nextId = Converter.bytesToUnsignedInt(buffer);
		stream.read(buffer, 0, 4);
		InetAddress nextIp = null;
		try {
			nextIp = InetAddress.getByAddress(buffer);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		successor.setId((int) nextId);
		successor.setIp(nextIp);
		packets.add(PacketFactory.createAnswerUpdate(fromIp, port, (byte) 1, (int) originId));
	}

	public void answerUpdate(ByteArrayInputStream stream) {

	}

}
