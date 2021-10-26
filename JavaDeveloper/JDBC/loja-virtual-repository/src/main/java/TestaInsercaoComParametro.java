import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestaInsercaoComParametro {
	public static void main(String[] args) throws SQLException {
		
		ConnectionFactory factory = new ConnectionFactory();
		try(Connection connection = factory.recuperarConexao()){ //autoclose
			
			connection.setAutoCommit(false);  //So executa  se nao ocorrer erro . 
			String sql ="INSERT INTO PRODUTO (nome,descricao) VALUES (?,?)";
			try (PreparedStatement stm = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)
					){
				adicionarVariavel("SmartTv", "45 Polegadas", stm);
				adicionarVariavel("Radio", "Radio de Bateria", stm);
				
				connection.commit();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ROLLBACK EXECUTADO");
				connection.rollback();
			}
		
		}
		
		
		
		

	}

	private static void adicionarVariavel(String nome, String descricao, PreparedStatement stm) throws SQLException {
		stm.setString(1,nome);
		stm.setString(2,descricao);
		//Testando uma exception		
		//		if(nome.equals("Radio")) {
		//			throw new RuntimeException("Nao foi possivel adicionar o produto"); 
		//		}
		
		stm.execute();
		//recuperando o id 
		try(ResultSet rst = stm.getGeneratedKeys()){
			while(rst.next()) {
				Integer id = rst.getInt(1);
				System.out.println(id);
			}
		}	
		
		
		
	}
}
