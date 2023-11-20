package ch.zli.m223.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import ch.zli.m223.model.UserType;

@ApplicationScoped
public class UserTypeService {
    
    @Inject
    EntityManager entityManager;

    @Transactional
    public UserType createUserType(UserType userType) {
        return entityManager.merge(userType);
    }

    @Transactional
    public void deleteUserType(Long id) {
        var entity = entityManager.find(UserType.class, id);
        entityManager.remove(entity);
    }

    @Transactional
    public UserType updateUserType(Long id, UserType userType) {
        userType.setId(id);
        return entityManager.merge(userType);
    }

    public List<UserType> findAll() {
        var query = entityManager.createQuery("FROM UserType", UserType.class);
        return query.getResultList();
    }
}
