

/**
 * Marker package to define custom Hibernate type.
 *
 *
 *
 *
 */
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
package vn.icommerce.common.outbox;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;