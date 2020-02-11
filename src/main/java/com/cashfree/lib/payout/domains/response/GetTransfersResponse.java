package com.cashfree.lib.payout.domains.response;

import java.util.List;
import java.util.ArrayList;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

import com.cashfree.lib.utils.CommonUtils;
import com.cashfree.lib.http.ObjectReaderUtils;
import com.cashfree.lib.annotations.Deserialize;
import com.cashfree.lib.payout.domains.TransferDetails;
import com.cashfree.lib.serializers.JsonFieldDeserializer;

@Data
@Accessors(chain = true)
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class GetTransfersResponse extends CfPayoutsResponse {
  private Payload data;

  @Data
  @Accessors(chain = true)
  public static final class Payload {
    @Deserialize(using = ListDeserializer.class)
    private List<TransferDetails> transfers;

    private String lastReturnId;
  }

  public static final class ListDeserializer implements JsonFieldDeserializer<List<TransferDetails>> {
    public ListDeserializer() {}

    @Override
    public List<TransferDetails> deserialize(String serializedString) {
      if (CommonUtils.isBlank(serializedString)) {
        return null;
      }

      JsonArray jsonArray = Json.parse(serializedString).asArray();
      List<TransferDetails> transfers = new ArrayList<>();
      for (int i = 0; i < jsonArray.size(); ++i) {
        JsonValue jsonValue = jsonArray.get(i);
        transfers.add((TransferDetails) ObjectReaderUtils.getFieldInstance(jsonValue, TransferDetails.class));
      }

      return transfers;
    }
  }
}
