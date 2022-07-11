package model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class EmailUser implements Serializable{


	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(nullable = false)
	private String email;
	
	/* @ManyToOne Muitos email para pessoa*/
	/*optional = false obrigatorio o email pertecer uma pessoa*/
	/*fetch = FetchType.EAGER = carrega no banco de dados*/
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private UsuarioPessoa pessoa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UsuarioPessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(UsuarioPessoa pessoa) {
		this.pessoa = pessoa;
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
		EmailUser other = (EmailUser) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	
	
}
