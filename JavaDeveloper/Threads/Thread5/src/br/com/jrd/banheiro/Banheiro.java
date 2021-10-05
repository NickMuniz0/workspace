package br.com.jrd.banheiro;

public class Banheiro {
	
	private boolean ehSujo = true;

	public void fazNumero1() {
		
		String nome = Thread.currentThread().getName();
		System.out.println(nome+" Batendo na porta");
		synchronized(this) {
			System.out.println(nome +" Entrando no Banheiro");
			
			
			while(ehSujo) {
						esperaLaFora(nome);
			}
			
			
			System.out.println(nome +" Fazendo Coisa Rapida");
			dormeUmPouco(5000);

			
			
			this.ehSujo= true;
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
			
			while(ehSujo) {
				esperaLaFora(nome);
			}
			
						
			System.out.println(nome +" Fazendo Coisa Demorada");
			dormeUmPouco(10000);
			this.ehSujo= true;
 
			System.out.println(nome +" Dando Descarga");
			System.out.println(nome +" Lavando Mão");
			System.out.println(nome +" Saindo do Banheiro");
			
			
		}
		
		
	}



	private void dormeUmPouco(long milissegundos) {
		try {
			Thread.sleep(milissegundos);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void limpa() {
		String nome = Thread.currentThread().getName();
		System.out.println(nome+" Batendo na porta");

		synchronized(this) {
			System.out.println(nome +" Entrando no Banheiro");
			
			
			if(!ehSujo) {
				System.out.println(nome +" ,nao esta sujo, vou sair");
				return;

			}
			
			System.out.println(nome +" Limpando");
			this.ehSujo= false;

			try {
				Thread.sleep(13000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.notifyAll();
			System.out.println(nome +" Saindo do Banheiro");
			
		}
		

	}
	
	

	private void esperaLaFora(String nome) {
		System.out.println(nome + ", eca, banheiro tá sujo");
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
