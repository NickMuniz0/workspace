package br.com.jrd.lista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Principal {

	public static void main(String[] args) throws InterruptedException {

//		List<String> lista = new ArrayList<>();
//		List<String> lista = Collections.synchronizedList(new ArrayList<String>());
		List<String> lista = new Vector<String>();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new TaregaAdicionarElemento(lista,i)).start();
			
		}
		Thread.sleep(2000);
		
		
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(i+ " "+lista.get(i));
			
		}
	}

}
