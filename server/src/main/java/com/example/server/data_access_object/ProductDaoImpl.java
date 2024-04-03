package com.example.server.data_access_object;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.server.data_transfer_object.PageResponse;
import com.example.server.models.Category;
import com.example.server.models.Products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class ProductDaoImpl implements ProductDao{
    @Autowired
    EntityManager entityManager;

    @Override
    public PageResponse<Products> getAll(String name, Category category, int page, int size,
    String sortBy, String sortOrder) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Products> criteriaQuery = criteriaBuilder.createQuery(Products.class);
        Root<Products> productRoot = criteriaQuery.from(Products.class);

        Predicate[] predicates = createPredicate(criteriaBuilder, productRoot, name, category);
        criteriaQuery.where(predicates);

        List<Products> result = entityManager.createQuery(criteriaQuery)
        .setFirstResult((page - 1) * size)
        .setMaxResults(size)
        .getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Products> root = countQuery.from(Products.class);
        countQuery.select(criteriaBuilder.count(root))
        .where(createPredicate(criteriaBuilder, root, name, category));

        Long totalItem = entityManager.createQuery(countQuery).getSingleResult();

        return PageResponse.success(result, page, size, totalItem);
    }

    private Predicate[] createPredicate(CriteriaBuilder criteriaBuilder, Root<Products> root,
    String name, Category category) {
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isBlank() && !name.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }

        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        return predicates.toArray(new Predicate[0]);
    }
}
