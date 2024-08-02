package com.e_commerce.order_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class CartDto {
    private Integer cartId;
    private Long userId;

    @JsonProperty("order")
    @JsonInclude(Include.NON_NULL)
    private Set<OrderDto> orderDtos;

    @JsonProperty("user")
    @JsonInclude(Include.NON_NULL)
    private UserDto userDto;
}
