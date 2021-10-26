import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import dao.CategoriaDAO;
import modelo.Categoria;
import modelo.Produto;

public class TestaListagemDeCategorias {

	public static void main(String[] args) throws Exception {

		try(Connection connection = new ConnectionFactory().recuperarConexao()){
			
			CategoriaDAO categoriaDAO = new CategoriaDAO(connection);
			//Query N+1
//			List<Categoria> listaDeCategorias = categoriaDAO.listar();
			
			List<Categoria> listaDeCategorias = categoriaDAO.listarComProduto();

			
			listaDeCategorias.stream().forEach(ct->			{
				
				
//				Query N+1
//				try {
//					for (Produto produto : new ProdutoDAO(connection).buscar(ct) ) {
//						System.out.println(ct.getNome() + "- "+ produto.getNome());
//
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				for (Produto produto : ct.getProdutos() ) {
					System.out.println(ct.getNome() + "- "+ produto.getNome());

				}
				
			}
					);
			
		}
		
	}

}
