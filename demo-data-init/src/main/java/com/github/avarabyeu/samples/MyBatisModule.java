package com.github.avarabyeu.samples;

import com.github.avarabyeu.samples.dao.MyBatisSampleDao;
import com.github.avarabyeu.samples.dao.SomeSampleDao;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Module configuration for MyBatis instead of JPA
 *
 * @author Andrei Varabyeu
 */
public class MyBatisModule extends AbstractModule {

    private static final String APP_PROPERTIES = "mybatis.properties";

    @Override
    protected void configure() {

        Names.bindProperties(binder(), loadConfiguration());

         /* MyBatis configuration */
        binder().install(new org.mybatis.guice.MyBatisModule() {
            @Override
            protected void initialize() {
                install(JdbcHelper.HSQLDB_Embedded);

                bindDataSourceProviderType(PooledDataSourceProvider.class);
                bindTransactionFactoryType(JdbcTransactionFactory.class);
                addMapperClass(MyBatisSampleDao.class);
            }

        });

        binder().bind(SomeSampleDao.class).to(MyBatisSampleDao.class);

        binder().bind(DatabaseInitializer.class).asEagerSingleton();
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

    public static class DatabaseInitializer {

        private static final String INIT_DDL_SCRIPT = "init_ddl.sql";
        private SqlSessionFactory sessionFactory;

        @Inject
        public DatabaseInitializer(SqlSessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
            loadInitScript();
        }

        private void loadInitScript() {
            try (SqlSession sqlSession = sessionFactory.openSession(true);
                 Connection connection = sqlSession.getConnection()) {
                new ScriptRunner(connection).runScript(Resources.asCharSource(Resources.getResource(INIT_DDL_SCRIPT), Charsets.UTF_8).openBufferedStream());
            } catch (IOException | SQLException e) {
                throw new IllegalStateException("Unable to execute init script!", e);
            }
        }
    }
}
