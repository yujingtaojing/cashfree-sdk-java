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
import com.cashfree.lib.serializers.JsonFieldDeserializer;

@Data
@Accessors(chain = true)
public class ListSettlementsResponse {
  private String status;

  private List<Settlement> settlements;

  private String message;

  private String lastId;

  public static final class ListDeserializer implements JsonFieldDeserializer<List<Settlement>> {
    public ListDeserializer() {}

    @Override
    public List<Settlement> deserialize(String serializedString) {
      if (CommonUtils.isBlank(serializedString)) {
        return null;
      }

      JsonArray jsonArray = Json.parse(serializedString).asArray();
      List<Settlement> transfers = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); ++i) {
        JsonValue jsonValue = jsonArray.get(i);
        transfers.add((Settlement) ObjectReaderUtils.getFieldInstance(jsonValue, Settlement.class));
      }

      return transfers;
    }
  }
}
