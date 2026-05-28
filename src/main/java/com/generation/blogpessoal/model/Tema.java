package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name ="tb_temas")

public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O Atributo Descrição é obrigrátorio")
	private String descricao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) // Define o relacionamento entre as tabelas | "FetchType.LAZY" = Carrega os dados apenas quando necessário | Um tema para muitas postagens | CascadeType.REMOVE define que quando um tema for deletado, todas as postagens relacionadas a ele também serão deletadas
	@JsonIgnoreProperties(value = "tema", allowSetters = true) // Define que a propriedade tema da classe Postagem deve ser ignorada na serialização para evitar loop infinito | allowSetters = true permite que a propriedade tema seja setada na classe Postagem
	private List<Postagem> postagem; // Define a lista de postagens relacionadas ao tema | List<Postagem> postagem = new ArrayList<>(); | List<Postagem> postagem = new LinkedList<>();
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Postagem> getPostagem() {
		return postagem;
	}

	public void setPostagem(List<Postagem> postagem) {
		this.postagem = postagem;
	}

	

}
