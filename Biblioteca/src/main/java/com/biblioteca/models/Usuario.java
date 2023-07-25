package com.biblioteca.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;






@Entity 
//indica que essa classe representa uma entidade no  banco de dados
//entidade é uma tabela, entidade é algo que possui atributos
@Table(name = Usuario.TABLE_NAME) //especifica o nome da tabela
public class Usuario {
	
	public static final Long USUARIO_BIBLIOTECA_ID = 8L;
	
	public static final String TABLE_NAME = "usuario"; //indica o nome da tabela que corresponde a classe Usuario
	
	
	@Id //primeira chave da tabela
	@GeneratedValue(strategy = GenerationType.IDENTITY) //indica que o valor do id vai ser criado automaticamente pelo bc
	@Column(name = "id", unique = true) // fala que o atributo abaixo id deve ser armazenado na coluna id
	private Long id;
	
	@Column(name = "username", length = 100, nullable = false, unique = true)
	//fala que o atributo username deve ser atribuido na coluna username
	//especifica para o banco de dados que ele não pode se falso e o maximo de tamanho de armazenamento no banco de dados
	//maximo 100 caracteres e ele deve ser único
	@NotNull
	//garante que a atribuição não é nula
	@NotEmpty
	//garante que a atribuição da string não esteja vazia ("")
	@Size( min = 2, max = 100)
	private String username;
	
	
	@JsonProperty(access = Access.WRITE_ONLY)
	//usar esse JsonProperty significa:
	//que ao converter o objeto JSON, não ira mostrar a senha
	//ira mostrar a propriedade password
	@Column(name = "password", length = 60, nullable = false)
	//é usada para atribuir o password a coluna password, diz que o maximo de caracteres é 60 e que não pode ser nulo
	@NotNull
	//garante que a atribuição não é nula
	@NotEmpty
	//garante que a atribuição da string não esteja vazia ("")
	@Size (min = 8, max = 60)
	private String password;
	
	
	//@OneToMany(mappedBy = "usuario")
	//indica que uma pessoa pode obter muitos livros
	//private List<Livro> livros = new ArrayList<Livro>();
	//livros é uma lista de objetos Livro, ele cria umma nova lista vazia de Livro

	public Usuario() {
	}
	//construtor vazio
	
	
	public Usuario(Long id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}
	//construtor parametrizados, ele inicia os valores


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	/*public List<Livro> getLivros() {
		return livros;
	}


	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}
	
	
	*/
	
}
