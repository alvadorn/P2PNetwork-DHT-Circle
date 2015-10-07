package br.ufes.ceunes.p2pnetwork.actions;

import java.util.Random;

public class Util {
	
	public static int generateId() {
		Random r = new Random(System.currentTimeMillis());
		return r.nextInt();
	}

}
