package com.github.avarabyeu.samples;


import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Andrey Vorobyov
 */
class DataSetLoader {

    @Inject
    @Named("dbunit.datatype.factory")
    private String dataTypeFactory;

    public void importData(Connection connection, InputStream data) throws SQLException {
        try {
            DatabaseOperation.CLEAN_INSERT.execute(wrapConnection(connection), new FlatXmlDataSetBuilder().build(data));
        } catch (DatabaseUnitException e) {
            throw new IllegalArgumentException("Incorrect dataset provided", e);
        }
    }

    private IDatabaseConnection wrapConnection(Connection connection) throws DatabaseUnitException {
        DatabaseConnection databaseConnection = new DatabaseConnection(connection);
        Properties props = new Properties();
        props.put(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory);
        databaseConnection.getConfig().setPropertiesByString(props);
        return databaseConnection;
        //this.connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
    }


}
