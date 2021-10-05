package br.com.jrd.banheiro;

public class TarefaLimpeza implements Runnable {

	private Banheiro banheiro;

	public TarefaLimpeza(Banheiro banheiro) {
		this.banheiro = banheiro;
	}

	@Override
	public void run() { 
		while(true) {
			banheiro.limpa();
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
