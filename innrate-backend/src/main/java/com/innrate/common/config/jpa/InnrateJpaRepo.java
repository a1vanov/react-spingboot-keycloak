package com.innrate.common.config.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class InnrateJpaRepo<T, ID> extends SimpleJpaRepository<T, ID> {

    private final JpaEntityInformation<T, ?> entityInformation;

    @Autowired
    public InnrateJpaRepo(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
    }

    public InnrateJpaRepo(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    @Override
    public void deleteById(@Nonnull ID id) {
        T t = findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("No %s entity with id %s exists.", entityInformation.getJavaType(), id)));
        delete(t);
    }
}
