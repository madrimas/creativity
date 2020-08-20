package com.madrimas.creativity.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "recipe_ingredients")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode
public class Recipe2Ingredient implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "recipe_id")
	private Integer recipeId;

	@Column(name = "ingredient_id")
	private Integer ingredient;

}
