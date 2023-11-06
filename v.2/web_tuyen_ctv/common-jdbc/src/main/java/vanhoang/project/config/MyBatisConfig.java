package vanhoang.project.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("vanhoang.project.entity");
        try {
            sqlSessionFactoryBean.setMapperLocations(
                    new PathMatchingResourcePatternResolver().getResources("classpath*:/mappers/*.xml")
            );
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            log.error("====> Connect database failed: {}", e.getMessage(), e);
            return null;
        }
    }
}
