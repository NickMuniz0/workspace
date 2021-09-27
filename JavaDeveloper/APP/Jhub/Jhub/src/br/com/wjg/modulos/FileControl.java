package br.com.wjg.modulos;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import br.com.wjg.config.Git;
import br.com.wjg.models.Autor;
import br.com.wjg.view.Mensagens;
@SuppressWarnings("unchecked")
public class FileControl {    
    private String path ="";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void write(Autor autor,JSONObject nivel, LocalDate key,Git gitConf) 
                                                            throws IOException{
            setPath(String.format("%s\\%s%s",
            gitConf.getPathRespositorio().getText(),
            gitConf.getArquivo().getText(),
            gitConf.getExt()) );


            JSONObject objeto = ExistKey(getPath());
            boolean existeDataCadastrada = objeto!= null;
            if(!existeDataCadastrada ){
                NaoExisteArquivo(nivel);
                return;              
            }
            boolean naoExisteChave = objeto.get(key.toString()) == null;
            if(naoExisteChave){
                NaoExisteChave(nivel,key,objeto);
                return;
            }
            ExisteChave(autor,nivel,key,objeto);  

    }

    public JSONObject ExistKey(String path){
        try{        
            String content = new String(Files.readAllBytes(Paths.get(path)));  
            return (JSONObject)  JSONValue.parse(content); 
        } catch (Exception ex) {
            return null;
        }
    }
    
    public void ExisteChave(Autor autor,
                            JSONObject nivel,
                            LocalDate key,
                            JSONObject objetoAntigo) throws IOException {
        //Antigo
        JSONObject object2 = (JSONObject) objetoAntigo.get(key.toString());
        JSONArray object3 = (JSONArray) object2.get(autor.getNome().getText());
        //Novo
        Object objA = JSONValue.parse(nivel.toJSONString()); 
        JSONObject jsonObject = (JSONObject) objA;
        JSONObject novo =  (JSONObject) jsonObject.get(key.toString());
        JSONArray object = (JSONArray) novo.get(autor.getNome().getText());

        boolean naoExisteAutor = object3==null;
        if(naoExisteAutor){
            NaoExisteAutor(object,objetoAntigo,autor,key);
            return;
        }
        object3.add(object.get(0));
        toSave(objetoAntigo);

    }

    public void NaoExisteAutor(JSONArray objetoNovo,JSONObject objetoAntigo,Autor autor,LocalDate key) throws IOException{
        JSONObject ob = (JSONObject) objetoAntigo.get(key.toString());
        ob.put(autor.getNome().getText(), objetoNovo);
        toSave(objetoAntigo);

    }

    public void NaoExisteChave(JSONObject nivel,
                                LocalDate key,
                                JSONObject objetoAntigo) throws IOException{
        Object objA = JSONValue.parse(nivel.toJSONString());  
        JSONObject jsonObjectA = (JSONObject) objA;
        objetoAntigo.put(key, jsonObjectA.get(key.toString()));
            toSave(objetoAntigo);
    }

    public void NaoExisteArquivo(JSONObject nivel) throws IOException{
            toSave(nivel);
    }

    public void toSave(JSONObject objeto) throws IOException{
        FileWriter writeFile =  new FileWriter(getPath());
        writeFile.append(objeto.toJSONString());
        writeFile.close();
		new Mensagens("Informações armazenada!");

    }
}
