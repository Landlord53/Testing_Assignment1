package ucoach.data.model;

import ucoach.data.dao.UcoachDataDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The User class represents a user that will record health measures.
 * 
 * @author anadaniel
 *
 */
@Entity
@Table(name="user") 
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator="mysql_user")
  @TableGenerator(name="mysql_user", table="mysql_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="user")
  @Column(name="id")
  private int id;
  @Column(name="lastname")
  private String lastname;
  @Column(name="firstname")
  private String firstname;
  @Temporal(TemporalType.DATE)
  @Column(name="birthdate")
  private Date birthdate;
  
  
  // Getters
  public int getId(){
    return id;
  }
  public String getLastname(){
    return lastname;
  }
  public String getFirstname(){
    return firstname;
  }
  public Date getBirthdate(){
    return birthdate;
  }
  
  // Setters
  public void setId(int id){
    this.id = id;
  }
  public void setLastname(String lastname){
    this.lastname = lastname;
  }
  public void setFirstname(String firstname){
    this.firstname = firstname;
  }
  public void setBirthdate(Date birthdate){
    this.birthdate = birthdate;
  }

  /**
   * Returns every user in the database.
   * 
   * @return  A list of Users
   */
  public static List<User> getAll() {
    EntityManager em = UcoachDataDao.instance.createEntityManager();
    List<User> list = em.createNamedQuery("User.findAll").getResultList();
    UcoachDataDao.instance.closeConnections(em);
    return list;
  }

  /**
   * Finds a User in the database given its id.
   * @param id    The id of the user
   * @return      The found user
   */
  public static User getUserById(int id) {
    EntityManager em = UcoachDataDao.instance.createEntityManager();
    User user = em.find(User.class, id);
    if(user != null)
      em.refresh(user);
    UcoachDataDao.instance.closeConnections(em);
    return user;
  }
  
  /**
   * Creates a user in the database.
   * 
   * @param user  The user to be persisted in the database
   * @return        The saved user
   */
  public static User createUser(User user) {
    EntityManager em = UcoachDataDao.instance.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    em.persist(user);
    tx.commit();
    UcoachDataDao.instance.closeConnections(em);
    return user;
  }
  
  /**
   * This method is used by the updateUser method to sync a user before updating
   * it on the database. When updating only specific fields, other fields will be set to
   * null, this method makes sure that the old values of the attributes that will not be 
   * updated remain in the database.
   * 
   * @param oldUser       The user that was retrieved from the database. It contains 
   *                      the old information of a user.        
   * @param updatedUser   The user containing only the attributes that will be updated.
   * @return              A user with updated information but also keeping its old attributes
   */
  public static User syncUser(User oldUser, User updatedUser) {
    updatedUser.setId(oldUser.getId());

    if (updatedUser.getFirstname() == null)
      updatedUser.setFirstname(oldUser.getFirstname());

    if (updatedUser.getLastname() == null)
      updatedUser.setLastname(oldUser.getLastname());

    if (updatedUser.getBirthdate() == null)
      updatedUser.setBirthdate(oldUser.getBirthdate());

    return updatedUser;
  }

  /**
   * Updates a User in the database. It makes sure to NOT override any data that 
   * is not being updated.
   * 
   * @param oldUser       The user with the currently saved data.
   * @param updatedUser   The user with the updated data that will be saved.
   * @return              The updated user
   */
  public static User updateUser(User oldUser, User updatedUser) {
    updatedUser = syncUser(oldUser, updatedUser);

    EntityManager em = UcoachDataDao.instance.createEntityManager(); 
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    updatedUser = em.merge(updatedUser);
    tx.commit();
    UcoachDataDao.instance.closeConnections(em);
    return updatedUser;
  }

  /**
   * Deletes a User from the database.
   * 
   * @param user  The user that will be deleted.
   */
  public static void deleteUser(User user) {
    EntityManager em = UcoachDataDao.instance.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    user = em.merge(user);
    em.remove(user);
    tx.commit();
    UcoachDataDao.instance.closeConnections(em);
  }
}