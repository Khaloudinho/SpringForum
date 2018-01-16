package fr.miage.sid.forum.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class HasPermissions extends Auditable {

  private boolean anonymousCanAccess = true;

  @ElementCollection
  private Set<Long> readers = new HashSet<>();

  @ElementCollection
  private Set<Long> writers = new HashSet<>();

  public void givePermissionTo(Long userId, Permission permission) {
    if (permission == Permission.READ || permission == Permission.ALL) {
      readers.add(userId);
    }

    if (permission == Permission.WRITE || permission == Permission.ALL) {
      writers.add(userId);
    }
  }

  public void givePermissionToAll(Set<Long> userIds, Permission permission) {
    for (Long user : userIds) {
      givePermissionTo(user, permission);
    }
  }

  public void removePermissionOf(Long userId, Permission permission) {
    if (permission == Permission.READ || permission == Permission.ALL) {
      readers.remove(userId);
    }

    if (permission == Permission.WRITE || permission == Permission.ALL) {
      writers.remove(userId);
    }
  }

  public void removePermissionOfAll(Set<Long> userIds, Permission permission) {
    for (Long user : userIds) {
      removePermissionOf(user, permission);
    }
  }

  /**
   * An user can write an entity with Permissions if :
   * he is in writers or writers is empty
   */
  public boolean canWrite(Long userId) {
    return writers.isEmpty() || writers.contains(userId);
  }

  /**
   * An user can read an entity with Permissions if :
   * he is anonymous and anonymousCanAccess is true or he is in readers or readers is empty
   */
  public boolean canRead(Long userId) {
    return (userId == null && anonymousCanAccess) || readers.isEmpty() || readers.contains(userId);
  }

  public boolean hasPermission(Long userId, Permission permission) {
    boolean canRead = canRead(userId);
    boolean canWrite = canWrite(userId);

    if (permission == Permission.READ) {
      return canRead;
    }

    if (permission == Permission.WRITE) {
      return canWrite;
    }

    return permission == Permission.ALL && canRead && canWrite;
  }

}
