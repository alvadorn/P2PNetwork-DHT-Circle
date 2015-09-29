package br.ufes.ceunes.p2pnetwork;

import br.ufes.ceunes.p2pnetwork.model.Network;

public class App {
    public static void main( String[] args ) {
      /* Arg[0] -> option like server client, server if first
        Arg[1] -> interface de rede usada
        Arg[2] -> ip do contato na rede caso cliente
      */
        Network net = new Network(args);
        while(true) { //gambiarra
          net.listener();
        }
    }
}
