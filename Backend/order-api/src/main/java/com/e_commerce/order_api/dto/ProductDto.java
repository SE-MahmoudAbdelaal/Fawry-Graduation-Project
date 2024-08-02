package com.e_commerce.order_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class ProductDto {

        private Integer productId;
        private Double priceUnit;
        private Integer quantity;

        @JsonProperty("order")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private OrderDto orderDto;
}
