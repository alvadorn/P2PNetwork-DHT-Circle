package br.ufes.ceunes.p2pnetwork;

import java.awt.EventQueue;
import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.SwingUtilities;

import br.ufes.ceunes.p2pnetwork.model.Network;
import br.ufes.ceunes.p2pnetwork.view.MainScreen;

public class Main {
	public static void main(String[] args) {

		ConcurrentLinkedQueue<DatagramPacket> packets = new ConcurrentLinkedQueue<>();
		Network net = new Network(packets);
		MainScreen window = new MainScreen(packets, net);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.getFrame().setVisible(true);
					Timer timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									window.refresh();
								}
							});
						}
					}, 0, 2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Packet sender
		new Thread() {
			public void run() {
				while (true) {
					if (!packets.isEmpty()) {
						net.send(packets.poll());
					}
					//System.out.println(net.getSuccessor().getId());
				}

			}
		}.start();

		// Listener
		new Thread() {
			public void run() {
				while (true) {
					net.listener();
				}
			}
		}.start();

	}
}
