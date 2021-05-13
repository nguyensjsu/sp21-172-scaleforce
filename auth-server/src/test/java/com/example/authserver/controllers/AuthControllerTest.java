package com.example.authserver.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.authserver.requests.UserRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void getJWT() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("Nick");
    userRequest.setPassword("N");

    mockMvc
        .perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$")).exists())
        .andExpect(jsonPath(("$.jwt")).exists());
  }

  @Test
  void getJWT_noRequestBody() throws Exception {
    mockMvc
        .perform(
            post("/auth"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getJWT_invalidUsername() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("Bob");
    userRequest.setPassword("B");

    mockMvc.perform(
        post("/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void getJWT_invalidPassword() throws Exception {
    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("Nick");
    userRequest.setPassword("A");

    mockMvc.perform(
        post("/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateJWT() throws Exception {

    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("Nick");
    userRequest.setPassword("N");

    MvcResult mvcResult = mockMvc
        .perform(
            post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
        .andReturn();

    JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
    String token = jsonNode.get("jwt").asText();

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$")).exists())
        .andExpect(jsonPath(("$.jwt")).exists())
        .andExpect(jsonPath(("$.jwt")).value("valid"));
  }

  @Test
  void validateJWT_expiredJwtException() throws Exception {
    /*
    Payload:
    {
      "jti": "f23c3e88-8115-484c-bdc1-f18e941af222",
      "iss": "HaircutAuthServer",
      "sub": "Nick",
      "type": "ADMIN",
      "iat": 1619522003,
      "exp": 1519522123
    }
    note: exp in formatted time is: Sat Feb 24 2018 17:28:43 UTC-0800 (Pacific Standard Time)
    */

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjNjM2U4OC04MTE1LTQ4NGMtYmRjMS1mMThlOTQxYWYyMj"
        + "IiLCJpc3MiOiJIYWlyY3V0QXV0aFNlcnZlciIsInN1YiI6Ik5pY2siLCJ0eXBlIjoiQURNSU4iLCJpYXQiOjE2MT"
        + "k1MjIwMDMsImV4cCI6MTUxOTUyMjEyM30.OZ0vukmXgqRiWCBtc5mhb5SM_XKT5QG3vWqGp1Zp9DE";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateJWT_unsupportedJwtException() throws Exception {
    // token represents an unsigned claims JWT
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzNzY1OWMwMi05NTI2LTQ5OTEtYjUyNS05OTM4NTdkNTM0NT"
        + "IiLCJpc3MiOiJIYWlyY3V0QXV0aFNlcnZlciIsInN1YiI6Ik5pY2siLCJ0eXBlIjoiQURNSU4iLCJpYXQiOjE2MT"
        + "k2NTUxNDYsImV4cCI6OTk5OTk5OTk5OX0.";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateJWT_malformedJwtException() throws Exception {
    /*
    Payload:
    "I�J�\u0004IHǊ(]�O���ǉ~�:N�%_�u\u000b,×"
     */
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5"
        + "c";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateJWT_signatureException() throws Exception {
    /*
    Payload:
    "{\"sub\":\"1234567890\",\"name\":\"Joho��\u0011�������Ј�������������"
     */
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG"
        + "4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfaQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateJWT_illegalArgumentException() throws Exception {
    String token = "";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }

  @Disabled
  @Test
  void validateJWT_nullClaims() throws Exception {
    /*
    may not actually be able to create a test s.t. validateJWT passes an invalid JWT to
    jwtUtil.getClaims():
    - validateJWT first passes the JWT to jwtUtil.validateJwt
    - jwtUtil.validateJwt passes the JWT to jwtParser.parseClaimsJws, which raises an exception
      upon an invalid JWT
    - validateJWT then passes the valid JWT to jwtUtil.getClaims
    - jwtUtil.getClaims passes the JWT (again) to jwtParser.parseClaimsJws, so in theory, we are
      guaranteed that no exception will be raised
    - therefore, claims can never be null
     */
  }

  @Test
  void validateJWT_invalidIssuer() throws Exception {
    /*
    Payload:
    {
      "jti": "081ed612-281a-451e-9b7b-6b90d916716d",
      "iss": "ManInTheMiddle",
      "sub": "Nick",
      "type": "ADMIN",
      "iat": 1619524935,
      "exp": 9999999999
    }
    note: key here is the mismatch in expected issuer (should be HaircutAuthServer)
     */

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwODFlZDYxMi0yODFhLTQ1MWUtOWI3Yi02YjkwZDkxNjcxNm"
        + "QiLCJpc3MiOiJNYW5JblRoZU1pZGRsZSIsInN1YiI6Ik5pY2siLCJ0eXBlIjoiQURNSU4iLCJpYXQiOjE2MTk1Mj"
        + "Q5MzUsImV4cCI6OTk5OTk5OTk5OX0.sVOK-WZt7XkbbIWmI0V-kUw5Eg9A8Kwu6tyrbgUl4D8";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateJWT_invalidSubject() throws Exception {
    /*
    Payload:
    {
      "jti": "081ed612-281a-451e-9b7b-6b90d916716d",
      "iss": "HaircutAuthServer",
      "sub": "Bob",
      "type": "ADMIN",
      "iat": 1619524935,
      "exp": 9999999999
    }
     */

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwODFlZDYxMi0yODFhLTQ1MWUtOWI3Yi02YjkwZDkxNjcxNm"
        + "QiLCJpc3MiOiJIYWlyY3V0QXV0aFNlcnZlciIsInN1YiI6IkJvYiIsInR5cGUiOiJBRE1JTiIsImlhdCI6MTYxOT"
        + "UyNDkzNSwiZXhwIjo5OTk5OTk5OTk5fQ.68-gPM788QoVTozWivgwcGZ0iENPn5c4m57xm0XPsrU";

    mockMvc
        .perform(
            post("/validate")
                .header("Authorization", "Bearer: " + token))
        .andExpect(status().isUnauthorized());
  }
}
