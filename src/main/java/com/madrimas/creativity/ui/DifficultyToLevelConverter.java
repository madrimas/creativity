package com.madrimas.creativity.ui;

import com.madrimas.creativity.model.Recipe;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class DifficultyToLevelConverter implements Converter<Recipe.Difficulty, Integer> {

	@Override
	public Result<Integer> convertToModel(Recipe.Difficulty value, ValueContext context) {
		Integer difficultyLevel = switch (value) {
			case Easy -> Recipe.DifficultyLevel.EASY;
			case Medium -> Recipe.DifficultyLevel.MEDIUM;
			case Hard -> Recipe.DifficultyLevel.HARD;
		};

		return Result.ok(difficultyLevel);
	}

	@Override
	public Recipe.Difficulty convertToPresentation(Integer value, ValueContext context) {
		return switch (value) {
			case 1 -> Recipe.Difficulty.Easy;
			case 2 -> Recipe.Difficulty.Medium;
			case 3 -> Recipe.Difficulty.Hard;
			default -> throw new IllegalStateException("Unexpected value: " + value);
		};
	}
}
