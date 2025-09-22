package co.com.bancolombia.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.bancolombia.r2dbc.model.SecretModel;
import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;

import java.time.Duration;

@Configuration
public class PostgreSQLConnectionPool {
    /* Change these values for your project */
    public static final int INITIAL_SIZE = 12;
    public static final int MAX_SIZE = 15;
    public static final int MAX_IDLE_TIME = 30;
    public static final int DEFAULT_PORT = 5432;
    
    private final GenericManagerAsync secretManager;
    private final String secretName;

    public PostgreSQLConnectionPool(GenericManagerAsync secretManager,
                                    @Value("${aws.secretName}") String secretName) {
        this.secretManager = secretManager;
        this.secretName = secretName;
    }
    
	@Bean
	public ConnectionPool getConnectionConfig(PostgresqlConnectionProperties properties) {
                SecretModel secrets;
                try {
                        secrets = this.secretManager.getSecret(secretName, SecretModel.class).block();
                        PostgresqlConnectionConfiguration dbConfiguration = PostgresqlConnectionConfiguration.builder()
                        .host(secrets.getDB_SOLICITUDES_HOST())
                        .port(properties.port())
                        .database(secrets.getDB_SOLICITUDES_NAME())
                        .schema(properties.schema())
                        .username(secrets.getDB_SOLICITUDES_USER())
                        .password(secrets.getDB_SOLICITUDES_PASS())
                        .sslMode(SSLMode.REQUIRE)
                        .build();

                ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder()
                        .connectionFactory(new PostgresqlConnectionFactory(dbConfiguration))
                        .name("api-postgres-connection-pool")
                        .initialSize(INITIAL_SIZE)
                        .maxSize(MAX_SIZE)
                        .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                        .validationQuery("SELECT 1")
                        .build();

                        return new ConnectionPool(poolConfiguration);
                } catch (SecretException e) {
                        throw new RuntimeException("Error obteniendo credenciales de AWS Secrets Manager", e);
                }
	}
}