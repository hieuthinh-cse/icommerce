package vn.icommerce.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.util.ObjectMapperSupplier;

/**
 * @author tuyenpham
 */
public class HibernateAwareObjectMapper implements ObjectMapperSupplier {

  private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();

  public HibernateAwareObjectMapper() {
  }

  @Override
  public ObjectMapper get() {
    return objectMapper;
  }
}
