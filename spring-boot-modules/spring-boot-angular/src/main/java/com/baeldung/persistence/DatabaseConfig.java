package com.baeldung.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

public class DatabaseConfig {

	public LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource, Map<String, Object> hibernateMap,
			String... packagesToScan) {
		LocalContainerEntityManagerFactoryBean manager = new LocalContainerEntityManagerFactoryBean();
		manager.setDataSource(dataSource);
		manager.setPackagesToScan(packagesToScan);
		manager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
		Map<String,Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", hibernateMap.get("hbm2ddl.auto"));
		properties.put("hibernate.dialect", hibernateMap.get("dialect"));
		
		manager.setJpaPropertyMap(properties);
		return manager;
		
	}
	
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManager) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManager.getObject());
		
		return transactionManager;
	}

}
