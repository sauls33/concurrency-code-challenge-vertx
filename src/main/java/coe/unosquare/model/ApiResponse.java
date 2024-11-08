package coe.unosquare.model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public record ApiResponse(boolean success, String message, Object data) {

        public String convertToJson() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return "{\"success\":false,\"message\":\"Serialization error\",\"data\":null}";
            }
        }

}
