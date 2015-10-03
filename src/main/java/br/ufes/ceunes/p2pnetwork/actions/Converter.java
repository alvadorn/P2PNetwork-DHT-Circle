package br.ufes.ceunes.p2pnetwork.actions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Converter {
	public static byte[] intToBytes(int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		return bb.array();
	}

	public static int bytesToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	public static InetAddress stringToIP(String str) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByName(str);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}
	
	public static long intToUnsignedInt(int value) {
		long result = value & 0xFFFFFFFFL;
		return result;
	}
	
	public static long bytesToUnsignedInt(byte[] b) {
		return Converter.intToUnsignedInt(Converter.bytesToInt(b));
	}

}
