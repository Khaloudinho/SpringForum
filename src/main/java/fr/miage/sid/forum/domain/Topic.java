package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.exception.PermissionPostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
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
public class Topic extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String title;

    @ManyToOne
    private User creator;

    
    @ManyToOne
    private Project project;

    @ManyToMany
    private Set<User> readerUser = new HashSet<>();
    @ManyToMany
    private Set<User> writerUser = new HashSet<>();
    
  
//    @OneToMany(mappedBy = "topic")
//    private Set<Post> posts = new HashSet<>();

    /**
     * addDroit - set permission for a user to the topic
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
     * @param u User, user who has verify
     * @param d EDroit, the permisssion verify
     * @return true if user can use the permission to the projet, false if it can't
     */
    public boolean canExecute(User u, EDroit d) {
        switch (d) {
            case READ:
                if (readerUser.contains(u)) {
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
     * addPost, set a post to the topic
     * @param post Post, post set
     * @throws PermissionPostException if the user can't insert the post 
     */
    public void addPost(Post post) throws PermissionPostException{
        if(this.canExecute(post.getUser(), EDroit.WRITE)){
//            this.posts.add(post);
            post.setTopic(this);
        }else{
            throw new PermissionPostException("User can't write post");
        }
    }

}
