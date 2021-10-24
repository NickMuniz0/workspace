import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestaListagem {
	public static void main(String[] args) throws SQLException {
		
		ConnectionFactory criaConexao = new ConnectionFactory();
		Connection connection = criaConexao.recuperarConexao();
		
		Statement stm = connection.createStatement();
		stm.execute("Select ID,NOME,DESCRICAO FROM PRODUTO");
		ResultSet rst = stm.getResultSet();
		while(rst.next()) {
			Integer	id = rst.getInt("ID");
			String nome = rst.getString("NOME");
			String descricao = rst.getString("DESCRICAO");
			System.out.println(id+"- " + nome+ "- " + descricao);
		}
		connection.close();
	}
}
