package com.project.starcoffee.webClientTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.starcoffee.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockResponseData {
    private String orderId;
    private String memberId;
    private long storeId;
    private int itemCount;
    private long finalPrice;
    private List<OrderItemDTO> orderItems;

    // 예상 응답을 객체로 변환하는 메서드
    public String toJsonString(ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Collections.singletonList(this));
    }
}
