package ma.jobintech.projetfilrouge.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ma.jobintech.projetfilrouge.user.dto.CreateUserRequest;
import ma.jobintech.projetfilrouge.user.entity.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    // ── T-B01 : Création nominale (HTTP 201) ──────────────────────────
    @Test @WithMockUser(roles = "ADMIN")
    void postUser_valid_returns201() throws Exception {
        String body = json.writeValueAsString(
            new CreateUserRequest("Ali", "ali22@test.com", "123456", Role.ETUDIANT));

        mvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("ali22@test.com"))
            .andExpect(jsonPath("$.actif").value(true)); // CA-3
    }
    // ── T-B07 : Nom vide → HTTP 400 ───────────────────────────────────
    @Test @WithMockUser(roles = "ADMIN")
    void postUser_emptyNom_returns400() throws Exception {
        String body = "{\"nom\":\"\",\"email\":\"a@b.com\","
            + "\"password\":\"123456\",\"role\":\"ETUDIANT\"}";

        mvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.nom").value("Nom obligatoire")); // CA-1
    }

    // ── T-B14 : Accès non-admin → HTTP 403 ───────────────────────────
    @Test
    @WithMockUser(roles = "ETUDIANT")
    void postUser_asEtudiant_returns403() throws Exception {

        String body = json.writeValueAsString(
            new CreateUserRequest("Ali", "ali2@test.com", "123456", Role.ETUDIANT)
        );

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isForbidden());
    }
    // ── T-B15 : Sans token → HTTP 401 ────────────────────────────────
    @Test
    void postUser_noToken_returns401() throws Exception {

        String body = json.writeValueAsString(
            new CreateUserRequest("Ali", "ali3@test.com", "123456", Role.ETUDIANT)
        );

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isUnauthorized());
    }
}