package com.github.avarabyeu.samples;

import com.github.avarabyeu.samples.dao.JpaSampleDaoImpl;
import com.github.avarabyeu.samples.dao.SomeSampleDao;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

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
        public DatabaseInitializer(EntityManager entityManager, DataSetLoader datasetLoader) {
            init(entityManager, datasetLoader);
        }

        @Transactional
        private void init(EntityManager entityManager, final DataSetLoader datasetLoader) {
            Session unwrap = entityManager.unwrap(Session.class);
            unwrap.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    datasetLoader.importData(connection);
                }
            });
        }
    }

}
