import com.github.avarabyeu.samples.JpaModule;
import com.github.avarabyeu.samples.dao.SomeSampleDao;
import com.github.avarabyeu.samples.model.Person;
import com.google.inject.Guice;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;


public class DatasetLoaderTest {

    @Inject
    private SomeSampleDao someSampleDao;

    /* don't do this in your real tests! just for example... */
    @Before
    public void before() {
        Guice.createInjector(new JpaModule()).injectMembers(this);
    }

    @Test
    public void testLoading() {
        List<Person> resultList = someSampleDao.findPersons();
        System.out.println(resultList);
    }
}
