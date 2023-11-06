package com.os.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MybatisConfig implements TransactionManagementConfigurer {

    @Resource
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(){
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:/mybatis/**/*Mapper.xml"));
            return bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("SqlSessionFactory inti fail",e);
        }
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory factory){
        return new SqlSessionTemplate(factory);
    }


    @Bean(name = "transactionManager")
    @Override
    public TransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
