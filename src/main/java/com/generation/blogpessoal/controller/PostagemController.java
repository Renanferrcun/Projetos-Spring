package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class PostagemController {

	//Basicamente é um atalho para Criar a classe repo | Implementar os métodos da Interfaçe Repositóry | Intânciar um objeto
	@Autowired	// Responsável por criar uma instância do PostagemRepository e injetá-la na classe PostagemController, permitindo que o controlador acesse os métodos do repositório para realizar operações de banco de dados relacionadas às postagens.
	private PostagemRepository postagemRepository; // Responsável por fornecer métodos para realizar operações de banco de dados relacionadas às postagens, como salvar, buscar, atualizar e excluir postagens.
	
	@GetMapping	//	<- "GET" | POST | PUT | DELETE
	public ResponseEntity<List<Postagem>> buscarTodasAsPostagens(){
		return 
			ResponseEntity.ok(
				postagemRepository.findAll()
			); // O método findAll() é um método fornecido pelo JpaRepository que retorna uma lista de todas as entidades do tipo Postagem presentes no banco de dados. O ResponseEntity.ok() é usado para criar uma resposta HTTP com status 200 (OK) e o corpo da resposta contendo a lista de postagens retornada pelo findAll().
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id) {
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo) {
		return 
				ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
		
		postagem.setId(null);
		
		return 
				ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
	}
	
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
		return 
				postagemRepository.findById(postagem.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
					.body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());	
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		postagemRepository.deleteById(id);
	}

}
