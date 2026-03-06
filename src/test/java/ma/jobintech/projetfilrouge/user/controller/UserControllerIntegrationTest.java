package ma.jobintech.projetfilrouge.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.jobintech.projetfilrouge.user.dto.request.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.entity.Role;
import ma.jobintech.projetfilrouge.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void postUser_validPayload_returns201() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setNom("Test User"); req.setEmail("test@uni.ma");
        req.setPassword("password123"); req.setRole(Role.ETUDIANT);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ETUDIANT")
    void postUser_asEtudiant_returns403() throws Exception {

        CreateUserRequest req = new CreateUserRequest();
        req.setNom("Test User");
        req.setEmail("test@uni.ma");
        req.setPassword("password123");
        req.setRole(Role.ETUDIANT);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void postUser_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void postUser_emptyNom_returns400() throws Exception {
        String body = """
            {"nom":"","email":"a@b.com","password":"123456","role":"ETUDIANT"}
            """;
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.nom").exists());
    }
}
