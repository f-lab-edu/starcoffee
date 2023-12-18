package com.project.starcoffee.webClientTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.starcoffee.controller.response.order.OrderResponse;
import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.domain.item.ItemType;
import com.project.starcoffee.domain.order.CupSize;
import com.project.starcoffee.domain.order.ItemSize;
import com.project.starcoffee.dto.ItemDTO;
import com.project.starcoffee.dto.OrderItemDTO;
import com.project.starcoffee.redis.CartRedisDAO;
import com.project.starcoffee.service.CartService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockWebServerTest {

    private MockWebServer mockWebServer;
    private CartService cartService;
    private RedisTemplate<String, List<ItemDTO>> redisListTemplate;
    private ObjectMapper objectMapper;

    @BeforeEach
    void initialize() {
        this.mockWebServer = new MockWebServer();
        CartRedisDAO cartRedisDAO = new CartRedisDAO(redisListTemplate);
        final String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        final WebClient webClient = WebClient.create(baseUrl);
        cartService = new CartService(mockCartDAO(),webClient);
        this.objectMapper = new ObjectMapper();
    }

    @AfterEach
    void shutdown() throws IOException {
        if (mockWebServer != null) {
            this.mockWebServer.shutdown();
        }
    }

    private CartRedisDAO mockCartDAO() {
        CartRedisDAO cartDAO = mock(CartRedisDAO.class);

        // 테스트에 필요한 대로 cartDAO 메서드의 동작을 목업
        // 예를 들어, cartDAO.findItem이 호출되면 샘플 List<ItemDTO>를 반환하도록 설정
        when(cartDAO.getCartItems(any(UUID.class))).thenReturn(Arrays.asList(new ItemDTO()));
        return cartDAO;
    }


    @Test
    void requestOrderTest() throws Exception {
        // Mock 데이터 생성
        UUID cartId = UUID.randomUUID();

        String expectedOrderId = "c4522190-7fe0-11ee-be52-14206599d046";
        String expectedMemberId = "7ddf7578-6a87-11ee-af50-ecf40330e8fa";
        long expectedStoreId = 1L;
        int expectedItemCount = 2;
        long expectedFinalPrice = 9000;
        List<OrderItemDTO> expectedOrderItems = Collections.singletonList(
                OrderItemDTO.builder()
                        .itemId(1L)
                        .itemName("아이스 아메리카노")
                        .itemPrice(4500)
                        .itemType(ItemType.ICED)
                        .itemSize(ItemSize.TALL)
                        .cupSize(CupSize.DISPOSABLE_CUP)
                        .build());

        // given
        // 테스트 결과를 받을 Response 데이터를 MockWebServer 에 주입한다.
        MockResponseData responseData = MockResponseData.builder()
                        .orderId(expectedOrderId)
                        .memberId(expectedMemberId)
                        .storeId(expectedStoreId)
                        .itemCount(expectedItemCount)
                        .finalPrice(expectedFinalPrice)
                        .orderItems(expectedOrderItems).build();


        mockWebServer.enqueue(new MockResponse()
                .setHeader("Content-type", MediaType.APPLICATION_JSON)
                .setResponseCode(200)
                .setBody(responseData.toJsonString(objectMapper)));


        // when
        List<OrderResponse> orderResponses = cartService.requestOrder(cartId);

        // then
        assertThat(orderResponses).isNotNull().hasSize(1);
        OrderResponse orderResponse = orderResponses.get(0);

//        assertThat(orderResponse.getOrderId()).isEqualTo(expectedOrderId);
//        assertThat(orderResponse.getMemberId()).isEqualTo(expectedMemberId);
        assertThat(orderResponse.getStoreId()).isEqualTo(expectedStoreId);
        assertThat(orderResponse.getItemCount()).isEqualTo(expectedItemCount);
        assertThat(orderResponse.getFinalPrice()).isEqualTo(expectedFinalPrice);
        assertThat(orderResponse.getOrderItems()).isEqualTo(expectedOrderItems);


        // 예상대로 모의 서버에 요청이 이루어졌는지 확인
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo("/order/new");
    }
}
