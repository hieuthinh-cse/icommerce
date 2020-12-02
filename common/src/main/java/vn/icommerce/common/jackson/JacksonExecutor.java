/*
 * Copyright 2019 Sendo company. All Rights Reserved.
 *
 * This software is the proprietary information of Sendo company. Use is subject to license terms.
 */

package vn.icommerce.common.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Component;

/**
 * The serializer using Jackson library to perform business.
 *
 * <p>Created on 12/11/19.
 *
 * @author khoanguyenminh
 */
@Component
public class JacksonExecutor {

  private final ObjectMapper objectMapper;

  public JacksonExecutor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String serializeAsString(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public byte[] serializeAsBytes(Object obj) {
    try {
      return objectMapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void serializeToStream(OutputStream outputStream, Object obj) {
    try {
      objectMapper.writeValue(outputStream, obj);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void serializeToFile(File file, Object obj) {
    try {
      objectMapper.writeValue(file, obj);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserializeFromString(String object, Class<T> type) {
    try {
      return objectMapper.readValue(object, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserializeFromString(String object, TypeReference<T> valueTypeRef) {
    try {
      return objectMapper.readValue(object, valueTypeRef);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserializeFromFile(File file, TypeReference<T> typeRef) {
    try {
      return objectMapper.readValue(file, typeRef);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T deserializeFromBytes(byte[] bytes, Class<T> type) {
    try {
      return objectMapper.readValue(bytes, type);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
