package com.madrimas.creativity.service;

import com.madrimas.creativity.dao.IngredientRepository;
import com.madrimas.creativity.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class IngredientService {

	@Autowired
	IngredientRepository ingredientRepository;

	public List<Ingredient> getIngredients(String filter) {
		if(StringUtils.isEmpty(filter)){
			return ingredientRepository.findAll();
		} else {
			return ingredientRepository.search(filter);
		}
	}
}
