package pe.edu.upeu.ProyectLP2.infraestructure.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/cine?serverTimezone=America/Lima&useSSL=false&allowPublicKeyRetrieval=true")
                .username("root")
                .password("admin")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}