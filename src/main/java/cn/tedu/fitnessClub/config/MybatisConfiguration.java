package cn.tedu.fitnessClub.config;

import cn.tedu.fitnessClub.mybatis.InsertUpdateTimeInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Mybatis配置类
 *
 * @author java@tedu.cn
 * @version 0.0.1
 */
@Slf4j
@Configuration
@MapperScan("cn.tedu.fitnessClub.mapper")
public class MybatisConfiguration {

    public MybatisConfiguration() {
        log.debug("创建配置类对象：MybatisConfiguration");
    }

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactories;

    @PostConstruct // Spring Bean的生命周期方法，会在构造方法、自动装配之后自动执行
    public void addInterceptor() {
        Interceptor interceptor = new InsertUpdateTimeInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactories) {
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        }
    }

}