/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.infra.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Product;

/**
 * The supported repository interface for {@link Product} that is implemented automatically by
 * Spring.
 */
public interface SpringProductRepository extends JpaRepository<Product, Long> {

}
