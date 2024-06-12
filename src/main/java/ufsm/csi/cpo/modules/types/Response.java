package ufsm.csi.cpo.modules.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response <T> {
    @JsonProperty("data")
    private T data;
    private int statusCode;
    private String statusMessage;
    private Date timestamp;


    public Response (T data, int statusCode, String statusMessage, Date timestamp) {
        this.data = data;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.timestamp = timestamp;
    }

    public Response (int statusCode, String statusMessage, Date timestamp) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.timestamp = timestamp;
    }
}
