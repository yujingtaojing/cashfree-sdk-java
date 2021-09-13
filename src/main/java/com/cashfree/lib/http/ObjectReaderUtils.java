package com.cashfree.lib.http;

import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Field;
import java.util.function.Function;

import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonObject;

import com.cashfree.lib.annotations.Deserialize;
import com.cashfree.lib.exceptions.JsonParseException;
import com.cashfree.lib.serializers.JsonFieldDeserializer;

/**
 * Helps in reading objects from string.
 */
public class ObjectReaderUtils {
  private static Map<String, Function<String, Object>> classTypeToValueExtractorMapping;
  static {
    classTypeToValueExtractorMapping = new HashMap<>();

    classTypeToValueExtractorMapping.put("java.lang.Boolean", Boolean::valueOf);
    classTypeToValueExtractorMapping.put("java.lang.String", String::valueOf);

    classTypeToValueExtractorMapping.put("java.lang.Short", Short::valueOf);
    classTypeToValueExtractorMapping.put("java.lang.Integer", Integer::valueOf);
    classTypeToValueExtractorMapping.put("java.lang.Long", Long::valueOf);
    classTypeToValueExtractorMapping.put("java.lang.Float", Float::valueOf);
    classTypeToValueExtractorMapping.put("java.lang.Double", Double::valueOf);
    classTypeToValueExtractorMapping.put("java.math.BigInteger", BigInteger::new);
    classTypeToValueExtractorMapping.put("java.math.BigDecimal", BigDecimal::new);
  }

  public static <T> T readValue(JsonValue jsonValue, Class<T> clazz) {
    if (jsonValue == null || jsonValue.isNull()) return null;

    // As we're mapping a jsonValue to a class it should be an object type.
    if (!jsonValue.isObject()) {
      throw new JsonParseException("Expected jsonValue " + jsonValue + " can't be mapped to any classes/objects.");
    }
    return readValue(jsonValue.asObject(), clazz);
  }

  private static <T> T readValue(JsonObject jsonObject, Class<T> clazz) {
    Object instance = ObjectMappingUtils.getInstanceOfClass(clazz);
    for (Field field: ObjectMappingUtils.getAllFields(clazz)) {
      String fieldName = field.getName();
      Class<?> fieldClass = field.getType();
      JsonValue jsonValue = jsonObject.get(fieldName);
      if (jsonValue == null || jsonValue.isNull()) continue;
      setField(field, instance, getFieldInstance(jsonValue, fieldClass, field));
    }
    return clazz.cast(instance);
  }

  public static Object getFieldInstance(JsonValue jsonValue, Class<?> fieldClass, Field field) {
    ObjectMappingUtils.JsonValueType jsonValueType = getJsonValueType(jsonValue);
    // At this stage jsonValueType can be either base values(boolean, number and string) or object & array.
    Object fieldValue = null;
    String fieldClassName = fieldClass.getName();
    String jsonValueAsString = jsonValue.toString();
    if (classTypeToValueExtractorMapping.containsKey(fieldClassName)) {
      jsonValueAsString = jsonValueAsString.replaceAll("\"", "");
      fieldValue = getBaseTypeValue(fieldClassName, jsonValueAsString);
    } else if (ObjectMappingUtils.JsonValueType.OBJECT.equals(jsonValueType) ||
        ObjectMappingUtils.JsonValueType.ARRAY.equals(jsonValueType)) {
      Deserialize deserialize = field.getAnnotation(Deserialize.class);
      fieldValue = deserialize == null ?
          readValue(jsonValue, fieldClass) : getFieldInstanceUsingCustomDeserializer(deserialize, jsonValueAsString);
    }
    return fieldValue;
  }

  public static Object getFieldInstance(JsonValue jsonValue, Class<?> fieldClass) {
    ObjectMappingUtils.JsonValueType jsonValueType = getJsonValueType(jsonValue);
    // At this stage jsonValueType can be either base values(boolean, number and string) or object & array.
    Object fieldValue = null;
    String fieldClassName = fieldClass.getName();
    String jsonValueAsString = jsonValue.toString().replaceAll("\"", "");
    if (ObjectMappingUtils.jsonBaseTypes.contains(jsonValueType)) {
      fieldValue = getBaseTypeValue(fieldClassName, jsonValueAsString);
    } else if (ObjectMappingUtils.JsonValueType.OBJECT.equals(jsonValueType)) {
      fieldValue = readValue(jsonValue, fieldClass);
    }
    return fieldValue;
  }

  private static ObjectMappingUtils.JsonValueType getJsonValueType(JsonValue jsonValue) {
    if (jsonValue.isNull()) return ObjectMappingUtils.JsonValueType.NULL;
    if (jsonValue.isBoolean()) return ObjectMappingUtils.JsonValueType.BOOLEAN;
    if (jsonValue.isNumber()) return ObjectMappingUtils.JsonValueType.NUMBER;
    if (jsonValue.isString()) return ObjectMappingUtils.JsonValueType.STRING;
    if (jsonValue.isObject()) return ObjectMappingUtils.JsonValueType.OBJECT;
    if (jsonValue.isArray()) return ObjectMappingUtils.JsonValueType.ARRAY;
    return ObjectMappingUtils.JsonValueType.UNMAPPED;
  }

  private static Object getBaseTypeValue(String fieldClassName, String jsonValueAsString) {
    try {
      return classTypeToValueExtractorMapping.get(fieldClassName).apply(jsonValueAsString);
    } catch (Exception x) {
      throw new JsonParseException(x);
    }
  }

  @SuppressWarnings("rawtypes")
  private static Object getFieldInstanceUsingCustomDeserializer(Deserialize deserialize, String jsonValueAsString) {
    Class<? extends JsonFieldDeserializer> deserializerClass = deserialize.using();
    JsonFieldDeserializer jsonFieldDeserializer = ObjectMappingUtils.getTypedInstance(deserializerClass);

    return jsonFieldDeserializer.deserialize(jsonValueAsString);
  }

  @SuppressWarnings("deprecation")
  private static void setField(Field field, Object object, Object fieldObject) {
    boolean isFieldAccessible = field.isAccessible();
    if (!isFieldAccessible) {
      field.setAccessible(true);
      try {
        field.set(object, fieldObject);
      } catch (IllegalAccessException x) {
        throw new RuntimeException(x);
      }
      field.setAccessible(false);
    }
  }
}
