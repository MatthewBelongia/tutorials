package com.baeldung.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.baeldung.persistence",
entityManagerFactoryRef = "entityManager",
transactionManagerRef = "transactionManager")
public class SqlServerDatabaseConfig extends DatabaseConfig{

	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManager(Environment env) {
		Map<String,Object> hibernateMap = new HashMap<>();
		hibernateMap.put("hmb2ddl.auto", env.getProperty("spring.jpa.properties.hibernate.hmb2ddl.auto"));
		hibernateMap.put("dialect", env.getProperty("spring.jpa.properties.hibernate.dialect.sqlserver"));
		return super.entityManager(sqlDataSource(), hibernateMap, "com.baeldung.persistence");
		
	}
	
	@Bean(name = "sqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.sql-datasource")
	public DataSource sqlDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "sqlSimpleJdbc")
	@ConfigurationProperties(prefix = "spring.datasource.sql-datasource")
	public SimpleJdbcCall sqlSimpleJdbc() {
		return new SimpleJdbcCall(sqlDataSource()).withFunctionName("test_Function");
	}
	
	@Bean
	public PlatformTransactionManager sqlTransactionManager(Environment env) {
		return super.transactionManager(entityManager(env));
	}
	
}
