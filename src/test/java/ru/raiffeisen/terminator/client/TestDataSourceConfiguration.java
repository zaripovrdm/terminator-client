package ru.raiffeisen.terminator.client;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * @author Roman Zaripov
 */
@Configuration
public class TestDataSourceConfiguration {
	
	@Primary
	@Bean(name = DataSourceConfiguration.WEBDB_DS)
	public DataSource webdbDataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2) 
			.addScript("h2-webdb.sql")
			.build();		

	}
	
	@Bean(name = DataSourceConfiguration.ICDB_DS)
	public DataSource icdbDataSource() {
		return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2) 
			.addScript("h2-icdb.sql")
			.build();		
	}
}
