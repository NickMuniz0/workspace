package br.com.jrd.banco;

public class TarefaAcessaBanco implements Runnable {

	private PoolDeConexao pool;
	private GerenciadorDeTransacao tx;

	public TarefaAcessaBanco(PoolDeConexao pool, GerenciadorDeTransacao tx) {
		this.pool = pool;
		this.tx = tx;
	}

	@Override
	public void run() {

		synchronized(pool) {
			System.out.println("peguei a chave do pool");
			pool.getConnection();
			
			synchronized(tx) {
				System.out.println("comando a gerenciar a tx");
				tx.begin();
			}
			
		}
	}

}
