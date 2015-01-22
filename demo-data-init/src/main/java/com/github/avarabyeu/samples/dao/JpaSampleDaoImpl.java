package com.github.avarabyeu.samples.dao;

import com.github.avarabyeu.samples.model.Person;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public class JpaSampleDaoImpl implements SomeSampleDao {

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    @Override
    @Transactional
    public List<Person> findPersons() {
        return entityManagerProvider.get().createQuery("from Person", Person.class).getResultList();
    }

    @Override
    @Transactional
    public Person findPerson(Long id) {
        return entityManagerProvider.get().find(Person.class, id);
    }
}
