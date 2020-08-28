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

	public interface DifficultyLevel {
		Integer EASY = 1;
		Integer MEDIUM = 2;
		Integer HARD = 3;
	}

	public enum Difficulty {
		Easy, Medium, Hard
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "difficulty")
	private Integer difficulty;

	@Column(name = "minutes")
	private Integer minutes;

	@Column(name = "instruction")
	private String instruction;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", insertable = false, updatable = false)
	private User author;

	@Column(name = "author_id")
	private Integer authorId;

	@Column(name = "private_only")
	private boolean privateOnly;

	@ManyToMany(cascade = {CascadeType.MERGE})
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
	public void prePersist() {
		authorId = author.getId();
	}

}
