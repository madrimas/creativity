package com.madrimas.creativity.dao;

import com.madrimas.creativity.model.Recipe;
import com.madrimas.creativity.pojo.FindRecipe;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RecipeDAO implements RecipeRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public List<Recipe> findRecipes(Integer currentUserId, FindRecipe findRecipe) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Recipe> query = cb.createQuery(Recipe.class);
		Root<Recipe> recipeRoot = query.from(Recipe.class);

		Path<String> titlePath = recipeRoot.get("title");
		Path<Integer> difficultyPath = recipeRoot.get("difficulty");
		Path<Integer> minutesPath = recipeRoot.get("minutes");
		Path<Integer> authorPath = recipeRoot.get("authorId");
		Path<Boolean> privateOnlyPath = recipeRoot.get("privateOnly");

		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotEmpty(findRecipe.getTitle())) {
			predicates.add(cb.like(
					cb.lower(titlePath),
					"%" + findRecipe.getTitle().toLowerCase() + "%"
			));
		}

		Integer difficulty = findRecipe.getDifficulty();
		FindRecipe.Compare compare = findRecipe.getDifficultyCompare();
		if (difficulty != null && compare != null) {
			Predicate predicate = switch (compare) {
				case Lower -> cb.lessThan(difficultyPath, difficulty);
				case Equal -> cb.equal(difficultyPath, difficulty);
				case Greater -> cb.greaterThan(difficultyPath, difficulty);
				default -> null;
			};

			predicates.add(predicate);
		}

		if (findRecipe.getMinutes() != null) {
			predicates.add(cb.lessThan(minutesPath, findRecipe.getMinutes()));
		}

		if (findRecipe.getAuthorId() != null) {
			predicates.add(cb.equal(authorPath, findRecipe.getAuthorId()));
		}

		predicates.add(cb.equal(privateOnlyPath, findRecipe.isPrivateOnly()));

		recipeRoot.fetch("ingredients", JoinType.LEFT);

		query.select(recipeRoot)
				.where(cb.and(predicates.toArray(new Predicate[0])));


		return entityManager.createQuery(query)
				.getResultList()
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}
}
