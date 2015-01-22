package com.github.avarabyeu.samples;


import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.fileloader.DataFileLoader;
import org.dbunit.util.fileloader.FlatXmlDataFileLoader;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Andrey Vorobyov
 */
class DataSetLoader {

    @Inject
    @Named("dataset.path")
    private String resourceName;

    @Inject
    @Named("dbunit.datatype.factory")
    private String dataTypeFactory;

    public void importData(Connection connection) throws SQLException {

        DataFileLoader loader = new FlatXmlDataFileLoader();
        IDataSet ds = loader.load(resourceName);
        try {
            DatabaseOperation.CLEAN_INSERT.execute(wrapConnection(connection), ds);
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
