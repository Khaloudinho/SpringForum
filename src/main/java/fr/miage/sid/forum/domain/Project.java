package fr.miage.sid.forum.domain;

import java.util.HashMap;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Project extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotEmpty
  private String name;
  
  @ManyToOne
  private User creator;
  
  /*  @ElementCollection
  private HashMap<String,String> droits;
  
   /***
   * Set permission if user doesn't exist, replace if exist
   * @param user User
   * @param droit user permission's
   */
  /*public void addDroit(User user, EDroit droit){
      if(droits.containsKey(user)){
          droits.replace(user.getEmail(), droit.name());
      }else{
          this.droits.put(user.getEmail(), droit.name());
      }     
  }*/

}
