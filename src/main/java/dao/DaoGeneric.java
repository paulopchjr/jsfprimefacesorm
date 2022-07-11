package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cursojpahibernate.HibernateUtil;

/*Criando um DAO GENERICO - Aula 25.19*/
public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();

	/* metodo responsável por inserir dados no banco */
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}

	/* metodo responsável por buscar um registro por id - Aula 25.11 */
	public E pesquisar(E entidade) {

		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);

		return e;
	}

	/* 2 método responsável por buscar um registro por id - Aula 25.11 */
	public E pesquisarporid(Long id, Class<E> entidade) {

		E e = (E) entityManager.find(entidade, id);
		return e;
	}

	/* método responsável por atualiza registro no banco - Aula 25.12 */
	public E updateMerge(E entidade) throws Exception {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeGravada = entityManager.merge(entidade);
		transaction.commit();

		return entidadeGravada;
	}

	/*  método responsável por excluir um registro no banco  - Aula 25.13*/
	public void deletarporId(E entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		/*
		 * é a mesmma função de delete from usuariopessoa where id = (O que for passado
		 * por paramentro)
		 */
		entityManager
				.createNativeQuery(
						"delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id = " + id)
				.executeUpdate();

		transaction.commit();

	}

	/* Método responsável por listar todos os usuarios da tabela - Aula 25.14*/
	public List<E> listar(Class<E> entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

						/* é a mesma função do select * from usuariopessoa */
		List<E> lista = entityManager.createQuery("from " + entidade.getName()).getResultList();
		transaction.commit();

		return lista;
	}

	/* Criando queres especificas aula 25.15 */

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
