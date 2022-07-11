package br.com.cursojpahibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

	// fabrica de conexao do hibernate
	public static EntityManagerFactory factory = null;

	// chamando o método init
	static {
		init();
	}

	private static void init() {
		try {
			if (factory == null) {

				// Lê todos os arquivos de configuração do persistence xml
				factory = Persistence.createEntityManagerFactory("cursojpahibernate");

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("linha " + e.getLocalizedMessage());
			System.out.println("causa " + e.getCause());
			System.out.println("msg " + e.getMessage());
		}
	}

	public static EntityManagerFactory getFactory() {
		return factory;
	}

	// retorna o gerenciados de entidade para fazer o gerencia o banco de dados
	public static EntityManager getEntityManager() {
		return factory.createEntityManager(); /* Provê a parte de persistencia */
	}

	public static Object getPrimaryKey(Object entity) { // Retorna a primay key
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
	}

}
