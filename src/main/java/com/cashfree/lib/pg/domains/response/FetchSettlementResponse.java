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
public class FetchSettlementResponse {
  private String status;

  @Deserialize(using = ListDeserializer.class)
  private List<Transaction> transactions;

  private String message;

  private String lastId;

  public static final class ListDeserializer implements JsonFieldDeserializer<List<Transaction>> {
    public ListDeserializer() {}

    @Override
    public List<Transaction> deserialize(String serializedString) {
      if (CommonUtils.isBlank(serializedString)) {
        return null;
      }

      JsonArray jsonArray = Json.parse(serializedString).asArray();
      List<Transaction> transfers = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); ++i) {
        JsonValue jsonValue = jsonArray.get(i);
        transfers.add((Transaction) ObjectReaderUtils.getFieldInstance(jsonValue, Transaction.class));
      }

      return transfers;
    }
  }
}
