package br.ufes.ceunes.p2pnetwork.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import br.ufes.ceunes.p2pnetwork.actions.PacketFactory;
import br.ufes.ceunes.p2pnetwork.model.Host;
import br.ufes.ceunes.p2pnetwork.model.Network;

import java.awt.SystemColor;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Queue;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainScreen {

	private JFrame frame;
	private JTextField self_ip;
	private JTextField self_id;
	private JTextField ant_ip;
	private JTextField ant_id;
	private JTextField suc_ip;
	private JTextField suc_id;
	private JComboBox intfBox;
	private JButton btnCreate;
	private JButton btnLeave;
	private JButton btnLookup;
	private InetAddress contact;
	private Queue<DatagramPacket> queue;
	private Network net;

	/**
	 * Create the application.
	 */
	public MainScreen(Queue<DatagramPacket> queue, Network net) {
		this.queue = queue;
		this.net = net;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 538, 268);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblSelectInterface = new JLabel("Select Interface:");
		lblSelectInterface.setBounds(29, 12, 126, 15);
		frame.getContentPane().add(lblSelectInterface);

		intfBox = new JComboBox(getInterfaces());

		intfBox.setBounds(163, 7, 126, 24);
		frame.getContentPane().add(intfBox);

		JSeparator separator = new JSeparator();
		separator.setBounds(29, 41, 483, 15);
		frame.getContentPane().add(separator);

		JLabel lblMyIp = new JLabel("My IP:");
		lblMyIp.setBounds(29, 53, 70, 15);
		frame.getContentPane().add(lblMyIp);

		self_ip = new JTextField();
		self_ip.setEditable(false);
		self_ip.setBounds(84, 51, 134, 19);
		frame.getContentPane().add(self_ip);
		self_ip.setColumns(10);

		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(29, 80, 70, 15);
		frame.getContentPane().add(lblId);

		self_id = new JTextField();
		self_id.setBounds(84, 78, 134, 19);
		frame.getContentPane().add(self_id);
		self_id.setColumns(10);

		btnCreate = new JButton("Create");

		btnCreate.setBounds(249, 48, 117, 25);
		frame.getContentPane().add(btnCreate);

		btnLookup = new JButton("Lookup");
		btnLookup.setBounds(249, 75, 117, 25);
		frame.getContentPane().add(btnLookup);

		btnLeave = new JButton("Leave");

		btnLeave.setBounds(381, 48, 117, 25);
		frame.getContentPane().add(btnLeave);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(29, 112, 483, 2);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(274, 130, 23, 86);
		frame.getContentPane().add(separator_2);

		JLabel lblAntecessor = new JLabel("Antecessor");
		lblAntecessor.setBounds(120, 127, 111, 15);
		frame.getContentPane().add(lblAntecessor);

		JLabel lblIp = new JLabel("IP:");
		lblIp.setBounds(29, 156, 70, 15);
		frame.getContentPane().add(lblIp);

		ant_ip = new JTextField();
		ant_ip.setEditable(false);
		ant_ip.setBounds(68, 154, 134, 19);
		frame.getContentPane().add(ant_ip);
		ant_ip.setColumns(10);

		JLabel lblId_1 = new JLabel("ID:");
		lblId_1.setBounds(29, 183, 70, 15);
		frame.getContentPane().add(lblId_1);

		ant_id = new JTextField();
		ant_id.setEditable(false);
		ant_id.setBounds(68, 183, 134, 19);
		frame.getContentPane().add(ant_id);
		ant_id.setColumns(10);

		JLabel lblSuccessor = new JLabel("Successor");
		lblSuccessor.setBounds(402, 126, 96, 15);
		frame.getContentPane().add(lblSuccessor);

		JLabel lblIp_1 = new JLabel("IP:");
		lblIp_1.setBounds(315, 156, 70, 15);
		frame.getContentPane().add(lblIp_1);

		suc_ip = new JTextField();
		suc_ip.setEditable(false);
		suc_ip.setBounds(384, 154, 114, 19);
		frame.getContentPane().add(suc_ip);
		suc_ip.setColumns(10);

		JLabel lblId_2 = new JLabel("ID:");
		lblId_2.setBounds(315, 183, 70, 15);
		frame.getContentPane().add(lblId_2);

		suc_id = new JTextField();
		suc_id.setEditable(false);
		suc_id.setBounds(384, 181, 114, 19);
		frame.getContentPane().add(suc_id);
		suc_id.setColumns(10);

		setIp();

		listeners();

		self_id.setText(Long.toString(net.getHost().getId()));

		JButton btnNewButton = new JButton("Force ID");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strId = JOptionPane.showInputDialog(null, "Set ID!", "Enter you ID",
						JOptionPane.QUESTION_MESSAGE);
				int id = Integer.parseInt(strId);
				net.getHost().setId(id);
				if (net.getHost().getId() == net.getAntecessor().getId()) {
					net.getAntecessor().setId(id);
					net.getSuccessor().setId(id);
				}
			}
		});
		btnNewButton.setBounds(381, 75, 117, 25);
		frame.getContentPane().add(btnNewButton);

	}

	private Object[] getInterfaces() {
		ArrayList<String> list = new ArrayList<String>();
		Enumeration<NetworkInterface> nets;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netintf : Collections.list(nets)) {
				list.add(netintf.getDisplayName());
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.toArray();
	}

	private void listeners() {
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				net.createNode(getIp(intfBox.getSelectedIndex()));
				btnCreate.setEnabled(false);
				btnLookup.setEnabled(false);
				setNeighborhood();
			}
		});

		btnLookup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = JOptionPane.showInputDialog(null, "What is contact IP?", "Enter you contact IP",
						JOptionPane.QUESTION_MESSAGE);

				try {
					contact = InetAddress.getByName(ip);
					btnCreate.setEnabled(false);
					queue.add(PacketFactory.createRequireLookUp(contact, 12345, (int) net.getHost().getId(),
							net.getHost().getIp(), (int) net.getHost().getId()));
					// btnLookup.setEnabled(false);
					// setNeighborhood();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		btnLeave.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				btnCreate.setEnabled(true);
				btnLookup.setEnabled(true);
				Host antecessor = net.getAntecessor();
				Host successor = net.getSuccessor();
				// Sending message to antecessor
				queue.add(PacketFactory.createSendLeave(antecessor.getIp(), net.getPort(), (int) net.getHost().getId(),
						(int) successor.getId(), successor.getIp(), (int) antecessor.getId(), antecessor.getIp()));

				// Sending message to successor
				queue.add(PacketFactory.createSendLeave(successor.getIp(), net.getPort(), (int) net.getHost().getId(),
						(int) successor.getId(), successor.getIp(), (int) antecessor.getId(), antecessor.getIp()));
			}

		});

		intfBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setIp();
			}
		});
	}

	private void setIp() {
		int selected = intfBox.getSelectedIndex();
		InetAddress ip = getIp(selected);
		if (ip != null) {
			net.getHost().setIp(ip);
			self_ip.setText(ip.getHostAddress());
		}
	}

	private InetAddress getIp(int index) {
		Enumeration<NetworkInterface> nets;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
			NetworkInterface netintf = Collections.list(nets).get(index);
			Enumeration<InetAddress> addresses = netintf.getInetAddresses();
			Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
			for (InetAddress address : Collections.list(addresses)) {
				if (p.matcher(address.getHostAddress()).matches()) {
					return address;
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void setNeighborhood() {
		Host host = net.getAntecessor();
		if (host.getIp() != null) {
			ant_ip.setText(host.getIp().getHostAddress());
			ant_id.setText(Long.toString(host.getId()));

			host = net.getSuccessor();
			suc_ip.setText(host.getIp().getHostAddress());
			suc_id.setText(Long.toString(host.getId()));

		} else {
			ant_id.setText("");
			suc_id.setText("");
			ant_ip.setText("");
			suc_ip.setText("");
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public void refresh() {
		setNeighborhood();
		self_id.setText(Long.toString(net.getHost().getId()));
	}
}
