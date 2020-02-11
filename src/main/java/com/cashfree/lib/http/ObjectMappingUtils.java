package com.cashfree.lib.http;

import java.util.List;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.ArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ObjectMappingUtils {
  public enum JsonValueType {
    NULL,
    BOOLEAN,
    NUMBER,
    STRING,
    OBJECT,
    ARRAY,
    UNMAPPED
  }

  public static final EnumSet<JsonValueType> jsonBaseTypes =
      EnumSet.of(JsonValueType.BOOLEAN, JsonValueType.NUMBER, JsonValueType.STRING);

  public static void getAllFields(List<Field> fields, Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));
    if (type.getSuperclass() != null) {
      getAllFields(fields, type.getSuperclass());
    }
  }

  public static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    getAllFields(fields, clazz);
    return fields;
  }

  private static Object getInstanceOfClass(String className) {
    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException x) {
      throw new RuntimeException(x);
    }
    return getInstanceOfClass(clazz);
  }

  public static Object getInstanceOfClass(Class<?> clazz) {
    Constructor<?> ctor;
    try {
      ctor = clazz.getConstructor();
    } catch (NoSuchMethodException x) {
      throw new RuntimeException(x);
    }
    try {
      return ctor.newInstance();
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException x) {
      throw new RuntimeException(x);
    }
  }

  public static <T> T getTypedInstance(Class<T> clazz) {
    Constructor<?> ctor;
    try {
      ctor = clazz.getConstructor();
    } catch (NoSuchMethodException x) {
      throw new RuntimeException(x);
    }
    try {
      return clazz.cast(ctor.newInstance());
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException x) {
      throw new RuntimeException(x);
    }
  }
}
