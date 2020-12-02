/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.dedup;


import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The supported repository interface for {@link CmdProceeded} that is implemented automatically by
 * Spring.
 *
 * <p>Created on 9/06/19.
 *
 * @author vanlh
 */
public interface SpringCmdProceededRepository extends JpaRepository<CmdProceeded, String> {

  List<CmdProceeded> findTop1000ByCreatedAtBeforeOrderByCreatedAt(OffsetDateTime date);
}
