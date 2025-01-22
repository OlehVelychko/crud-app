package ua.com.alicecompany.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import ua.com.alicecompany.ConfigFlattener;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan("ua.com.alicecompany")
@EnableWebMvc
@EnableTransactionManagement
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public Map<String, String> hibernateProperties() throws Exception {
        // Load `hibernate.yaml` from classpath
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Map<String, Object> yamlConfig = yamlMapper.readValue(
                getClass().getClassLoader().getResourceAsStream("hibernate.yaml"),
                Map.class
        );

        // Flatten and return properties
        return ConfigFlattener.flattenConfig((Map<String, Object>) yamlConfig.get("hibernate"), "hibernate");
    }

    @Bean
    public DataSource dataSource() throws Exception {
        Map<String, String> properties = hibernateProperties();

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.get("hibernate.connection.driver_class"));
        dataSource.setUrl(properties.get("hibernate.connection.url"));
        dataSource.setUsername(properties.get("hibernate.connection.username"));
        dataSource.setPassword(properties.get("hibernate.connection.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws Exception {
        Map<String, String> properties = hibernateProperties();

        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("ua.com.alicecompany.models");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        Properties hibernateProps = new Properties();
        hibernateProps.put("hibernate.dialect", properties.get("hibernate.dialect"));
        hibernateProps.put("hibernate.show_sql", properties.get("hibernate.show_sql"));
        hibernateProps.put("hibernate.hbm2ddl.auto", properties.get("hibernate.hbm2ddl.auto"));
        hibernateProps.put("hibernate.format_sql", properties.get("hibernate.format_sql"));

        entityManagerFactory.setJpaProperties(hibernateProps);

        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}