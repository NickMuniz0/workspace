package br.com.wjg.controller;


import org.json.simple.JSONObject;

import br.com.wjg.view.LoadConfig;
import br.com.wjg.view.TelaDeCadastro;

public class Main {


    @SuppressWarnings("unused")
	public void start(){

    	 if(new LoadConfig().ExistFile() == null) {  
    		 new TelaDeCadastro();
    	 } else {
    		 JSONObject resultado = new LoadConfig().ExistFile();
    		 String arquivogit = (String) resultado.get("arquivogit");
    		 String pathgit = (String) resultado.get("pathgit");
    		 Integer indexfinal = arquivogit.indexOf(".json");
    		 String arquivoNameGit = arquivogit.substring(0, indexfinal);

    		 TelaDeCadastro teladecadastro =new TelaDeCadastro();
    		 teladecadastro.getJtarquivoGit().setText(arquivoNameGit);
    		 teladecadastro.getJtrepositorioGit().setText(pathgit);


    	 
    	 }
    	

    }


  

   
    
}

