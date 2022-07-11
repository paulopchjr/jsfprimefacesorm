package br.com.cursojpahibernate;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class testeHibernate {

	@Test
	public void testeHibernateUtil() {
		HibernateUtil.getEntityManager();

	}

	@Test
	public void testSavePessoa() throws Exception {

		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setNome("Roberto");
		pessoa.setSobreNome("teste");
		pessoa.setLogin("teste");
		pessoa.setSenha("teste");

		daoGeneric.salvar(pessoa);

	}

	@Test
	public void testeBuscar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setId(8L);
		pessoa = daoGeneric.pesquisar(pessoa);

		System.out.println(pessoa);

	}

	@Test
	public void testeBuscarporId() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		UsuarioPessoa pessoa = daoGeneric.pesquisarporid(8L, UsuarioPessoa.class);
		System.out.println(pessoa);

	}

	@Test
	public void testeUpdate() throws Exception {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisarporid(17L, UsuarioPessoa.class);

		pessoa.setNome("Fernanda");
		pessoa.setSobreNome("Henrique");
		pessoa.setLogin("teste");
		pessoa.setSenha("teste");

		pessoa = daoGeneric.updateMerge(pessoa);

		System.out.println(pessoa);

	}

	@Test
	public void testeDelete() {
		try {
			DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
			UsuarioPessoa pessoa = daoGeneric.pesquisarporid(17L, UsuarioPessoa.class);

			daoGeneric.deletarporId(pessoa);

		} catch (Exception e) {
				e.printStackTrace();
		}
	}

	@Test
	public void testeConsultar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println("------------");
		}

	}

	/* HQL */
	@Test
	public void testeQueryList() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa where nome = 'Carla'")
				.getResultList();
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}

	/* HQL por ordenação - ordenações e máximo resultados definidos - aula 25.16 */
	@Test
	public void testeQueryListMaxResult() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		/* SELECT * FROM usuariopessoa order by nome limit 5; */
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery(" from UsuarioPessoa  order by id")
				.setMaxResults(5).getResultList();
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}

	/* HQL PARAMENTROS E QUERYES CONDICIONAIS - AULA 25.17 */
	@Test
	public void testeQueryListParamentro() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery("from UsuarioPessoa where nome = :nome or sobrenome = :sobrenome")
				.setParameter("nome", "aa").setParameter("sobrenome", "Henrique").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	/* Efetuando operações matematicas - aula 25.18 */
	@Test
	public void testeQueryCountRegistro() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		/*
		 * contou todos os registros do banco, pegou o resultado e converteu de um
		 * objeto para inteiro atraves do hascode
		 */
		int totalRegistro = (int) daoGeneric.getEntityManager().createQuery("select count(u.nome) from UsuarioPessoa u")
				.getSingleResult().hashCode();
		System.out.println("Total de registro no banco é = " + totalRegistro);

	}

	/* Aula 25.19 */
	@Test
	public void testeNameQuery1() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.todos")
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}

	@Test
	public void testeNameQuery2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.buscaPorNome")
				.setParameter("nome", "Laura").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}

	/* Aula 25.20 */
	@Test
	public void testeGravaTelefone() throws Exception {
		DaoGeneric daoGeneric = new DaoGeneric();

		// pegando o pesquisando o usuário para ser acrescentado o telefone
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisarporid(8L, UsuarioPessoa.class);

		TelefoneUser telefone = new TelefoneUser();
		telefone.setTipo("celular");
		telefone.setNumero("0123456789");
		telefone.setUsuarioPessoa(pessoa);

		daoGeneric.salvar(telefone);
	}

	@Test
	public void testeConsultaTelefones() throws Exception {
		DaoGeneric daoGeneric = new DaoGeneric();

		// pegando o pesquisando o usuário para ser acrescentado o telefone
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisarporid(8L, UsuarioPessoa.class);
		for (TelefoneUser fones : pessoa.getTelefoneUsers()) {
			System.out.println(fones.getNumero());
			System.out.println(fones.getTipo());
			System.out.println(fones.getUsuarioPessoa().getNome());

			System.out.println("---------------------------------------------");
		}

	}

}
