package ru.raiffeisen.terminator.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ru.raiffeisen.terminator.client.bean.ClientWriterWebdb;
import ru.raiffeisen.terminator.client.model.Client;
import ru.raiffeisen.terminator.common.integration.ErrorFlowConfiguration;
import ru.raiffeisen.terminator.common.jmx.ProcessJmxConfiguration;

/**
 * @author Roman Zaripov
 */
@Configuration
@EnableIntegration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = ClientWriterWebdb.class)
@Import({ErrorFlowConfiguration.class, ProcessJmxConfiguration.class})
@PropertySource({
	"classpath:data.properties", 
	"classpath:system.properties", 
	"classpath:jms.properties", 
	"classpath:sql/icdb.xml", 
	"classpath:sql/webdb.xml",
	"classpath:sql/rcas.xml",
	"classpath:sql/sso.xml"
})
public class CoreConfiguration {
	
	@Autowired @Qualifier(DataSourceConfiguration.WEBDB_DS)
	private DataSource webdbDataSource;
	
	@Bean
	public NamedParameterJdbcTemplate webdbJdbcTemplate() {
		return new NamedParameterJdbcTemplate(webdbDataSource);
	}
	
	@Bean
	public NamedParameterJdbcTemplate icdbJdbcTemplate(@Autowired @Qualifier(DataSourceConfiguration.ICDB_DS) DataSource icdbDataSource) {
		return new NamedParameterJdbcTemplate(icdbDataSource);
	}
	
	@Bean
	public NamedParameterJdbcTemplate rcasJdbcTemplate(@Autowired @Qualifier(DataSourceConfiguration.RCAS_DS) DataSource rcasDataSource) {
		return new NamedParameterJdbcTemplate(rcasDataSource);
	}
	
	@Bean
	public NamedParameterJdbcTemplate ssoJdbcTemplate(@Autowired @Qualifier(DataSourceConfiguration.SSO_DS) DataSource ssoDataSource) {
		return new NamedParameterJdbcTemplate(ssoDataSource);
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(webdbDataSource);
	}
	
	@Bean
	public JmsTemplate jmsTemplate(@Autowired ConnectionFactory connectionFactory, @Autowired Topic topic) {		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(Client.class.getPackage().getName());
		
		MarshallingMessageConverter mmc = new MarshallingMessageConverter(marshaller);
		mmc.setTargetType(MessageType.TEXT);
		
		JmsTemplate jt = new JmsTemplate(connectionFactory);
		jt.setDefaultDestination(topic);
		jt.setPubSubDomain(true);
		jt.setMessageConverter(mmc);
		
		return jt;
	}
	
	@Primary
	@Bean(destroyMethod = "shutdownNow")
	public ExecutorService executor() {
		return Executors.newWorkStealingPool(8);
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}
