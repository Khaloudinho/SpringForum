package fr.miage.sid.forum.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
abstract class Auditable implements Serializable {

  @CreatedBy
  @ManyToOne
  private User createdBy;

  @LastModifiedBy
  @ManyToOne
  private User modifiedBy;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
