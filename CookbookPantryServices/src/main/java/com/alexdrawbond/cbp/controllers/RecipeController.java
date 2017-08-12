package com.alexdrawbond.cbp.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alexdrawbond.cbp.domain.Recipe;
import com.alexdrawbond.cbp.services.RecipeService;

@RestController
@RequestMapping("/cookbookpantry")
public class RecipeController {
	@Autowired
	RecipeService service;
	
	@RequestMapping(value = "/recipes/{id}", method = RequestMethod.GET)
	public ResponseEntity<Recipe> getRecipe(@PathVariable(required = false) Long id, HttpServletRequest request) {
		Recipe recipe = service.getRecipe(id);

		buildLinks(recipe, id, request);
		
		return new ResponseEntity<Recipe> (recipe, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/recipes", method = RequestMethod.POST) 
	public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe, HttpServletRequest request) {
		recipe = service.addRecipe(recipe);
		
		buildLinks(recipe, recipe.getDbId(), request);
		
		return new ResponseEntity<Recipe> (recipe, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/recipes/{id}", method = RequestMethod.PUT) 
	public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe, HttpServletRequest request) {
		recipe = service.updateRecipe(id, recipe);
		
		buildLinks(recipe, id, request);
		
		return new ResponseEntity<Recipe> (recipe, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/recipes/{id}", method = RequestMethod.DELETE) 
	public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable(required = false) Long id) {
		service.deleteRecipe(id);
		return new ResponseEntity<HttpStatus> (HttpStatus.NO_CONTENT);
	}
	
	private void buildLinks(Recipe recipe, Long id, HttpServletRequest request) {
		recipe.add(linkTo(methodOn(RecipeController.class).getRecipe(id, request)).withSelfRel());
		recipe.add(linkTo(methodOn(IngredientsController.class).getIngredients(id, request)).withRel("ingredients"));
		recipe.add(linkTo(methodOn(InstructionController.class).getInstructionsForRecipe(id, request)).withRel("instructions"));
	}
	
}
