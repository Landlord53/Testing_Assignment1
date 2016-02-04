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
@NamedQuery(
  name = "HealthMeasure.findCurrentMeasuresForUser",
  query = "SELECT hm FROM HealthMeasure hm, HMType hmt WHERE hm.user.id = :uid AND hm.hmType.id = hmt.id GROUP BY hmt.id"
)
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
}