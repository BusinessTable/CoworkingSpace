package ch.zli.m223.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.Space;

@ApplicationScoped
public class SpaceService {
    
    @Inject
    EntityManager entityManager;

    @Transactional
    public Space createSpace(Space space) {
        return entityManager.merge(space);
    }

    @Transactional
    public void deleteSpace(Long id) {
        var entity = entityManager.find(Space.class, id);
        entityManager.remove(entity);
    }

    @Transactional
    public Space updateSpace(Long id, Space space) {
        space.setId(id);
        return entityManager.merge(space);
    }

    public List<Space> findAll() {
        var query = entityManager.createQuery("FROM Space", Space.class);
        return query.getResultList();
    }
}
