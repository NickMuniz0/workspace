import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TestaRemocao {
	
	public static void main(String[] args) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection connection = factory.recuperarConexao();
		//Statement stm = connection.createStatement();
		//stm.execute("DELETE FROM PRODUTO WHERE  ID > 1");
		
		PreparedStatement stm = connection.prepareStatement("DELETE FROM PRODUTO WHERE  ID > ?");
		stm.setInt(1, 2);
		stm.execute();
		
		Integer linhasModificadas = stm.getUpdateCount();
		System.out.println(linhasModificadas );
	}

}
