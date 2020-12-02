/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.dbtool.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.icommerce.sharedkernel.domain.model.Buyer;
import vn.icommerce.sharedkernel.domain.model.ShoppingCart;

/**
 * The supported repository interface for {@link Buyer} that is implemented automatically by
 * Spring.
 *
 * <p>Created on 8/20/19.
 *
 * @author khoanguyenminh
 */
public interface SpringShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

  Slice<ShoppingCartShoppingCartId> findByShoppingCartIdNotNull(Pageable pageable);

  List<ShoppingCart> findByShoppingCartIdIn(List<Long> shoppingCartIdList);
}
