package com.biblioteca.models;

import java.util.List;

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
@Table(name = Livro.TABLE_NAME)
public class Livro {
	
	public static final String TABLE_NAME = "livro";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Long id;

	
	@Column(name = "nomeLivro", length = 255, nullable = false)
	@NotNull
	@NotEmpty
	@Size(min = 1, max = 255)
	private String nomeLivro;
	
	@Column(name = "emprestado", nullable = false)
    private boolean emprestado;
	
	@Column(name = "quantidade_emprestimos", nullable = false, columnDefinition = "int default 0")
	private int quantidadeEmprestimos;

	
	//novo
	@OneToMany(mappedBy = "livro")
	private List<Emprestimo> emprestimos;
	//novo



	public Livro() {
	}

	public Livro(Long id, Usuario usuario, String nomeLivro,
			boolean emprestado, List<Emprestimo> emprestimos, Integer quantidadeEmprestimos) {
		super();
		this.id = id;
		this.nomeLivro = nomeLivro;
		this.emprestado = emprestado;
		this.emprestimos = emprestimos;
		this.quantidadeEmprestimos = 0; 
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeLivro() {
		return nomeLivro;
	}

	public void setNomeLivro(String nomeLivro) {
		this.nomeLivro = nomeLivro;
	}

	public List<Emprestimo> getEmprestimos() {
		return emprestimos;
	}

	public void setEmprestimos(List<Emprestimo> emprestimos) {
		this.emprestimos = emprestimos;
	}

	
	public boolean isEmprestado() {
		return emprestado;
	}

	public void setEmprestado(boolean emprestado) {
		if (this.emprestado != emprestado && emprestado) {
			// O livro não estava emprestado e agora foi emprestado, incrementa a quantidade de empréstimos
			this.quantidadeEmprestimos++;
		}
		this.emprestado = emprestado;
	}

	public int getQuantidadeEmprestimos() {
		return quantidadeEmprestimos;
	}

	public void setQuantidadeEmprestimos(int quantidadeEmprestimos) {
		this.quantidadeEmprestimos = quantidadeEmprestimos;
	}	
	
	

}
