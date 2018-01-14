package fr.miage.sid.forum.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
abstract class HasPermissions extends Auditable {

  private boolean anonymousCanAccess = true;

  @ManyToMany
  private Set<User> readers = new HashSet<>();

  @ManyToMany
  private Set<User> writers = new HashSet<>();

  public void givePermission(User user, Permission permission) {
    if (permission == Permission.READ || permission == Permission.ALL) {
      readers.add(user);
    }

    if (permission == Permission.WRITE || permission == Permission.ALL) {
      writers.add(user);
    }
  }

  public void removePermission(User user, Permission permission) {
    if (permission == Permission.READ || permission == Permission.ALL) {
      readers.remove(user);
    }

    if (permission == Permission.WRITE || permission == Permission.ALL) {
      writers.remove(user);
    }
  }


  /**
   * An user can write an entity with Permissions if :
   * he is in writers or writers is empty
   */
  public boolean canWrite(User user) {
    return writers.isEmpty() || writers.contains(user);
  }

  /**
   * An user can read an entity with Permissions if :
   * he is anonymous and anonymousCanAccess is true or he is in readers or readers is empty
   */
  public boolean canRead(User user) {
    return (user == null && anonymousCanAccess) || readers.isEmpty() || readers.contains(user);
  }

  public boolean hasPermission(User user, Permission permission) {
    boolean canRead = canRead(user);
    boolean canWrite = canWrite(user);

    if (permission == Permission.READ) {
      return canRead;
    }

    if (permission == Permission.WRITE) {
      return canWrite;
    }

    return permission == Permission.ALL && canRead && canWrite;
  }

}
