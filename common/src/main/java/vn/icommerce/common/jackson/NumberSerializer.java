/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

/**
 * Custom serializer to serialize a {@link Number} object to its string representation.
 *
 * <p>Created on 10/20/19.
 *
 * @author khoanguyenminh
 */
public class NumberSerializer extends StdSerializer<Number> {

  public NumberSerializer() {
    this(Number.class);
  }

  public NumberSerializer(Class<Number> t) {
    super(t);
  }

  @Override
  public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(value.toString());
  }
}
