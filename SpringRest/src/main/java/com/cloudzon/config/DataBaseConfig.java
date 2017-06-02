package com.cloudzon.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableJpaRepositories("com.cloudzon.repository")
@EnableTransactionManagement
public class DataBaseConfig {

	@Autowired
	private ApplicationConfig config;

	@Autowired
	private Environment env;

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(config.getJDBCDriver());
		dataSource.setJdbcUrl(config.getJDBCURL());
		dataSource.setUser(config.getJDBCUserName());
		dataSource.setPassword(config.getJDBCPassword());
		// <!-- these are C3P0 properties -->
		dataSource.setAcquireIncrement(5);
		// dataSource.setIdleConnectionTestPeriod(14400);
		dataSource.setMaxIdleTime(3600);
		dataSource.setMaxIdleTimeExcessConnections(300);
		dataSource.setMaxPoolSize(10000);
		dataSource.setMinPoolSize(5);
		dataSource.setNumHelperThreads(6);
		// dataSource.setMaxStatements(100);
		// dataSource.setMaxStatementsPerConnection(12);
		dataSource.setUnreturnedConnectionTimeout(3600);
		// <!-- Keep pool alive -->
		dataSource.setPreferredTestQuery("SELECT 1");
		dataSource.setTestConnectionOnCheckout(true);
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
			throws PropertyVetoException {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean
				.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		factoryBean.setPackagesToScan(config.getHPackageToScan());
		factoryBean.setJpaProperties(hibernateProp());
		factoryBean.setJpaDialect(hibernateJpaDialect());
		return factoryBean;

	}

	private Properties hibernateProp() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", config.getHDialect());
		properties.setProperty("hibernate.show_sql", config.getHShowSQL());
		properties.setProperty("hibernate.hbm2ddl.auto", config.getHHBM2DDL());
		return properties;
	}

	@Bean
	public CustomHibernateJpaDialect hibernateJpaDialect() {
		return new CustomHibernateJpaDialect();
	}

	@Bean
	public JpaTransactionManager transactionManager()
			throws PropertyVetoException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory()
				.getObject());
		return transactionManager;
	}
}
