package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoGeneric;
import dao.DaoUsuario;
import model.EmailUser;
import model.UsuarioPessoa;

@ViewScoped
@ManagedBean(name = "usuariosBean")
public class UsuariosBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private DaoUsuario<UsuarioPessoa> daoPessoa = new DaoUsuario<UsuarioPessoa>();
	
	private List<UsuarioPessoa> pessoas = new ArrayList<UsuarioPessoa>();
	private EmailUser emailUser = new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();

	private String filtonome;
	/* Responsavel pro criar o graficos do primefaces */
	private BarChartModel barChartModel = new BarChartModel();

	@PostConstruct
	public void init() {
		pessoas = daoPessoa.listar(UsuarioPessoa.class);

		montarGrafico();
	}

	private void montarGrafico() {
		barChartModel = new BarChartModel();
		
		ChartSeries userSalario = new ChartSeries("Salários dos Usuários"); /* Grupo de Funcionários */

		userSalario.setLabel("Users");
		for (UsuarioPessoa usuarioPessoa : pessoas) { /* add salario para o grupo */
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario()); /* Add Salários */
		}
		barChartModel.addSeries(userSalario); /* Add grupo no barChartModel */
		barChartModel.setTitle("Gráfico de Salários");
	}

	public String salvar() throws Exception {
		usuarioPessoa = daoPessoa.updateMerge(usuarioPessoa);
		pessoas.add(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		init();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));

		return "";

	}
	

	public String novo() {
		if (usuarioPessoa != null) {
			usuarioPessoa = new UsuarioPessoa();
		}
		return "";
	}

	public String excluir() {
		try {
			if (usuarioPessoa != null) {
				daoPessoa.removerUsuario(usuarioPessoa);
				daoPessoa.deletarporId(usuarioPessoa);
				pessoas.remove(usuarioPessoa);
				init();
				usuarioPessoa = new UsuarioPessoa();
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Removido com Sucesso!"));
			}
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Informação: ", "Existem Telefones para o Usuário!"));
			} else {
				e.printStackTrace();
			}
		}
		return "";
	}

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {

			URL url = new URL("https://viacep.com.br/ws/" + usuarioPessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();

			InputStream inputStream = connection.getInputStream();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String cep = "";

			StringBuilder jsonCep = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {

				jsonCep.append(cep);

			}

			UsuarioPessoa usuarioPessoaGson = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			usuarioPessoa.setLocalidade(usuarioPessoaGson.getLocalidade());
			usuarioPessoa.setUf(usuarioPessoaGson.getUf());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addEmail() throws Exception {
		/* Amarrando o email na pessoa */
		emailUser.setPessoa(usuarioPessoa);
		/* cadastranto o emal na lista */
		emailUser = daoEmail.updateMerge(emailUser);

		/* Adicionando email na lista */
		usuarioPessoa.getEmails().add(emailUser);

		emailUser = new EmailUser();

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", " Email savo com Sucesso!"));

	}

	public void removerEmail() throws Exception {
		String codigoemail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigoEmail");

		emailUser.setId(Long.valueOf(codigoemail));
		daoEmail.deletarporId(emailUser);
		usuarioPessoa.getEmails().remove(emailUser);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado... ", " Email Removido com Sucesso!"));

	}
	
	
	
	
	public void upload(FileUploadEvent image) {
		
		/*base64*/
		String imagem = "data:image/png;base64," +DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem);
		
	}
	
	
	public void download() throws IOException {
		
		Map<String, String> params = FacesContext.getCurrentInstance()
									 .getExternalContext().getRequestParameterMap();
		
		String fileDownloadId = params.get("fileDownloadUserID");
		
		UsuarioPessoa pessoa = daoPessoa.pesquisarporid(Long.parseLong(fileDownloadId), UsuarioPessoa.class);
		
		byte[] imagem = new Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);
		
		HttpServletResponse response = (HttpServletResponse) 
										FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		response.addHeader("Content-Disposition", "attachment; filename=download.png");
		response.setContentType("application/octet-stream");
		response.setContentLength(imagem.length);
		response.getOutputStream().write(imagem);
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
		
	}
	
	public void buscarporNome() {
		
		pessoas = daoPessoa.filtrar(filtonome);
		montarGrafico();
		
	}

	public List<UsuarioPessoa> getPessoas() {

		return pessoas;
	}

	public void setPessoas(List<UsuarioPessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	public EmailUser getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(EmailUser emailUser) {
		this.emailUser = emailUser;
	}

	public void setFiltonome(String filtonome) {
		this.filtonome = filtonome;
	}

	public String getFiltonome() {
		return filtonome;
	}

}
