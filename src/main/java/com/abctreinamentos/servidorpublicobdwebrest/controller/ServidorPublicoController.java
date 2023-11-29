package com.abctreinamentos.servidorpublicobdwebrest.controller;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.abctreinamentos.servidorpublicobdwebrest.api.ServidorPublicoAPIRest;
import com.abctreinamentos.servidorpublicobdwebrest.entity.ServidorPublico;
import com.abctreinamentos.servidorpublicobdwebrest.service.ServidorPublicoService;

@RestController
public class ServidorPublicoController implements ServidorPublicoAPIRest{
	
	private ServidorPublicoService servidorService;
	
	@Autowired
	public void setServidorPublicoService(ServidorPublicoService servidorService)
	{
		this.servidorService = servidorService;
	}

	@GetMapping("/listarServidores")
	public ResponseEntity<List<ServidorPublico>> listarServidores() {
		List<ServidorPublico> servidorespublicos = servidorService.listAll();
		return new ResponseEntity<List<ServidorPublico>>(servidorespublicos,HttpStatus.OK);
	}

	@GetMapping("/listarServidor/{matricula}")
	public ResponseEntity<ServidorPublico> listarServidor(long matricula) {
		
		Optional<ServidorPublico> servidorEncontrado = servidorService.listByMatricula(matricula);
		
		if (servidorEncontrado.isPresent())
			return new ResponseEntity<ServidorPublico>(servidorEncontrado.get(),HttpStatus.OK);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Servidor Público Não Encontrado");
	}

	@DeleteMapping("/excluirServidor/{matricula}")
	public void excluirServidor(long matricula) {
		
		Optional<ServidorPublico> servidorEncontrado = servidorService.listByMatricula(matricula);
		
		if (servidorEncontrado.isPresent())
		{
			servidorService.delete(matricula);
			throw new ResponseStatusException(HttpStatus.OK,"Servidor Público Excluído");
		}
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Servidor Público Não Encontrado");
	}

	@PutMapping("/editarServidor/{matricula}")
	public String editarServidor(long matricula, ServidorPublico servidorAlterado) {
		
		Optional<ServidorPublico> servidorEncontrado = servidorService.listByMatricula(matricula);
		
		if (servidorEncontrado.isPresent())
		{
			servidorService.update(servidorAlterado);
			throw new ResponseStatusException(HttpStatus.OK,"Servidor Público Alterado");
		}
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Servidor Público Não Encontrado");

	}

	@PostMapping("/cadastrarServidor")
	public String cadastrarServidor(ServidorPublico novoservidor) {
		
		Optional<ServidorPublico> servidorEncontrado = servidorService.listByMatricula(novoservidor.getMatricula());
		
		if (!servidorEncontrado.isPresent())
		{
			servidorService.save(novoservidor);
			throw new ResponseStatusException(HttpStatus.OK,"Servidor Público Cadastrado");
		}
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Servidor Público Já Existente");	
	}
	
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Object> handleNotFoundException(ResponseStatusException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("message", ex.getReason());
		body.put("status", ex.getStatusCode());
		return new ResponseEntity<>(body, ex.getStatusCode());
	} 
	
	
}
