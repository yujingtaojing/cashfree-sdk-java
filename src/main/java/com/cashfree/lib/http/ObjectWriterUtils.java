package com.cashfree.lib.http;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.lang.reflect.Field;

import lombok.SneakyThrows;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import com.cashfree.lib.annotations.Serialize;
import com.cashfree.lib.serializers.JsonFieldSerializer;

public class ObjectWriterUtils {
  private static Set<String> javaBasicTypes;
  static {
    javaBasicTypes = new HashSet<>();

    javaBasicTypes.add("java.lang.Boolean");
    javaBasicTypes.add("java.lang.String");

    javaBasicTypes.add("java.lang.Short");
    javaBasicTypes.add("java.lang.Integer");
    javaBasicTypes.add("java.lang.Long");
    javaBasicTypes.add("java.lang.Float");
    javaBasicTypes.add("java.lang.Double");
    javaBasicTypes.add("java.math.BigInteger");
    javaBasicTypes.add("java.math.BigDecimal");
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  public static <T> String writeValue(T t) {
    JsonObject jsonObject = new JsonObject();
    for (Field field: ObjectMappingUtils.getAllFields(t.getClass())) {
      Object fieldValue = getField(field, t);
      if (fieldValue == null) continue;

      String fieldValueAsString;
      String fieldName = field.getName();
      Class<?> fieldClass = field.getType();
      if (javaBasicTypes.contains(fieldClass.getName())) {
        fieldValueAsString = fieldValue.toString();
      } else if ("java.util.List".equals(fieldClass.getName())) {
        JsonArray jsonArray = new JsonArray();
        List<Object> casted = (List<Object>) fieldValue;
        for (Object obj: casted) {
          jsonArray.add(writeValue(obj));
        }
        fieldValueAsString = jsonArray.toString();
      } else {
        Serialize serialize = field.getAnnotation(Serialize.class);
        fieldValueAsString = serialize == null ?
            writeValue(fieldClass) : getFieldInstanceUsingCustomSerializer(serialize, fieldValue);
      }
      jsonObject.set(fieldName, fieldValueAsString);
    }
    return jsonObject.toString()
        .replaceAll("\\\\", "")
        .replaceAll("\"\\[", "\\[")
        .replaceAll("]\"", "]")
        .replaceAll("\"\\{", "\\{")
        .replaceAll("}\"", "}");
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static String getFieldInstanceUsingCustomSerializer(Serialize serialize, Object obj) {
    Class<? extends JsonFieldSerializer> serializerClass = serialize.using();
    JsonFieldSerializer jsonFieldSerializer = ObjectMappingUtils.getTypedInstance(serializerClass);

    return jsonFieldSerializer.serialize(obj);
  }

  @SuppressWarnings("deprecation")
  private static Object getField(Field field, Object object) {
    boolean isFieldAccessible = field.isAccessible();
    if (!isFieldAccessible) {
      field.setAccessible(true);
      try {
        Object obj = field.get(object);
        field.setAccessible(false);
        return obj;
      } catch (IllegalAccessException x) {
        throw new RuntimeException(x);
      }
    }
    return null;
  }
}
