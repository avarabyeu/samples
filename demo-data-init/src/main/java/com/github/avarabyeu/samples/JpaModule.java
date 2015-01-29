package com.github.avarabyeu.samples;

import com.github.avarabyeu.samples.dao.JpaSampleDaoImpl;
import com.github.avarabyeu.samples.dao.SomeSampleDao;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

/**
 * @author Andrey Vorobyov
 */
public class JpaModule extends AbstractModule {

    private static final String APP_PROPERTIES = "app.properties";
    private static final String PERSISTANCE_UNIT_NAME = "testunit";

    @Override
    protected void configure() {

        /* bind properties to Guice app context */
        Names.bindProperties(binder(), loadConfiguration());

        install(new JpaPersistModule(PERSISTANCE_UNIT_NAME));

        /* Once Guice doesn't have lifecycle support (JSR-250) out of the box, we use eager initialization
        *  with post-construct logic in constructors
        */
        bind(PersistanceInitializer.class).asEagerSingleton();
        bind(DatabaseInitializer.class).asEagerSingleton();

        bind(SomeSampleDao.class).to(JpaSampleDaoImpl.class);


    }

    /* actually, there is no need to push TemplateEngine into Guice context. For this example it's gonna be used only once
     * and might be put into DatabaseInitializer
     */
    @Provides
    @Singleton
    public TemplateEngine provideTemplateEngine() {
        Version freemarkerVersion = Configuration.VERSION_2_3_21;
        Configuration configuration = new Configuration(freemarkerVersion);
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        /* this is default date-time (timestamp) format for DBUnit */
        configuration.setDateTimeFormat("yyyy-MM-dd hh:mm:ss.000");

        try {
            TemplateModel dateUtilsTemplateModel = new BeansWrapperBuilder(freemarkerVersion).build().getStaticModels().get(DateUtils.class.getCanonicalName());
            configuration.setSharedVariable("dateUtils", dateUtilsTemplateModel);

            TemplateModel stringRandomTemplateModel = new BeansWrapperBuilder(freemarkerVersion).build().getStaticModels().get(RandomStringUtils.class.getCanonicalName());
            configuration.setSharedVariable("stringRandom", stringRandomTemplateModel);

            configuration.setSharedVariable("random", new Random());

        } catch (TemplateModelException e) {
            throw new IllegalStateException("Unable to initialize template shared variables", e);
        }
        return new TemplateEngine(configuration);

    }


    private Properties loadConfiguration() {
        Properties properties;
        try {
            properties = new Properties();
            properties.load(Resources.getResource(APP_PROPERTIES).openStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load properties", e);
        }
        return properties;
    }


    public static class PersistanceInitializer {
        @Inject
        public PersistanceInitializer(PersistService persistService) {
            persistService.start();
        }
    }

    public static class DatabaseInitializer {
        @Inject
        public DatabaseInitializer(EntityManager entityManager,
                                   DataSetLoader datasetLoader,
                                   TemplateEngine templateEngine,
                                   @Named("dataset.template.name") String datasetTemplate) {
            byte[] dataset = templateEngine.merge(datasetTemplate, null);
            initDatabase(entityManager, datasetLoader, new ByteArrayInputStream(dataset));
        }

        @Transactional
        private void initDatabase(EntityManager entityManager, final DataSetLoader datasetLoader, final InputStream dataset) {

            Session unwrap = entityManager.unwrap(Session.class);
            unwrap.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    datasetLoader.importData(connection, dataset);
                }
            });
        }
    }

}
