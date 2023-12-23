package application;

import controllers.UserController;
import database.Database;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetUserWithId() throws Exception {
        var aUser = Database.getInstance().getUsers().get(0);

        mockMvc.perform(get("/users/{id}", aUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(aUser.getUsername()))
                .andExpect(jsonPath("$.password").value(aUser.getPassword()))
                .andExpect(jsonPath("$.email").value(aUser.getEmail()))
                .andExpect(jsonPath("$.birthDate").value(aUser.getBirthDate()))
                .andExpect(jsonPath("$.address").value(aUser.getAddress()))
                .andExpect(jsonPath("$.credit").value(aUser.getCredit()))
                .andExpect(jsonPath("$.commoditiesRates").value(aUser.getCommoditiesRates()))
                .andExpect(jsonPath("$.buyList").value(aUser.getBuyList()))
                .andExpect(jsonPath("$.purchasedList").value(aUser.getPurchasedList()));
    }


    @Test
    public void testGetUserWhenNotExists() throws Exception {
        mockMvc.perform(get("/users/non-existent-username"))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = { "1000.5", "1.0", "0.0" })
    public void testAddCredit(String credit) throws Exception {
        var body = JsonBuilder.addCredit(credit);
        mockMvc.perform(post("/users/{id}/credit", "ali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("credit added successfully!"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "-1.0", "-1000" })
    public void testAddCreditWithNegativeCredit(String credit) throws Exception {
        var body = JsonBuilder.addCredit(credit);
        mockMvc.perform(post("/users/{id}/credit", "ali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Credit value must be a positive float"));
    }

    @Test
    public void testAddCreditWithInvalidCredit() throws Exception {
        var body = JsonBuilder.addCredit("invalid credit");
        mockMvc.perform(post("/users/{id}/credit", "ali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please enter a valid number for the credit amount."));
    }

}
