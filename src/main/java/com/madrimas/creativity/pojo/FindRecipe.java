package com.madrimas.creativity.pojo;

import com.madrimas.creativity.model.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor
public class FindRecipe {

	public enum Compare {
		Lower, Equal, Greater
	}

	private String title;

	private Compare difficultyCompare;

	private Integer difficulty;

	private Integer minutes;

	private Integer authorId;

	private boolean privateOnly;

	private Set<Ingredient> ingredients = new HashSet<>();

}
