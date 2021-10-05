package br.com.jrd.banheiro;

public class Banheiro {

	public void fazNumero1() {
		
		String nome = Thread.currentThread().getName();
		System.out.println(nome+" Batendo na porta");
		synchronized(this) {
			System.out.println(nome +" Entrando no Banheiro");
			System.out.println(nome +" Fazendo Coisa Rapida");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(nome +" Dando Descarga");
			System.out.println(nome +" Lavando Mão");
			System.out.println(nome +" Saindo do Banheiro");
		}
		
		
	}
	
	public void fazNumero2() {
		String nome = Thread.currentThread().getName();
		System.out.println(nome+" Batendo na porta");

		synchronized(this) {

			
			System.out.println(nome +" Entrando no Banheiro");
			System.out.println(nome +" Fazendo Coisa Demorada");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(nome +" Dando Descarga");
			System.out.println(nome +" Lavando Mão");
			System.out.println(nome +" Saindo do Banheiro");
			
			
		}
		
		
	}
}
