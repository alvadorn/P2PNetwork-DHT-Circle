package br.ufes.ceunes.p2pnetwork.model;

import java.net.InetAddress;

import br.ufes.ceunes.p2pnetwork.actions.Converter;

public class Host {
	private InetAddress ip;
	private int id;
	final long limitUp;

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
		limitUp = (long) (Math.pow(2, 32) - 1) & 0xFFFFFFFFL;
	}

	public boolean isNext(long lookingId, long successorId) {
		long selfId = Converter.intToUnsignedInt(this.id);
		if (successorId == selfId) {
			return true;
		}
		if (selfId > successorId) {

			if ((lookingId >= selfId && selfId <= limitUp) || (lookingId >= 0 && lookingId < successorId)) {
				return true;
			}
		} else if (lookingId >= selfId && lookingId < successorId) {
			return true;
		}
		return false;
	}

	public boolean isBetween(long lookingId, long antecessorId) {
		long selfId = Converter.intToUnsignedInt(this.id);
		if (antecessorId == selfId) {
			return true;
		}
		if (selfId < antecessorId) {
			if ((lookingId > antecessorId && selfId <= limitUp) || (lookingId >= 0 && lookingId <= selfId)) {
				return true;
			}
		} else if (lookingId > antecessorId && lookingId <= selfId) {
			return true;
		}
		return false;
	}
}
