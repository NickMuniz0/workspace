package br.com.jrd.lista;

import java.util.List;

public class TaregaAdicionarElemento implements Runnable {

	private List<String> lista;
	private int numeroDoThread;

	public TaregaAdicionarElemento(List<String> lista, int numeroDoThread) {
	
		this.lista = lista;
		this.numeroDoThread = numeroDoThread;
	}

	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			lista.add("Thread " +numeroDoThread+"- "+i);
		}
	}

}
