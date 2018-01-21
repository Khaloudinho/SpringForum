package fr.miage.sid.forum.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class HasPermissions extends Auditable {

  protected boolean anonymousCanAccess = true;

  @ElementCollection
  private Set<Long> readers = new HashSet<>();

  @ElementCollection
  private Set<Long> writers = new HashSet<>();


  /**
   * We use the returned Map to avoid errors in the frontend.
   * For example, we don't add an user to the reader table if he already had the permission.
   */
  public Map<Permission, Boolean> givePermissionTo(Long userId, Permission permission) {
    Map<Permission, Boolean> result = new HashMap<>();
    if (permission == Permission.READ || permission == Permission.ALL) {
      result.put(Permission.READ, readers.add(userId));
    }

    if (permission == Permission.WRITE || permission == Permission.ALL) {
      result.put(Permission.WRITE, writers.add(userId));
    }

    return result;
  }

  public Map<Permission, Boolean> removePermissionOf(Long userId, Permission permission) {
    Map<Permission, Boolean> result = new HashMap<>();
    if (permission == Permission.READ || permission == Permission.ALL) {
      result.put(Permission.READ, readers.remove(userId));
    }

    if (permission == Permission.WRITE || permission == Permission.ALL) {
      result.put(Permission.WRITE, writers.remove(userId));
    }

    return result;
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
   * first check reads as : if your are connected or anonymousCanAccess
   * Of course if readers is not empty anonymous can't acess because it's stupid to restrict users
   * but not anonymous.
   */
  public boolean canRead(Long userId) {
    return (userId != null || isAnonymousCanAccess()) && (readers.isEmpty() || readers
        .contains(userId));
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
