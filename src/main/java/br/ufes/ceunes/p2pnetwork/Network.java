package br.ufes.ceunes.p2pnetwork;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class Network {

	private InetAddress predecessorIp;
	private InetAddress successorIp;
	private InetAddress myIp;
	private int lowerLimit;
	private int upperLimit;

	public Network(String mode, String netInterface) {

		try {
			myIp = this.getMyIP(netInterface);
			System.out.println("My ip: " + myIp);
		} catch (SocketException e) {

		} catch (UnknownHostException e) {
			System.out.println("Impossible to create network.");
		}

		if (mode == "server") {
			this.create();
		} else {
			this.join();
		}
	}

	public void create() {
		predecessorIp = successorIp = myIp;
		lowerLimit = 0;
		upperLimit = (int) Math.pow(2, 32) - 1;
	}

	public void update() {

	}

	public void join() {

	}

	public void lookUp() {

	}

	public void leave() {

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

}
