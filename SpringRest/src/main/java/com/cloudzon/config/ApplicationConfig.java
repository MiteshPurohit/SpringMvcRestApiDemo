package com.cloudzon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource(value = { "classpath:database.properties",
		"classpath:hibernate.properties" })
@Configuration
public class ApplicationConfig {

	@Autowired
	private Environment environment;

	/* Database */
	public String getJDBCDriver() {
		return environment.getProperty("db.driver");
	}

	public String getJDBCURL() {
		return environment.getProperty("db.url");
	}

	public String getJDBCUserName() {
		return environment.getProperty("db.username");
	}

	public String getJDBCPassword() {
		return environment.getProperty("db.password");
	}

	/* Hibernate */
	public String getHDialect() {
		return environment.getProperty("hibernate.dialect");
	}

	public String getHShowSQL() {
		return environment.getProperty("hibernate.show_sql");
	}

	public String getHHBM2DDL() {
		return environment.getProperty("hibernate.hbm2ddl.auto");
	}

	public String getHFlushOperation() {
		return environment
				.getProperty("hibernate.transaction.flushbeforecompletion");
	}

	public String getHPackageToScan() {
		return environment.getProperty("hibernate.packages.to.scan");
	}
}
