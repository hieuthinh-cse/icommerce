/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

/**
 * Marker package to define custom Hibernate type.
 *
 * <p>Created on 9/16/19.
 *
 * @author khoanguyenminh
 */
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
package vn.icommerce.common.outbox;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;