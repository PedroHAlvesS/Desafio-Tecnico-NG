package com.ng.billing.bank.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ng.billing.bank.application.PaymentCommand;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.PaymentMethodTypeEnum;
import com.ng.billing.bank.resource.dto.PaymentRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(PaymentResource.class)
class PaymentResourceTest {

    @MockitoBean
    private PaymentCommand paymentCommand;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    PaymentResource paymentResource;

    @BeforeEach
    void setUp() {
        paymentResource = new PaymentResource(paymentCommand);
    }

    @Nested
    @DisplayName("Given PaymentResource")
    class GivenPaymentResource {

        @Nested
        @DisplayName("When payment is called with success")
        class WhenPaymentIsCalledWithSuccess {

            private MvcResult result;
            private final AccountResponseDto expectedResponse = new AccountResponseDto("accountNumber", BigDecimal.ONE);

            @BeforeEach
            void setUp() throws Exception {
                PaymentRequestDto mockRequest = new PaymentRequestDto("123", new BigDecimal("10.00"), PaymentMethodTypeEnum.PIX);

                when(paymentCommand.execute(mockRequest)).thenReturn(expectedResponse);

                result = mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                        .header("Content-Type", "application/json")
                        .content(mapper.writeValueAsString(mockRequest))).andReturn();
            }

            @Test
            @DisplayName("Then should return status ok")
            void ThenShouldReturnStatusOk() {
                assertEquals(200, result.getResponse().getStatus());
            }

            @Test
            @DisplayName("Then should return accountResponseDto")
            void ThenShouldReturnAccountResponseDto() throws JsonProcessingException, UnsupportedEncodingException {
                AccountResponseDto resultDto = mapper.readValue(result.getResponse().getContentAsString(), AccountResponseDto.class);
                assertEquals(expectedResponse, resultDto);
            }
        }

        @Nested
        @DisplayName("When payment is called with failure")
        class WhenPaymentIsCalledWithFailure {

            @Nested
            @DisplayName("When body field is invalid")
            class WhenBodyFieldIsInvalid {

                private MvcResult result;

                @BeforeEach
                void setUp() throws Exception {
                    PaymentRequestDto mockRequest = new PaymentRequestDto("", new BigDecimal("-1"), PaymentMethodTypeEnum.PIX);

                    result = mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                            .header("Content-Type", "application/json")
                            .content(mapper.writeValueAsString(mockRequest))).andReturn();
                }

                @Test
                @DisplayName("Then should return status bad request")
                void ThenShouldReturnStatusBadRequest() {
                    assertEquals(400, result.getResponse().getStatus());
                }
            }

            @Nested
            @DisplayName("When body field is null")
            class WhenBodyFieldIsNull {

                private MvcResult result;

                @BeforeEach
                void setUp() throws Exception {
                    PaymentRequestDto mockRequest = new PaymentRequestDto(null, null, PaymentMethodTypeEnum.PIX);

                    result = mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                            .header("Content-Type", "application/json")
                            .content(mapper.writeValueAsString(mockRequest))).andReturn();
                }

                @Test
                @DisplayName("Then should return status bad request")
                void ThenShouldReturnStatusBadRequest() {
                    assertEquals(400, result.getResponse().getStatus());
                }
            }

            @Nested
            @DisplayName("When account not found")
            class WhenAccountNotFound {

                private MvcResult result;

                @BeforeEach
                void setUp() throws Exception {
                    PaymentRequestDto mockRequest = new PaymentRequestDto("123", new BigDecimal("10.00"), PaymentMethodTypeEnum.PIX);

                    when(paymentCommand.execute(mockRequest)).thenThrow(new GenericException(ErrosEnum.NOT_FOUND));

                    result = mockMvc.perform(MockMvcRequestBuilders.post("/transacao")
                            .header("Content-Type", "application/json")
                            .content(mapper.writeValueAsString(mockRequest))).andReturn();
                }

                @Test
                @DisplayName("Then should return status not found")
                void ThenShouldReturnStatusNotFound() {
                    assertEquals(404, result.getResponse().getStatus());
                }
            }
        }
    }
}