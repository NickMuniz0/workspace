package br.com.trd.threads;

public class Principal {
	public static void main(String[] args) {
		String nome ="da";
		
		Thread threadAssinaturas1 = new Thread(new TarefaBuscarTextual("assinaturas1.txt",nome));
		Thread threadAssinaturas2 = new Thread(new TarefaBuscarTextual("assinaturas2.txt",nome));
		Thread threadAutores = new Thread(new TarefaBuscarTextual("autores.txt",nome));
		
		
		threadAssinaturas1.start();
		threadAssinaturas2.start();
		threadAutores.start();

	}
}
