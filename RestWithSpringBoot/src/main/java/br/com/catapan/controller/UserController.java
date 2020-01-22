package br.com.catapan.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.catapan.data.vo.v1.UserVO;
import br.com.catapan.services.UserServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "UserEndpoint") 
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	private UserServices service;
	
	@ApiOperation(value = "Find all users" )
	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
	public List<UserVO> findAll() {
		List<UserVO> users =  service.findAll();
		users
			.stream()
			.forEach(p -> p.add(
					linkTo(methodOn(UserController.class).findById(p.getKey())).withSelfRel()
				)
			);
		return users;
	}	
	
	@ApiOperation(value = "Find a specific user by your ID" )
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
	public UserVO findById(@PathVariable("id") Long id) {
		UserVO userVO = service.findById(id);
		userVO.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		return userVO;
	}	
	
	@ApiOperation(value = "Create a new user")
	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, 
			consumes = { "application/json", "application/xml", "application/x-yaml" })
	public UserVO create(@RequestBody UserVO book) {
		UserVO userVO = service.create(book);
		userVO.add(linkTo(methodOn(UserController.class).findById(userVO.getKey())).withSelfRel());
		return userVO;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}	
	
}