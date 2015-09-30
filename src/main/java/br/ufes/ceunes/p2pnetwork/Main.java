package br.ufes.ceunes.p2pnetwork;

import java.awt.EventQueue;
import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;

import br.ufes.ceunes.p2pnetwork.model.Network;
import br.ufes.ceunes.p2pnetwork.view.MainScreen;

public class Main {
	public static void main(String[] args) {
		
		ConcurrentLinkedQueue<DatagramPacket> packets = new ConcurrentLinkedQueue<>();
		Network net = new Network(packets);
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen(packets, net);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// Packet sender
		new Thread() {
			public void run() {
				while(true) {
					if (!packets.isEmpty()) {
						net.send(packets.poll());
					}
				}
				
			}
		}.start();
		
		
		// Listener
		new Thread() {
			public void run() {
				while(true) {
					net.listener();
				}
			}
		}.start();
		
		
	}
}
