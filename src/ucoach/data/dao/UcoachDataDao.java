package ucoach.data.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public enum UcoachDataDao {
  instance;
  private EntityManagerFactory emf;

  private UcoachDataDao() {
    if (emf!=null) {
      emf.close();
    }
    
    try {
      emf = Persistence.createEntityManagerFactory("ucoach-data-jpa");
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