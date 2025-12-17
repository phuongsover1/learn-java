package com.manning.javapersistence.springdatajpa.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.Properties;

@EnableJpaRepositories("com.manning.javapersistence.springdatajpa.repositories")
public class SpringDataConfiguration {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl(
        "jdbc:mysql://localhost:3306/JAVA_PERSISTENCE_WITH_SPRING_DATA_AND_HIBERNATE?serverTimezone=UTC");
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    return dataSource;
  }

  @Bean
  public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setDatabase(Database.MYSQL);
    jpaVendorAdapter.setShowSql(true);
    return jpaVendorAdapter;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    localContainerEntityManagerFactoryBean.setDataSource(dataSource());
    Properties properties = new Properties();
    properties.put("hibernate.hbm2ddl.auto", "create");
    localContainerEntityManagerFactoryBean.setJpaProperties(properties);
    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    localContainerEntityManagerFactoryBean.setPackagesToScan("com.manning.javapersistence.springdatajpa");
    return localContainerEntityManagerFactoryBean;
  }
}
