package cn.tedu.fitnessClub.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.tedu.fitnessClub.mapper")
public class MybatisConfiguration {
}
