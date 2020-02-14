package com.cashfree.lib.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import com.cashfree.lib.serializers.JsonFieldSerializer;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serialize {
  @SuppressWarnings("rawtypes")
  Class<? extends JsonFieldSerializer> using();
}