package com.e_commerce.order_api.dto;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
        private Long id;
        private String username;
        private String email;
        private String phone;

}
