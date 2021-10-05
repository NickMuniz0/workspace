package br.com.trd.threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TarefaBuscarTextual implements Runnable {

	private String nomeArquivo;
	private String nome;

	public TarefaBuscarTextual(String nomeArquivo, String nome) {
		this.nomeArquivo = nomeArquivo;
		this.nome = nome;
	}

	@Override
	public void run() {
		try {
			Scanner	scanner = new Scanner(new File(nomeArquivo));
			int numeroLinha = 1;
			
			while(scanner.hasNextLine()) {
				String linha = scanner.nextLine();
				if(linha.toLowerCase().contains(nome.toLowerCase() )) {
					System.out.println(String.format("%s - %s - %s ", nomeArquivo,numeroLinha,  linha));
				}
				numeroLinha++;
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

}
