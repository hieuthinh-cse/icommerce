/*
 * Copyright 2020 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.sharedkernel.domain.model;

import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * This entity represents a token used to access API.
 *
 * <p>Created on 8/26/19.
 *
 * @author khoanguyenminh
 */
@Accessors(chain = true)
@Setter
@Getter
@ToString
public class OptToken {

  private String email;

  private String role;

  private Set<String> privilegeSet;

  private OffsetDateTime expiredAt;
}
