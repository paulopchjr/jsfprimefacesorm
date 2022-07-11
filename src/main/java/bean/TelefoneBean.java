package bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import dao.DaoTelefone;
import dao.DaoUsuario;
import model.TelefoneUser;
import model.UsuarioPessoa;

@ViewScoped
@ManagedBean(name = "telefoneBean")
public class TelefoneBean {

	private UsuarioPessoa pessoa = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoUsuario = new DaoUsuario<UsuarioPessoa>();
	private DaoTelefone<TelefoneUser> daoTelefone = new DaoTelefone<TelefoneUser>();
	private TelefoneUser telefoneUser = new TelefoneUser();
	private List<TelefoneUser> telefones = new ArrayList<TelefoneUser>();

	@PostConstruct
	public void init() {

		String codUser = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigouser");
		pessoa = daoUsuario.pesquisarporid(Long.parseLong(codUser), UsuarioPessoa.class);
		telefones = pessoa.getTelefoneUsers();

	}

	public String salvar() {
		telefoneUser.setUsuarioPessoa(pessoa);
		daoTelefone.salvar(telefoneUser);
		telefones.add(telefoneUser);
		telefoneUser = new TelefoneUser();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		return "";
	}

	public String removerTelelefone() throws Exception {
		daoTelefone.deletarporId(telefoneUser);
		telefones.remove(telefoneUser);
		telefoneUser = new TelefoneUser();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Excluido com Sucesso!"));
		return "";
	}

	public UsuarioPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(UsuarioPessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setDaoTelefone(DaoTelefone<TelefoneUser> daoTelefone) {
		this.daoTelefone = daoTelefone;
	}

	public DaoTelefone<TelefoneUser> getDaoTelefone() {
		return daoTelefone;
	}

	public TelefoneUser getTelefoneUser() {
		return telefoneUser;
	}

	public void setTelefoneUser(TelefoneUser telefoneUser) {
		this.telefoneUser = telefoneUser;
	}

	public List<TelefoneUser> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneUser> telefones) {
		this.telefones = telefones;
	}

}
