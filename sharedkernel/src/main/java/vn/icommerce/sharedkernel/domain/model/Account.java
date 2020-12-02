package vn.icommerce.sharedkernel.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This entity represents an account in Alliance.
 *
 * <p>Created on 8/28/19.
 *
 * @author khoanguyenminh
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "accountId")
@ToString(exclude = "password")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account")
public class Account {

  @Id
  @Column(name = "account_id", updatable = false, nullable = false)
  @Setter(AccessLevel.NONE)
  private UUID accountId = UUID.randomUUID();

  @Column(name = "email", nullable = false, updatable = false, unique = true)
  private String email;

  @Column(name = "password", length = 60, nullable = false)
  @JsonIgnore
  private String password;

  @Column(name = "account_name", nullable = false)
  private String accountName;

  @Column(name = "role_name", nullable = false)
  private String roleName;

  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private OffsetDateTime createdAt;

  @Setter(AccessLevel.NONE)
  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private OffsetDateTime updatedAt;

  @Column(name = "created_by", nullable = false, updatable = false)
  @CreatedBy
  private String createdBy;

  @Column(name = "updated_by", nullable = false)
  @LastModifiedBy
  private String updatedBy;
}
