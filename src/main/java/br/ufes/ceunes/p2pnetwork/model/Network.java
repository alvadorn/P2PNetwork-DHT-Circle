package br.ufes.ceunes.p2pnetwork.model;

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
import java.util.Queue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JTextField;

import br.ufes.ceunes.p2pnetwork.actions.Converter;
import br.ufes.ceunes.p2pnetwork.actions.PacketFactory;
import br.ufes.ceunes.p2pnetwork.actions.PacketReceiver;
import br.ufes.ceunes.p2pnetwork.actions.Util;

import java.time.LocalDate;

public class Network {

	private Host predecessor;
	private Host successor;
	private Host host;
	private PacketReceiver receiver;
	private DatagramSocket server;
	private DatagramSocket client;
	private final int port = 12345;

	public Network(Queue<DatagramPacket> queue) {
		predecessor = new Host();
		successor = new Host();
		host = new Host(Util.generateId());
		receiver = new PacketReceiver(host, successor, predecessor, queue);

		try {
			server = new DatagramSocket(12345);
			client = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPort() {
		return port;
	}

	public Host getAntecessor() {
		return predecessor;
	}

	public Host getSuccessor() {
		return successor;
	}

	public Host getHost() {
		return host;
	}

	public void createNode(InetAddress ip) {
		host.setIp(ip);
		successor.copyHost(host);
		predecessor.copyHost(host);

	}

	public void send(DatagramPacket packet) {
		try {
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			new Thread() {
				public void run() {

					ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
					int code = stream.read();
					switch (code) {
					case 0:
						receiver.requireJoin(stream, packet.getAddress());
						break;
					case 1:
						receiver.requireLeave(stream, packet.getAddress());
						break;
					case 2:
						receiver.requireLookUp(stream);
						break;
					case 3:
						receiver.requireUpdate(stream, packet.getAddress());
						break;
					case 128:
						receiver.answerJoin(stream);
						break;
					case 129:
						receiver.answerLeave(stream);
						break;
					case 130:
						receiver.answerLookUp(stream);
						break;
					case 131:
						receiver.answerUpdate(stream);
						break;
					default:
						System.out.println("It seems something went wrong. Operation code invalid!");
						break;
					}
				}
			}.start();
		} catch (IOException e) {
			System.out.println("No packet captured");
		}
	}
	
	void setBox(JTextField succIp, JTextField succId) {
		receiver.setBox(succIp, succId);
	}

}
