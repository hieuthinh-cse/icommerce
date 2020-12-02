/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Custom deserializer to deserialize a string representation of number to a {@link
 * BigDecimal} object.
 *
 * <p>Created on 10/20/19.
 *
 * @author khoanguyenminh
 */
public class BigDecimalDeserializer extends StdDeserializer<BigDecimal> {

  public BigDecimalDeserializer() {
    this(BigDecimal.class);
  }

  public BigDecimalDeserializer(Class<BigDecimal> t) {
    super(t);
  }

  @Override
  public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    return new BigDecimal(p.getValueAsString()).setScale(0, RoundingMode.DOWN);
  }
}
