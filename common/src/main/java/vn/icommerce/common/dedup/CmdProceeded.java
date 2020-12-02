package vn.icommerce.common.dedup;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

/**
 * This entity represents a consumed event
 *
 * <p>Created on 9/6/19.
 *
 * @author vanlh
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "cmdProceededId")
@ToString
@Entity
@Table(name = "cmd_proceeded")
public class CmdProceeded {

  @Id
  @Column(name = "cmd_proceeded_id", nullable = false, updatable = false)
  private String cmdProceededId;

  @CreationTimestamp
  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;
}
