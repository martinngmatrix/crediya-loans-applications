package co.com.bancolombia.model.notification;
import lombok.Builder;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Notification {
    private Map<String, Object> payload;
    private String queueKey;
}
