package com.github.avarabyeu.samples.dao;

import com.github.avarabyeu.samples.model.Person;
import com.github.avarabyeu.samples.model.Title;

import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public interface SomeSampleDao {

    List<Person> findPersons();

    Person findPerson(Long id);

    List<Title> findTitles();
}
