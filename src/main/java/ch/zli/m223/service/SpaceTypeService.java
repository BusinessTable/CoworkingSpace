package ch.zli.m223.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.SpaceType;

@ApplicationScoped
public class SpaceTypeService {
    
    @Inject
    EntityManager entityManager;

    @Transactional
    public SpaceType createSpaceType(SpaceType spaceType) {
        return entityManager.merge(spaceType);
    }

    @Transactional
    public void deleteSpaceType(Long id) {
        var entity = entityManager.find(SpaceType.class, id);
        entityManager.remove(entity);
    }

    @Transactional
    public SpaceType updateSpaceType(Long id, SpaceType spaceType) {
        spaceType.setId(id);
        return entityManager.merge(spaceType);
    }

    public List<SpaceType> findAll() {
        var query = entityManager.createQuery("FROM SpaceType", SpaceType.class);
        return query.getResultList();
    }
}
