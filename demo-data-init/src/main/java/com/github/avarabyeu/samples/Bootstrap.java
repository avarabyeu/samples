package com.github.avarabyeu.samples;

import com.github.avarabyeu.samples.model.Person;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by andrey.vorobyov on 1/20/15.
 */
public class Bootstrap {

    public static void main(String... args) {

        Injector injector = Guice.createInjector(new JpaModule());

        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();

        List<Person> resultList = em.createQuery("from Person").getResultList();
        System.out.println(resultList);

        System.out.println(em.createQuery("from Title").getResultList());

        em.getTransaction().commit();
        injector.getInstance(PersistService.class).stop();
        System.out.println("closing...");

    }
}
