package com.cashfree.lib.pg.domains.response;

import java.util.List;
import java.util.ArrayList;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import lombok.Data;
import lombok.experimental.Accessors;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.http.ObjectReaderUtils;
import com.cashfree.lib.annotations.Deserialize;
import com.cashfree.lib.serializers.JsonFieldDeserializer;

@Data
@Accessors(chain = true)
public class ListRefundsResponse {
  private String status;

  private String settlements;

  private String message;

  private Integer lastId;

  @Deserialize(using = ListDeserializer.class)
  private List<Refund> refund;

  public static final class ListDeserializer implements JsonFieldDeserializer<List<Refund>> {
    public ListDeserializer() {}

    @Override
    public List<Refund> deserialize(String serializedString) {
      if (CommonUtils.isBlank(serializedString)) {
        return null;
      }

      JsonArray jsonArray = Json.parse(serializedString).asArray();
      List<Refund> transfers = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); ++i) {
        JsonValue jsonValue = jsonArray.get(i);
        transfers.add((Refund) ObjectReaderUtils.getFieldInstance(jsonValue, Refund.class));
      }

      return transfers;
    }
  }
}
