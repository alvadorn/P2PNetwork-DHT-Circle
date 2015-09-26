package br.ufes.ceunes.p2pnetwork;

import java.net.InetAddress;

class Host {
  private InetAddress ip;
  private int id;

  public InetAddress getIp() {
    return this.ip;
  }

  public int getId() {
    return this.id;
  }
}
