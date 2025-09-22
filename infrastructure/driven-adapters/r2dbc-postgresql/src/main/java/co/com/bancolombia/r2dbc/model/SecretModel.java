package co.com.bancolombia.r2dbc.model;

import lombok.Data;

@Data
public class SecretModel {
  private String DB_SOLICITUDES_NAME;
  private String DB_SOLICITUDES_USER;
  private String DB_SOLICITUDES_PASS;
  private String DB_SOLICITUDES_HOST;
}
