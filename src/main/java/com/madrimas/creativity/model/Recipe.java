package com.madrimas.creativity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter @Setter @NoArgsConstructor
public class Recipe {

	interface DifficultyLevel {
		int EASY = 1;
		int MEDIUM = 2;
		int HARD = 3;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "difficulty")
	private int difficulty;

	@Column(name = "minutes")
	private int minutes;

	@Column(name = "instruction")
	private String instruction;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", insertable = false, updatable = false)
	private User author;

	@Column(name = "author_id")
	private Integer authorId;

	@Column(name = "private_only")
	private boolean privateOnly;

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
			name = "recipe_ingredients",
			joinColumns = {@JoinColumn(name = "recipe_id")},
			inverseJoinColumns = {@JoinColumn(name = "ingredient_id")}
	)
	private Set<Ingredient> ingredients = new HashSet<>();

	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Column(name = "modification_date")
	private LocalDateTime modificationDate;

	@PrePersist
	public void prePersist(){
		authorId = author.getId();
	}

}
