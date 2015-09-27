package br.ufes.ceunes.p2pnetwork;

import java.net.InetAddress;

public class Host {
	private InetAddress ip;
	private int id;
	private int lowerLimit;
	private int upperLimit;

	public InetAddress getIp() {
		return this.ip;
	}

	public int getId() {
		return this.id;
	}
	
	public int getLowLimit() {
		return lowerLimit;
	}
	
	public int getHighLimit() {
		return upperLimit;
	}

	public void setInfo(InetAddress ip, int id) {
		this.ip = ip;
		this.id = id;
	}
	
	public void setLimit(int low, int high) {
		this.lowerLimit = low;
		this.upperLimit = high;
	}
	
	public void copyHost(Host h1) {
		this.setInfo(h1.getIp(), h1.getId());
		this.setLimit(h1.getLowLimit(), h1.getHighLimit());
	}

}
