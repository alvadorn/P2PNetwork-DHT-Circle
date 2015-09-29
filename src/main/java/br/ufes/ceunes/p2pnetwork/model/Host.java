package br.ufes.ceunes.p2pnetwork.model;

import java.net.InetAddress;

import br.ufes.ceunes.p2pnetwork.actions.Converter;

public class Host {
	private InetAddress ip;
	private int id;

	public InetAddress getIp() {
		return this.ip;
	}

	public long getId() {
		return Converter.intToUnsignedInt(this.id);
	}
	

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInfo(InetAddress ip, int id) {
		this.ip = ip;
		this.id = id;
	}

	public void copyHost(Host h1) {
		this.setInfo(h1.getIp(), (int) h1.getId());
	}

	public Host(int id) {
		this();
		this.id = id;
	}

	public Host() {

	}
}
