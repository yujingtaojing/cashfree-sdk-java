package com.cashfree.lib.payout.domains.response;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.http.ObjectReaderUtils;
import com.cashfree.lib.annotations.Deserialize;
import com.cashfree.lib.serializers.JsonFieldDeserializer;

@Data
@Accessors(chain = true)
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class BulkValidationStatusResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    private String bulkValidationId;

    @Deserialize(using = CustomMapDeserializer.class)
    private Map<String, Entry> entries;

    @Data
    @Accessors(chain = true)
    public static final class Entry {
      private String name;

      private String phone;

      private String bankAccount;

      private String ifsc;

      private String accountExists;

      private String nameAtBank;

      private BigDecimal amountDeposited;

      private String utr;

      private String refId;

      private String message;
    }
  }

  public static final class CustomMapDeserializer implements JsonFieldDeserializer<Map<String, Payload.Entry>> {
    public CustomMapDeserializer() {}

    public Map<String, Payload.Entry> deserialize(String serializedString) {
      if (CommonUtils.isBlank(serializedString)) {
        return null;
      }

      JsonObject jsonObject = Json.parse(serializedString).asObject();
      Map<String, Payload.Entry> map = new HashMap<>();
      Iterator<JsonObject.Member> it = jsonObject.iterator();
      for (; it.hasNext();) {
        JsonObject.Member member = it.next();
        map.put(member.getName(), (Payload.Entry) ObjectReaderUtils.getFieldInstance(member.getValue(), Payload.Entry.class));
      }

      return map;
    }
  }
}
