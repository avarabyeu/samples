package com.github.avarabyeu.samples.dao;

import com.github.avarabyeu.samples.model.Person;
import com.github.avarabyeu.samples.model.Title;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Andrei Varabyeu
 */
public interface MyBatisSampleDao extends SomeSampleDao {

    @Override
    @Select("SELECT * FROM person")
    List<Person> findPersons();

    @Override
    @Select("SELECT * FROM person WHERE id=#{id}")
    Person findPerson(Long id);

    /*
     * "LEFT OUTER JOIN person p ON p.id=t.person_id")
     * WARNING! You can add JOIN, but in this case this doesn't make sense.
     * MyBatis doesn't support join mapping via annotations
     *
     * So, there will be n+1 selects from database where N - count of joined objects.
     * This is not efficient in production!
     */
    @Override
    @Select("SELECT * FROM title t")
    @Results({@Result(property = "person", column = "person_id", javaType = Person.class, one = @One(select = "findPerson"))})
    List<Title> findTitles();
}
