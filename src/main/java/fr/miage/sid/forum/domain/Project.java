package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.exception.PermissionTopicException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
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
    
    private boolean anonymeAccess;

    @ManyToMany
    private Set<User> readerUser = new HashSet<>();
    @ManyToMany
    private Set<User> writerUser = new HashSet<>();

//    @OneToMany(mappedBy = "project")
//    private Set<Topic> topics= new HashSet<>();
    /**
     * addDroit - set permission for a user to the project
     *
     * @param u User, user who received permission
     * @param d EDroit, the permission apply to the user
     */
    public void addDroit(User u, EDroit d) {
        switch (d) {
            case READ:
                if (!readerUser.contains(u)) {
                    readerUser.add(u);
                }
                ;
                break;
            case WRITE:
                if (!writerUser.contains(u)) {
                    writerUser.add(u);
                }
                ;
                break;
            case ALL:
                if (!readerUser.contains(u)) {
                    readerUser.add(u);
                }
                if (!writerUser.contains(u)) {
                    writerUser.add(u);
                }
                ;
                break;
        }
    }

    /**
     * removeDroit - remove permission for a user to the project
     *
     * @param u User, user who has the permission removed
     * @param d EDroit, the permission apply to the user
     */
    public void removeDroit(User u, EDroit d) {
        switch (d) {
            case READ:
                if (readerUser.contains(u)) {
                    readerUser.remove(u);
                }
                ;
                break;
            case WRITE:
                if (writerUser.contains(u)) {
                    writerUser.remove(u);
                }
                ;
                break;
            case ALL:
                if (readerUser.contains(u)) {
                    readerUser.remove(u);
                }
                if (writerUser.contains(u)) {
                    writerUser.remove(u);
                }
                ;
                break;
        }
    }

    /**
     * canExecute - verify if user can execute an action
     *
     * @param u User, user who has verify
     * @param d EDroit, the permisssion verify
     * @return true if user can use the permission to the projet, false if it
     * can't
     */
    public boolean canExecute(User u, EDroit d) {
        if(u == null&& anonymeAccess&& d == EDroit.READ){
            return true;
        }
        switch (d) {
            case READ:
                if (readerUser.contains(u)||this.anonymeAccess) {
                    return true;
                }
                ;
                break;
            case WRITE:
                if (writerUser.contains(u)) {
                    return true;
                }
                ;
                break;
            case ALL:
                if (readerUser.contains(u) && writerUser.contains(u)) {
                    return true;
                }
                ;
                break;
        }
        return false;
    }

    /**
     * addTopic, set a topic to the project
     *
     * @param topic Topic, topic set
     * @throws PermissionTopicException if the user can't insert the topic
     */
    public void addTopic(Topic topic) throws PermissionTopicException {
        if (this.canExecute(topic.getCreator(), EDroit.WRITE)) {
            topic.setProject(this);
//            this.topics.add(topic);
        } else {
            throw new PermissionTopicException("User can't create topic");
        }
    }

}
