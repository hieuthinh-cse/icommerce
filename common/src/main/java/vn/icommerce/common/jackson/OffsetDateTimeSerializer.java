

package vn.icommerce.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.OffsetDateTime;
import org.springframework.boot.jackson.JsonComponent;

/**
 * Custom serializer to serialize an {@link OffsetDateTime} object to the string representation of
 * number of milliseconds from the epoch.
 *
 *
 *
 *
 */
public class OffsetDateTimeSerializer extends StdSerializer<OffsetDateTime> {

  public OffsetDateTimeSerializer() {
    this(OffsetDateTime.class);
  }

  public OffsetDateTimeSerializer(Class<OffsetDateTime> t) {
    super(t);
  }

  @Override
  public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(String.valueOf(value.toInstant().toEpochMilli()));
  }
}
