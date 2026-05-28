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

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

import jakarta.validation.Valid;

@RestController // Define que a classe é um controlador REST, ou seja, ela vai receber requisições HTTP e retornar respostas HTTP
@RequestMapping("/temas")   // Define o caminho base para as requisições, ou seja, todas as requisições para essa classe devem começar com "/temas"
@CrossOrigin(origins = "*", allowedHeaders = "*")   // Define que a classe pode receber requisições de qualquer origem e com qualquer cabeçalho, ou seja, permite o acesso de qualquer aplicação
public class TemaController {
    
    @Autowired  // Define que o Spring vai criar uma instância da classe TemaRepository e injetar na variável temaRepository, ou seja, a variável temaRepository vai ser inicializada automaticamente pelo Spring
    private TemaRepository temaRepository;
    
    @GetMapping // Define que o método getAll vai ser executado quando uma requisição GET for feita para o caminho "/temas"
    public ResponseEntity<List<Tema>> getAll(){ // Define que o método vai retornar uma resposta HTTP com um corpo do tipo List<Tema>
        return ResponseEntity.ok(temaRepository.findAll()); // Retorna uma resposta HTTP com o status 200 (OK) e o corpo contendo a lista de temas encontrada no banco de dados
    }
    
    @GetMapping("/{id}")    // Define que o método getById vai ser executado quando uma requisição GET for feita para o caminho "/temas/{id}", onde {id} é um parâmetro que representa o id do tema a ser buscado
    public ResponseEntity<Tema> getById(@PathVariable Long id){ // Define que o método vai receber um parâmetro do tipo Long chamado id, que vai ser preenchido com o valor do parâmetro {id} da requisição
        return temaRepository.findById(id)  // Busca o tema no banco de dados pelo id
            .map(resposta -> ResponseEntity.ok(resposta))   // Se o tema for encontrado, retorna uma resposta HTTP com o status 200 (OK) e o corpo contendo o tema encontrado
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());   // Se o tema não for encontrado, retorna uma resposta HTTP com o status 404 (NOT FOUND) e sem corpo
    }
    
    @GetMapping("/descricao/{descricao}")   // Define que o método getAllByDescricao vai ser executado quando uma requisição GET for feita para o caminho "/temas/descricao/{descricao}", onde {descricao} é um parâmetro que representa a descrição do tema a ser buscado
    public ResponseEntity<List<Tema>> getAllByDescricao(@PathVariable   // Define que o método vai receber um parâmetro do tipo String chamado descricao, que vai ser preenchido com o valor do parâmetro {descricao} da requisição
    String descricao){  // Busca os temas no banco de dados pela descrição, ignorando maiúsculas e minúsculas
        return ResponseEntity.ok(temaRepository     // Retorna uma resposta HTTP com o status 200 (OK) e o corpo contendo a lista de temas encontrada no banco de dados
            .findAllByDescricaoContainingIgnoreCase(descricao));    //   Busca os temas no banco de dados pela descrição, ignorando maiúsculas e minúsculas
    }
    
    @PostMapping    // Define que o método post vai ser executado quando uma requisição POST for feita para o caminho "/temas"
    public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){    // Define que o método vai receber um objeto do tipo Tema no corpo da requisição, que vai ser preenchido com os dados enviados na requisição e validado de acordo com as anotações de validação presentes na classe Tema
    	
    	tema.setId(null);   // Define que o id do tema a ser criado deve ser nulo, ou seja, o banco de dados vai gerar um id automaticamente para o novo tema
    	
        return ResponseEntity.status(HttpStatus.CREATED)    // Retorna uma resposta HTTP com o status 201 (CREATED) e o corpo contendo o tema criado
                .body(temaRepository.save(tema));   // Salva o tema no banco de dados e retorna uma resposta HTTP com o status 201 (CREATED) e o corpo contendo o tema criado
    }
    
    @PutMapping    // Define que o método put vai ser executado quando uma requisição PUT for feita para o caminho "/temas"
    public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){    // Define que o método vai receber um objeto do tipo Tema no corpo da requisição, que vai ser preenchido com os dados enviados na requisição e validado de acordo com as anotações de validação presentes na classe Tema
        return temaRepository.findById(tema.getId())    // Busca o tema no banco de dados pelo id do tema recebido na requisição
            .map(resposta -> ResponseEntity.status(HttpStatus.OK)   // Se o tema for encontrado, retorna uma resposta HTTP com o status 200 (OK) e o corpo contendo o tema atualizado
            .body(temaRepository.save(tema)))   // Salva o tema atualizado no banco de dados e retorna uma resposta HTTP com o status 200 (OK) e o corpo contendo o tema atualizado
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());   // Se o tema não for encontrado, retorna uma resposta HTTP com o status 404 (NOT FOUND) e sem corpo
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)  // Define que o método delete vai retornar uma resposta HTTP com o status 204 (NO CONTENT) e sem corpo
    @DeleteMapping("/{id}") // Define que o método delete vai ser executado quando uma requisição DELETE for feita para o caminho "/temas/{id}", onde {id} é um parâmetro que representa o id do tema a ser deletado
    public void delete(@PathVariable Long id) { // Define que o método vai receber um parâmetro do tipo Long chamado id, que vai ser preenchido com o valor do parâmetro {id} da requisição
        Optional<Tema> tema = temaRepository.findById(id);  // Busca o tema no banco de dados pelo id
        
        if(tema.isEmpty())  // Se o tema não for encontrado, lança uma exceção com o status 404 (NOT FOUND) e a mensagem "Tema não encontrado!"
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);    // Lança uma exceção com o status 404 (NOT FOUND) e a mensagem "Tema não encontrado!"
        
        temaRepository.deleteById(id);  // Se o tema for encontrado, deleta o tema no banco de dados pelo id e retorna uma resposta HTTP com o status 204 (NO CONTENT) e sem corpo     
    }

}