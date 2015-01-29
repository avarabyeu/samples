import com.github.avarabyeu.samples.JpaModule;
import com.github.avarabyeu.samples.MyBatisModule;
import com.github.avarabyeu.samples.dao.MyBatisSampleDao;
import com.github.avarabyeu.samples.dao.SomeSampleDao;
import com.github.avarabyeu.samples.model.Person;
import com.github.avarabyeu.samples.model.Title;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.List;


public class DatasetLoaderTest {


    @Test
    public void testJpaModule() {
        Injector injector = Guice.createInjector(new JpaModule());
        SomeSampleDao someSampleDao = injector.getInstance(SomeSampleDao.class);

        List<Person> resultList = someSampleDao.findPersons();
        System.out.println(resultList);
    }

    @Test
    public void testMyBatisModule() {
        Injector injector = Guice.createInjector(new MyBatisModule());
        SomeSampleDao someSampleDao = injector.getInstance(SomeSampleDao.class);

        List<Title> resultList = someSampleDao.findTitles();
        System.out.println(resultList);
    }
}
