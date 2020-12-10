package vn.icommerce.sharedkernel.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import vn.icommerce.sharedkernel.app.generator.IdGenerator;

/**
 * This entity represents an buyer.
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "buyerId")
//@ToString(exclude = "password")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "buyer")
public class Buyer {

  @Id
  @Column(name = "buyer_id", updatable = false, nullable = false)
  @Setter(AccessLevel.NONE)
  private Long buyerId = Long.sum(300_000_000L, IdGenerator.generate(0L, 99_999_999L));

  @Column(name = "social_id")
  private String socialId;

  @Enumerated(EnumType.STRING)
  @Column(name = "social_platform")
  private SocialPlatform platform;

  @Column(name = "email", nullable = false, updatable = false, unique = true)
  private String email;

  @Column(name = "password", length = 60)
  @JsonIgnore
  private String password;

  @Column(name = "buyer_name", nullable = false)
  private String accountName;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private BuyerStatus status;

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

  public boolean active() {
    return status == BuyerStatus.ACTIVE;
  }
}
