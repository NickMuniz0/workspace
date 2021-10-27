package br.com.hibernate.loja.testes;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.hibernate.dao.CategoriaDao;
import br.com.hibernate.dao.ProdutoDao;
import br.com.hibernate.loja.modelo.Categoria;
import br.com.hibernate.loja.modelo.Produto;
import br.com.hibernate.util.JPAUtil;

public class CadastroDeProduto {

	public static void main(String[] args) {
		//cadatrarProduto();
		
		Long id =2l;
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		
		//Produto p = produtoDao.buscarPorId(id);
		//System.out.println(p.getPreco());
		
		//List<Produto> todosOsProdutos = produtoDao.buscarTodos();
		//todosOsProdutos.forEach(p2 -> System.out.println(p2.getId()+" " + p2.getNome()+" "+p2.getDescricao() +" "+ p2.getPreco()) );
		
		
		//List<Produto> nomeProduto = produtoDao.buscarPorNome("Xiaomi Redmi");
		//nomeProduto.forEach(p2 -> System.out.println(p2.getId()+" " + p2.getNome()+" "+p2.getDescricao() +" "+ p2.getPreco()) );
		
		
		//List<Produto> nomeProduto = produtoDao.buscarPorNomeDaCategoria("Celulares");
		//nomeProduto.forEach(p2 -> System.out.println(p2.getId()+" " + p2.getNome()+" "+p2.getDescricao() +" "+ p2.getPreco()) );
		
		//BigDecimal precoDoProduto = produtoDao.buscarPrecoDoProdutoComNome("Aple");
		//System.out.println(precoDoProduto);
		
	}

	private static void cadatrarProduto() {
		Categoria celulares = new Categoria("Celulares");				
		Produto celular = new Produto("Xiaomi Redmi",
									"Muito Legal",
									new BigDecimal("800"),celulares);
		
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);		
		
		em.getTransaction().begin();
		categoriaDao.cadastrar(celulares);
		produtoDao.cadastrar(celular);
		em.getTransaction().commit();
		em.close();
	}
}
