package br.com.wjg.view;

import org.json.simple.JSONObject;
import br.com.wjg.modulos.FileControl;

public class LoadConfig {

	public LoadConfig() {
		ExistFile();
	}
	
	public JSONObject ExistFile() {
		FileControl file =new FileControl();
		String fileconf ="confGit.json";
		String path =  String.format("%s\\%s", System.getProperty("user.dir"),fileconf);	
		return file.ExistKey(path);
	}
	
	
}
