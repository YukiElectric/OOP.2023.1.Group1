package oop.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseDTO {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("volume")
    private String volume;
    
    @JsonProperty("floorPrice")
    private String floorPrice;


}

