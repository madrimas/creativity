package com.madrimas.creativity.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ingredients")
@Getter @Setter @NoArgsConstructor
public class Ingredient {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id", insertable = false, updatable = false)
	private User author;

	@Column(name = "author_id")
	private Integer authorId;

	@ManyToMany(mappedBy = "ingredients")
	private Set<Recipe> recipes = new HashSet<>();

	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	@Column(name = "modification_date")
	private LocalDateTime modificationDate;

	@PrePersist
	public void prePersist() {
		authorId = author.getId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Ingredient that = (Ingredient) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(description, that.description) &&
				Objects.equals(authorId, that.authorId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, authorId);
	}
}
