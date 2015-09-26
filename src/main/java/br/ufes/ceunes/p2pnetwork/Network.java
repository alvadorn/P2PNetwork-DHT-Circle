package br.ufes.ceunes.p2pnetwork;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Pattern;
import java.time.LocalDate;

public class Network {

	private InetAddress predecessorIp;
	private InetAddress successorIp;
	private InetAddress myIp;
	private int lowerLimit;
	private int upperLimit;
	private int id;
	private DatagramSocket server;
	private DatagramSocket client;

	public Network(String[] args) {

		try {
			myIp = this.getMyIP(args[1]);
			System.out.println("My ip: " + myIp);
		} catch (SocketException e) {

		} catch (UnknownHostException e) {
			System.out.println("Impossible to create network.");
		}

		try {
			server = new DatagramSocket(12345);
			client = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Port 12345 already in use.");
		}

		if (args[0].equals("server")) {
			this.create();
		} else {
			try {
				this.join(args[2]);
			} catch (IOException e) {
				System.out.println("buguei 2");
			}
		}



	}

	public void create() {
		predecessor = successor = myIp;
		id = generateId();
		lowerLimit = 0;
		upperLimit = (int) Math.pow(2, 32) - 1;
	}

	public void update() {

	}

	public void update(ByteArrayInputStream stream) {

	}

	public void join(String ip) throws IOException {
		InetAddress contact;
		try {
			contact = InetAddress.getByName(ip);
			id = generateId();
			System.out.println(id);
			client.send(PacketFactory.createSendJoin(contact, 12345, (byte) 0, id));

		} catch (UnknownHostException e) {
			System.out.println("Bugueii");
			e.printStackTrace();
		}

	}

	public void join(ByteArrayInputStream stream, InetAddress originIp) {
		byte[] buffer = new byte[4];
		stream.read(buffer, 0, 4); // byte[], offset, length
		int receivedId = Converter.bytesToInt(buffer);
		/* TODO
			Verificar limites inferior menor que zero
		*/
		if (id > lowerLimit && id <= upperLimit) {
			upperLimit = receivedId - 1;
			client.send(PacketFactory.createReplyJoin(originIp, 12345, (byte) 128, receivedId,
			lowerLimit, myIp, upperLimit, myIp));
		}
	}

	public void lookUp() {

	}

	public void lookUp(ByteArrayInputStream stream) {

	}

	public void leave() {

	}

	public void leave(ByteArrayInputStream stream) {

	}

	private InetAddress getMyIP(String netint) throws SocketException, UnknownHostException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netintf : Collections.list(nets)) {
			System.out.println(netintf);
			if (netintf.getName().contains(netint)) {
				Enumeration<InetAddress> addresses = netintf.getInetAddresses();
				Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
				for (InetAddress address : Collections.list(addresses)) {
					if (p.matcher(address.getHostAddress()).matches()) {
						return address;
					}
				}
			}
		}
		return InetAddress.getLocalHost();
	}

	public void listener() {
		System.out.println("Packing");
		DatagramPacket packet = new DatagramPacket(new byte[256], 256);
		try {
			server.receive(packet);
			ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
			byte code = (byte) stream.read();
			switch (code) {
			case 0:
				this.join(stream, packet.getAddress());
				break;
			case 1:
				this.leave(stream);
				break;
			case 2:
				this.lookUp(stream);
				break;
			case 3:
				this.update(stream);
				break;
			default:
				System.out.println("It seems something went wrong. Operation code invalid!");
				break;
			}
		} catch (IOException e) {
			System.out.println("No packet captured");
		}
	}

	private int generateId() {
		Random r = new Random(System.currentTimeMillis());
		return r.nextInt();
	}

}
