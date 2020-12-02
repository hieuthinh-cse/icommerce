package vn.icommerce.common.outbox;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
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
import org.hibernate.annotations.Type;

/**
 * This entity represents a outbox event
 *
 * <p>Created on 9/6/19.
 *
 * @author vanlh
 */
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(of = "eventId")
@ToString
@Entity
@Table(name = "outbox")
public class Outbox {

  @Id
  @Column(name = "event_id", nullable = false, updatable = false)
  @Setter(AccessLevel.NONE)
  private UUID eventId = UUID.randomUUID();

  @Column(name = "payload", nullable = false)
  private String payload;

  @Column(name = "topic", nullable = false)
  private String topic;

  @Type(type = "jsonb")
  @Column(name = "trace_context", columnDefinition = "jsonb")
  private Map<String, String> traceContext;

  @CreationTimestamp
  @Setter(AccessLevel.NONE)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;
}
