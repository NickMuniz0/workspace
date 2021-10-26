import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import modelo.Produto;
import dao.ProdutoDAO;

public class TestaInsercaoEListagemComProduto {

	
	
	public static void main(String[] args) throws SQLException {
		 Produto comoda =  new Produto("Cômoda", "Cômoda Vertical");		 
		 try(Connection connection= new ConnectionFactory().recuperarConexao()){
			 ProdutoDAO produtoDAO = new  ProdutoDAO(connection);
			 produtoDAO.salvar(comoda);
			 List<Produto> listaDeProdutos = produtoDAO.listar();
			 listaDeProdutos.stream().forEach(lp-> System.out.println(lp));
			 //Lista = persistenciaProduto.listar();
		 }
		 System.out.println(comoda);
	}
}
