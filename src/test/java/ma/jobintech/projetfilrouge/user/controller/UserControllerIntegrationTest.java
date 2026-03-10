package ma.jobintech.projetfilrouge.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.jobintech.projetfilrouge.common.enums.Role;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired MockMvc       mockMvc;
    @Autowired ObjectMapper  objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_asAdmin_returns201() throws Exception {
        CreateUserRequest req = new CreateUserRequest(
            "Test User", "test.user3@university.ma", "Password123!", Role.ETUDIANT
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test.user3@university.ma"))
            .andExpect(jsonPath("$.role").value("ETUDIANT"));
    }

    @Test
    @WithMockUser(roles = "ETUDIANT")
    void createUser_asEtudiant_returns403() throws Exception {
        CreateUserRequest req = new CreateUserRequest(
            "Hacker", "hack@test.ma", "pass123", Role.ADMIN
        );

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_asAdmin_returns200() throws Exception {
        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Sql("/test-data.sql")
    void login_validCredentials_returnsToken() throws Exception {
        String body = """
            {"email":"admin@university.ma","password":"Admin@1234"}
            """;

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}