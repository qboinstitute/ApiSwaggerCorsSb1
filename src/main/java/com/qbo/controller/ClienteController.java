package com.qbo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qbo.exception.ResourceNotFoundException;
import com.qbo.model.Cliente;
import com.qbo.service.ClienteService;

/*CrossOrigin debe utilizarse por proyecto frontend*/
@RestController
@RequestMapping("api/v1/cliente")
//@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
public class ClienteController {

	@Autowired
	protected ClienteService servicio;
	
	@GetMapping("/pageable")
	public ResponseEntity<?> searchByName(@RequestParam String nombre, 
			Pageable pageable){
		Page<Cliente> clientes = servicio.searchByName(nombre, pageable);
		if(clientes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}
	
	
	@GetMapping("/dni/{dni}")
	public ResponseEntity<Cliente> searchByDni(@PathVariable("dni")String dni){
		Cliente cliente = servicio.searchByDni(dni)
				.orElseThrow(()-> 
				new ResourceNotFoundException("Not found client with DNI="+dni));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}
	
	//@CrossOrigin(origins = "*")//@CrossOrigin(origins = "http://patitas.com")
	@GetMapping("")
	public ResponseEntity<List<Cliente>> getAll(){
		List<Cliente> clientes = new ArrayList<Cliente>();
		servicio.findAll().forEach(clientes::add);
		if(clientes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
		}
		return new ResponseEntity<>(clientes, HttpStatus.OK);
		
		}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> getById(@PathVariable("id") long id){
		Cliente cliente = servicio.findById(id)
				.orElseThrow(()-> 
				new ResourceNotFoundException("Not found State wiht id= "+ id));
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("")
	public ResponseEntity<Cliente> createState(@RequestBody Cliente cliente){
		return new ResponseEntity<>(servicio.save(cliente), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> updateState(@PathVariable("id")long id, 
			@RequestBody Cliente cliente){
		Cliente _cliente = servicio.findById(id)
				.orElseThrow(()-> 
				new ResourceNotFoundException("Not found State wiht id= "+ id));
		_cliente.setNomcliente(cliente.getNomcliente());
		_cliente.setApecliente(cliente.getApecliente());
		_cliente.setDnicliente(cliente.getDnicliente());
		return new ResponseEntity<>(servicio.save(_cliente), HttpStatus.OK);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteState(@PathVariable("id")long id){
		servicio.findById(id)
		.orElseThrow(()-> 
		new ResourceNotFoundException("Not found State wiht id= "+ id));
		return ResponseEntity.status(HttpStatus.OK).body(servicio.deleteById(id));
	}
}
