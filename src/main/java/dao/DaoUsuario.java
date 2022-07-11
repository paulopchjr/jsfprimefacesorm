package dao;

import java.util.List;

import javax.persistence.Query;

import model.UsuarioPessoa;

public class DaoUsuario <E> extends DaoGeneric<UsuarioPessoa> {

	public void removerUsuario(UsuarioPessoa pessoa) throws Exception {
		getEntityManager().getTransaction().begin();
		
		/*removendo a pessoa*/
		getEntityManager().remove(pessoa);
		
		getEntityManager().getTransaction().commit();

	}

	public List<UsuarioPessoa> filtrar(String filtonome) {
		Query query  = super.getEntityManager().createQuery("from UsuarioPessoa where nome like '%"+filtonome+"%'");	
		return query.getResultList();
	}

}
