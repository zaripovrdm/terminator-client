package ru.raiffeisen.terminator.client;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * @author Roman Zaripov
 */
@Configuration
public class DataSourceConfiguration {
	
	public static final String ICDB_DS = "icdbDataSource";
	public static final String WEBDB_DS = "webdbDataSource";
	public static final String RCAS_DS = "oracleDataSource";
	public static final String SSO_DS = "ssoDataSource";
		
	@Bean(name = WEBDB_DS)
	public DataSource webdbdDataSource(	
			@Value("${db.webdb.jdbc.url}") String jdbcUrl,
			@Value("${db.webdb.username}") String username,
			@Value("${db.webdb.password}") String password,
			@Value("${db.webdb.pool.connection.max}") int maxConnections
	) {
    	BoneCPDataSource ds = new BoneCPDataSource();
    	ds.setDriverClass("com.ibm.db2.jcc.DB2Driver");
    	ds.setUsername(username);
    	ds.setPassword(password);
    	ds.setJdbcUrl(jdbcUrl);
    	ds.setDisableJMX(false);
    	
    	Properties properties = new Properties();
    	properties.setProperty("driverType", "4"); 
    	ds.setDriverProperties(properties);    	
    	
    	ds.setPartitionCount(1);
    	ds.setMaxConnectionsPerPartition(maxConnections);
    	
    	ds.setPoolName("terminator-client-webdb");
    	
    	return ds;
	}
	
	@Bean(name = ICDB_DS)
	public DataSource icbdDataSource(	
			@Value("${db.icdb.jdbc.url}") String jdbcUrl,
			@Value("${db.icdb.username}") String username,
			@Value("${db.icdb.password}") String password,
			@Value("${db.icdb.pool.connection.max}") int maxConnections
	) {
    	BoneCPDataSource ds = new BoneCPDataSource();
    	ds.setDriverClass("com.ibm.db2.jcc.DB2Driver");
    	ds.setUsername(username);
    	ds.setPassword(password);
    	ds.setJdbcUrl(jdbcUrl);
    	ds.setDisableJMX(false);
    	
    	Properties properties = new Properties();
    	properties.setProperty("driverType", "4"); 
    	ds.setDriverProperties(properties);    	
    	
    	ds.setPartitionCount(1);
    	ds.setMaxConnectionsPerPartition(maxConnections);
    	
    	ds.setPoolName("terminator-client-icdb");
    	
    	return ds;
	}
	
	@Bean(name = RCAS_DS)
	public DataSource rcasDataSource(	
			@Value("${db.rcas.jdbc.url}") String jdbcUrl,
			@Value("${db.rcas.username}") String username,
			@Value("${db.rcas.password}") String password,
			@Value("${db.rcas.pool.connection.max}") int maxConnections
	) {
    	BoneCPDataSource ds = new BoneCPDataSource();
    	ds.setDriverClass("oracle.jdbc.driver.OracleDriver");
    	ds.setUsername(username);
    	ds.setPassword(password);
    	ds.setJdbcUrl(jdbcUrl);
    	ds.setDisableJMX(false);	   	
    	ds.setPartitionCount(1);
    	ds.setMaxConnectionsPerPartition(maxConnections);
    	
    	ds.setPoolName("terminator-client-rcas");
    	
    	return ds;
	}
	
	@Bean(name = SSO_DS)
	public DataSource ssoDataSource(	
			@Value("${db.sso.jdbc.url}") String jdbcUrl,
			@Value("${db.sso.username}") String username,
			@Value("${db.sso.password}") String password,
			@Value("${db.sso.pool.connection.max}") int maxConnections
	) {
    	BoneCPDataSource ds = new BoneCPDataSource();
    	ds.setDriverClass("org.postgresql.Driver");
    	ds.setUsername(username);
    	ds.setPassword(password);
    	ds.setJdbcUrl(jdbcUrl);
    	ds.setDisableJMX(false);	   	
    	ds.setPartitionCount(1);
    	ds.setMaxConnectionsPerPartition(maxConnections);
    	
    	ds.setPoolName("terminator-client-sso");
    	
    	return ds;
	}

}
