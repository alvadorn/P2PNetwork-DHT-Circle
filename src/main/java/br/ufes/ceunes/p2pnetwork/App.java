package br.ufes.ceunes.p2pnetwork;

public class App {
    public static void main( String[] args ) {
    	System.out.println(args.length + args[0]);
        Network net = new Network(args[0], args[1]);
        System.out.println("Teste");
    }
}
