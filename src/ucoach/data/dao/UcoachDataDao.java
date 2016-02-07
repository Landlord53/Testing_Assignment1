package ucoach.data.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

public enum UcoachDataDao {
  instance;
  private EntityManagerFactory emf;

  private Map<String, String> getDBProperties(){
    String databaseUrl = System.getenv("CLEARDB_DATABASE_URL");
    Map<String, String> properties = new HashMap<String, String>();

    if( databaseUrl != null ) {
      StringTokenizer st = new StringTokenizer(databaseUrl, ":@/");
      String dbVendor = st.nextToken();
      String username = st.nextToken();
      String password = st.nextToken();
      String host = st.nextToken();
      String databaseName = st.nextToken();
      String jdbcUrl = String.format("jdbc:%s://%s:3306/%s?reconnect=true", dbVendor, host, databaseName);
      properties.put("hibernate.connection.url", jdbcUrl);
      properties.put("hibernate.connection.username", username);
      properties.put("hibernate.connection.password", password);
      properties.put("hibernate.connection.password", password);
      properties.put("hibernate.connection.pool_size", "8");
      properties.put("hibernate.connection.max_active", "10");
      properties.put("hibernate.connection.max_idle", "5");
      properties.put("hibernate.connection.min_idle", "2");
      properties.put("hibernate.connection.remove_abandoned", "true");
      properties.put("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
      properties.put("hibernate.c3p0.min_size", "1");
      properties.put("hibernate.c3p0.max_size", "5");
      properties.put("hibernate.c3p0.timeout", "300");
      properties.put("hibernate.c3p0.idle_test_period", "3000");

    } else {
      properties.put("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/ucoach_data_service");
      properties.put("hibernate.connection.username", "root");
      properties.put("hibernate.connection.password", "root");
      properties.put("hibernate.connection.pool_size", "8");
      properties.put("hibernate.connection.max_active", "10");
      properties.put("hibernate.connection.max_idle", "5");
      properties.put("hibernate.connection.min_idle", "2");
      properties.put("hibernate.connection.remove_abandoned", "true");
      properties.put("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
      properties.put("hibernate.c3p0.min_size", "1");
      properties.put("hibernate.c3p0.max_size", "5");
      properties.put("hibernate.c3p0.timeout", "300");
      properties.put("hibernate.c3p0.idle_test_period", "3000");
    }

    return properties;
  }

  private UcoachDataDao() {
    if (emf!=null) {
      emf.close();
    }
    
    try {
      emf = Persistence.createEntityManagerFactory("ucoach-data-jpa", getDBProperties());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public EntityManager createEntityManager() {
    try {
      return emf.createEntityManager();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;    
  }

  public void closeConnections(EntityManager em) {
    em.close();
  }

  public EntityTransaction getTransaction(EntityManager em) {
    return em.getTransaction();
  }

  public EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }  
}