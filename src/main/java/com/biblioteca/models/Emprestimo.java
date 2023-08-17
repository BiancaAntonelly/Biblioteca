package com.biblioteca.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
//informa que essa classe é uma entidade
@Table(name = "emprestimo")
//cria uma tabela com nome empréstimo
public class Emprestimo {
    
	//id do emprestimo
	@Id
	//primeira chave da tabela
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//indica que o valor do id vai ser criado automaticamente pelo banco de dados
    private Long id;

	
	@ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    
    @ManyToOne
    //muitos emprestimos para um usuario
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_emprestimo", nullable = false)
    //cria uma coluna para data_emprestimo
    private LocalDate dataEmprestimo;
    //LocalDate é uma class do pacote java.time
    //uma data sem considerar a hora

    @Column(name = "data_prazo_entrega", nullable = false)
  //cria uma coluna para data_prazo_entrega
    private LocalDate dataPrazoEntrega;

    @Column(name = "data_devolucao")
  //cria uma coluna para data_devolução
    private LocalDate dataDevolucao;

    @Column(nullable = false)
    private String status;

	public Emprestimo() {
	}

	public Emprestimo(Long id, Livro livro, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataPrazoEntrega,
			LocalDate dataDevolucao, String status) {
		this.id = id;
		this.livro = livro;
		this.usuario = usuario;
		this.dataEmprestimo = dataEmprestimo;
		this.dataPrazoEntrega = dataPrazoEntrega;
		this.dataDevolucao = dataDevolucao;
		this.status = status;
	}
	//contrutor parametrizado

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public LocalDate getDataPrazoEntrega() {
		return dataPrazoEntrega;
	}

	public void setDataPrazoEntrega(LocalDate dataPrazoEntrega) {
		this.dataPrazoEntrega = dataPrazoEntrega;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
