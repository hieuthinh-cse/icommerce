/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.infra.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;
import vn.icommerce.sharedkernel.domain.model.ShoppingCartStatus;

/**
 * The supported repository interface for {@link ShoppingCart} that is implemented automatically by
 * Spring.
 */
public interface SpringShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

  Optional<ShoppingCart> findByBuyerIdAndStatus(Long buyerID, ShoppingCartStatus status);
}
