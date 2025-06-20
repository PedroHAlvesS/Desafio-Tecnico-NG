package com.ng.billing.bank.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ng.billing.bank.application.CreateAccountCommand;
import com.ng.billing.bank.application.RetrieveAccountCommand;
import com.ng.billing.bank.domain.exception.ErrosEnum;
import com.ng.billing.bank.domain.exception.generic.GenericException;
import com.ng.billing.bank.resource.dto.AccountResponseDto;
import com.ng.billing.bank.resource.dto.CreateAccountRequestDto;
import com.ng.billing.bank.utils.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(AccountResource.class)
class AccountResourceTest {

    @MockitoBean
    private CreateAccountCommand createAccountCommand;

    @MockitoBean
    private RetrieveAccountCommand retrieveAccountCommand;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    AccountResource accountResource;

    @BeforeEach
    void setUp() {
        accountResource = new AccountResource(createAccountCommand, retrieveAccountCommand);
    }

    @Nested
    @DisplayName("Given AccountResource is started")
    class GivenAccountResource {

        @Nested
        @DisplayName("When createAccount is called")
        class WhenCreateAccount {

            @Nested
            @DisplayName("And Success")
            class AndSuccess {
                private MvcResult result;
                private final AccountResponseDto expectedResponse = new AccountResponseDto("accountNumber", BigDecimal.ONE);

                @BeforeEach
                void mockAndAct() throws Exception {
                    CreateAccountRequestDto mockRequest = new CreateAccountRequestDto("accountNumber", BigDecimal.ONE);
                    when(createAccountCommand.execute(mockRequest)).thenReturn(expectedResponse);

                    result = mockMvc.perform(MockMvcRequestBuilders.post("/conta")
                            .header("Content-Type", "application/json")
                            .content(mapper.writeValueAsString(mockRequest))).andReturn();
                }

                @Test
                @DisplayName("Then return status code 201")
                void thenReturnStatusCode201() {
                    assertEquals(201, result.getResponse().getStatus());
                }

                @Test
                @DisplayName("Then return accountResponseDto")
                void thenReturnAccountResponseDto() throws UnsupportedEncodingException, JsonProcessingException {
                    AccountResponseDto resultDto = TestUtil.getMapper().readValue(result.getResponse().getContentAsString(), AccountResponseDto.class);
                    assertEquals(expectedResponse, resultDto);
                }
            }

            @Nested
            @DisplayName("And Failure")
            class AndFailure {
                @Nested
                @DisplayName("When body fields is invalid")
                class WhenBodyFieldsIsInvalid {

                    private MvcResult result;

                    @BeforeEach
                    void mockAndAct() throws Exception {
                        CreateAccountRequestDto mockRequest = new CreateAccountRequestDto("", new BigDecimal("-1"));

                        result = mockMvc.perform(MockMvcRequestBuilders.post("/conta")
                                .header("Content-Type", "application/json")
                                .content(mapper.writeValueAsString(mockRequest))).andReturn();
                    }

                    @Test
                    @DisplayName("Then return status code 400")
                    void thenReturnStatusCode400() {
                        assertEquals(400, result.getResponse().getStatus());
                    }
                }

                @Nested
                @DisplayName("When body fields is null")
                class WhenBodyFieldsIsNull {

                    private MvcResult result;

                    @BeforeEach
                    void mockAndAct() throws Exception {
                        CreateAccountRequestDto mockRequest = new CreateAccountRequestDto(null, null);

                        result = mockMvc.perform(MockMvcRequestBuilders.post("/conta")
                                .header("Content-Type", "application/json")
                                .content(mapper.writeValueAsString(mockRequest))).andReturn();
                    }

                    @Test
                    @DisplayName("Then return status code 400")
                    void thenReturnStatusCode400() {
                        assertEquals(400, result.getResponse().getStatus());
                    }
                }

                @Nested
                @DisplayName("When throws DataIntegrityViolationException")
                class WhenThrowsDataIntegrityViolationException {

                    private MvcResult result;

                    @BeforeEach
                    void mockAndAct() throws Exception {
                        CreateAccountRequestDto mockRequest = new CreateAccountRequestDto("accountNumber", BigDecimal.ONE);

                        when(createAccountCommand.execute(mockRequest)).thenThrow(DataIntegrityViolationException.class);

                        result = mockMvc.perform(MockMvcRequestBuilders.post("/conta")
                                .header("Content-Type", "application/json")
                                .content(mapper.writeValueAsString(mockRequest))).andReturn();
                    }

                    @Test
                    @DisplayName("Then return status code 409")
                    void thenReturnStatusCode409() {
                        assertEquals(409, result.getResponse().getStatus());
                    }
                }

                @Nested
                @DisplayName("When throws HttpMessageNotReadableException")
                class WhenThrowsHttpMessageNotReadableException {

                    private MvcResult result;

                    @BeforeEach
                    void mockAndAct() throws Exception {
                        result = mockMvc.perform(MockMvcRequestBuilders.post("/conta")
                                        .header("Content-Type", "application/json"))
                                .andReturn();
                    }

                    @Test
                    @DisplayName("Then return status code 400")
                    void thenReturnStatusCode400() {
                        assertEquals(400, result.getResponse().getStatus());
                    }
                }
            }
        }

        @Nested
        @DisplayName("When getAccount is called")
        class WhenGetAccount {
            @Nested
            @DisplayName("And Success")
            class AndSuccess {
                private MvcResult result;
                private final AccountResponseDto expectedResponse = new AccountResponseDto("accountNumber", BigDecimal.ONE);

                @BeforeEach
                void mockAndAct() throws Exception {
                    when(retrieveAccountCommand.execute("accountNumber")).thenReturn(expectedResponse);

                    result = mockMvc.perform(MockMvcRequestBuilders.get("/conta")
                            .header("Content-Type", "application/json")
                            .queryParam("numero_conta", "accountNumber")).andReturn();
                }

                @Test
                @DisplayName("Then return status code 200")
                void thenReturnStatusCode200() {
                    assertEquals(200, result.getResponse().getStatus());
                }

                @Test
                @DisplayName("Then return accountResponseDto")
                void thenReturnAccountResponseDto() throws UnsupportedEncodingException, JsonProcessingException {
                    AccountResponseDto resultDto = TestUtil.getMapper().readValue(result.getResponse().getContentAsString(), AccountResponseDto.class);
                    assertEquals(expectedResponse, resultDto);
                }
            }

            @Nested
            @DisplayName("And Failure")
            class AndFailure {
                @Nested
                @DisplayName("When request param is null")
                class WhenRequestParamIsNull {

                    private MvcResult result;

                    @BeforeEach
                    void mockAndAct() throws Exception {

                        result = mockMvc.perform(MockMvcRequestBuilders.get("/conta")
                                .header("Content-Type", "application/json")
                        ).andReturn();
                    }

                    @Test
                    @DisplayName("Then return status code 500")
                    void thenReturnStatusCode500() {
                        assertEquals(500, result.getResponse().getStatus());
                    }
                }

                @Nested
                @DisplayName("When throws not found")
                class WhenThrowsNotFoundException {
                    private MvcResult result;

                    @BeforeEach
                    void mockAndAct() throws Exception {
                        when(retrieveAccountCommand.execute("accountNumber")).thenThrow(new GenericException(ErrosEnum.NOT_FOUND));

                        result = mockMvc.perform(MockMvcRequestBuilders.get("/conta")
                                .header("Content-Type", "application/json")
                                .queryParam("numero_conta", "accountNumber")).andReturn();
                    }

                    @Test
                    @DisplayName("Then return status code 404")
                    void thenReturnStatusCode404() {
                        assertEquals(404, result.getResponse().getStatus());
                    }
                }
            }
        }
    }
}