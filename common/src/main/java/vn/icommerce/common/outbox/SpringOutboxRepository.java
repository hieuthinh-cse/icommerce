/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.outbox;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The supported repository interface for {@link Outbox} that is implemented automatically by
 * Spring.
 *
 * <p>Created on 9/06/19.
 *
 * @author vanlh
 */
public interface SpringOutboxRepository extends JpaRepository<Outbox, UUID> {

  List<Outbox> findTop1000ByOrderByCreatedAt();
}
