package ucoach.data.model;

import ucoach.data.dao.UcoachDataDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The HealthMeasure class represents recorded health measures of a User
 * 
 * @author anadaniel
 *
 */
@Entity
@NamedQueries({
  @NamedQuery(
    name = "HealthMeasure.findCurrentMeasuresForUser",
    query = "SELECT hm FROM HealthMeasure hm, HMType hmt WHERE hm.user.id = :uid AND hm.hmType.id = hmt.id GROUP BY hmt.id"
  ),
  @NamedQuery(
    name = "HealthMeasure.findHealthMeasuresFromUserByHMType",
    query = "SELECT hm FROM HealthMeasure hm, HMType hmt WHERE hm.user.id = :uid AND hm.hmType.id = hmt.id AND hmt.id = :hmtid ORDER BY hm.id DESC"
  )
})
@Table(name="health_measure") 
public class HealthMeasure implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator="mysql_health_measure")
  @TableGenerator(name="mysql_health_measure", table="mysql_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="health_measure")
  @Column(name="id")
  private int id;
  @Column(name="value")
  private Float value;
  @Temporal(TemporalType.DATE)
  @Column(name="date")
  private Date date;

  @ManyToOne
  @JoinColumn(name="hm_type_id",referencedColumnName="id")
  private HMType hmType;

  @ManyToOne
  @JoinColumn(name="user_id", referencedColumnName="id")
  private User user;
  
  // Getters
  public int getId(){
    return id;
  }
  public Float getValue(){
    return value;
  }
  public Date getDate(){
    return date;
  }
  @XmlTransient
  public User getUser(){
    return user;
  }
  public HMType getHmType(){
    return hmType;
  }
  
  // Setters
  public void setId(int id){
    this.id = id;
  }
  public void setValue(Float value){
    this.value = value;
  }
  public void setDate(Date date){
    this.date = date;
  }
  public void setUser(User user){
    this.user = user;
  }
  public void setHmType(HMType hmType){
    this.hmType = hmType;
  }

  /**
   * Gets the last recorded measure of every HMType for a given User.
   * 
   * @param userId   The id of the user.
   * @return           A list of the current HealthMeasures of a User
   */
  public static List<HealthMeasure> getCurrentMeasuresForUser(int userId) {
    EntityManager em = UcoachDataDao.instance.createEntityManager();
    List<HealthMeasure> list = em.createNamedQuery("HealthMeasure.findCurrentMeasuresForUser")
      .setParameter("uid", userId)
      .getResultList();
    UcoachDataDao.instance.closeConnections(em);
    return list;
  }

  /**
   * Gets the Health Measures for a given User and a given HM Type.
   * 
   * @param userId    The id of the user.
   * @param hmTypeId  The id of the MeasureType.
   * @return          A list of a User's Health Measures of a given HM Type.
   */
  public static List<HealthMeasure> getHealthMeasuresFromUserByHMType(int userId, int hmTypeId) {
    EntityManager em = UcoachDataDao.instance.createEntityManager();
    List<HealthMeasure> list = em.createNamedQuery("HealthMeasure.findHealthMeasuresFromUserByHMType")
      .setParameter("uid", userId)
      .setParameter("hmtid", hmTypeId)
      .getResultList();
    UcoachDataDao.instance.closeConnections(em);
    return list;
  }

  /**
   * Creates a Health Measure in the database.
   * 
   * @param healthMeasure   The Health Measure to be persisted in the database.
   * @param userId          The id of the user the Health Measure belongs to.
   * @param hmTypeId        The id of the HM Type of the Health Measure.
   * @return                The created Health Measure.
   */
  public static HealthMeasure createHealthMeasure(HealthMeasure healthMeasure, int userId, int hmTypeId) {
    healthMeasure.setUser(User.getUserById(userId));
    healthMeasure.setHmType(HMType.getHMTypeById(hmTypeId));
    if ( healthMeasure.getDate() == null ) healthMeasure.setDate(new Date());

    EntityManager em = UcoachDataDao.instance.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(healthMeasure);
    tx.commit();
    UcoachDataDao.instance.closeConnections(em);
    return healthMeasure;
  }
}