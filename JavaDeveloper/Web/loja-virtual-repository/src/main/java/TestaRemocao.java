import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestaRemocao {
	
	public static void main(String[] args) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection connection = factory.recuperarConexao();
		Statement stm = connection.createStatement();
		stm.execute("DELETE FROM PRODUTO WHERE  ID > 1");
		
		Integer linhasModificadas = stm.getUpdateCount();
		System.out.println(linhasModificadas );
	}

}
