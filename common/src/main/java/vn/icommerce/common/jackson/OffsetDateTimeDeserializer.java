

package vn.icommerce.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Custom deserializer to deserialize a string representation of number of milliseconds from the
 * epoch to an {@link OffsetDateTime} object.
 *
 *
 *
 *
 */
public class OffsetDateTimeDeserializer extends StdDeserializer<OffsetDateTime> {

  public OffsetDateTimeDeserializer() {
    this(OffsetDateTime.class);
  }

  public OffsetDateTimeDeserializer(Class<OffsetDateTime> t) {
    super(t);
  }

  @Override
  public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    var msFromEpoch = Long.parseLong(p.getValueAsString());

    return Instant.ofEpochMilli(msFromEpoch).atOffset(ZoneOffset.UTC);
  }
}
