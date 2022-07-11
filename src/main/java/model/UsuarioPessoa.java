package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author paulo
 *
 */
@Entity

public class UsuarioPessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // gera um novo id para um novo registro automatico
	private Long id;

	private String nome;
	private String sobreNome;
	private String sexo;
	private String login;
	private String senha;

	private String cep;
	private String localidade;
	private String uf;
	private Double salario;
	
	@Column(columnDefinition = "text")
	private String imagem;
	

	/*
	 * um para muitos, mapea o atributo responsável por identificar os emails na
	 * classe EmailUser.
	 * 
	 * fetch = FetchType.LAZY = so carrega os emails quando for invocado
	 */
	@OneToMany(mappedBy = "pessoa", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true) // traz
	private List<EmailUser> emails = new ArrayList<EmailUser>();

	/*
	 * um para muitos, mapea o atributo responsável por identificar os telefones na
	 * classe TelefoneUser.
	 * 
	 * fetch = FetchType.EAGER = sempre carrega os telefones
	 */
	@OneToMany(mappedBy = "usuarioPessoa", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true) // traz
	private List<TelefoneUser> telefoneUsers = new ArrayList<TelefoneUser>();

	public void setTelefoneUsers(List<TelefoneUser> telefoneUsers) {
		this.telefoneUsers = telefoneUsers;
	}

	public List<TelefoneUser> getTelefoneUsers() {
		return telefoneUsers;
	}

	public UsuarioPessoa() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobreNome() {
		return sobreNome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public List<EmailUser> getEmails() {
		return emails;
	}

	public void setEmails(List<EmailUser> emails) {
		this.emails = emails;
	}
	
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioPessoa other = (UsuarioPessoa) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UsuarioPessoa [id=" + id + ", nome=" + nome + ", sobreNome=" + sobreNome + ", sexo=" + sexo
				+ ", salario=" + salario + "]";
	}

}
