package application;

import controllers.CommoditiesController;
import database.Database;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import service.Baloot;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommoditiesController.class)
public class CommoditiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    User aUser = Database.getInstance().getUsers().get(0);
    ;
    List<Commodity> commodities = Database.getInstance().getCommodities();
    ;
    Commodity aCommodity = commodities.get(0);

    @BeforeEach
    public void setUpFixture() {
        Baloot.getInstance().fetchAndStoreData();
    }

    @Test
    public void testGetCommoditiesWhenCommoditiesExist() throws Exception {
        mockMvc.perform(get("/commodities"))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(commodities)));
    }


    @Test
    public void testGetCommoditiesWithIdWhenCommodityExists() throws Exception {
        mockMvc.perform(get("/commodities/{id}", aCommodity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(aCommodity)));
    }

    @Test
    public void testGetCommoditiesWithIdWhenNotExists() throws Exception {
        mockMvc.perform(get("/commodities/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRateCommodity() throws Exception {
        var body = JsonBuilder.rateCommodity(aUser.getUsername(), "5");
        mockMvc.perform(post("/commodities/{id}/rate", aCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("rate added successfully!"));
    }

    @Test
    public void testRateCommodityWhenCommodityNotExists() throws Exception {
        var body = JsonBuilder.rateCommodity(aUser.getUsername(), "5");
        mockMvc.perform(post("/commodities/non-existent-commodity/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Commodity does not exist."));
    }

    @Test
    public void testRateCommodityWhitInvalidRate() throws Exception {
        var body = JsonBuilder.rateCommodity(aUser.getUsername(), "invalid rate");
        mockMvc.perform(post("/commodities/{id}/rate", aCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("For input string: \"invalid rate\""));
    }

    @Test
    public void testAddComment() throws Exception {
        var body = JsonBuilder.addComment(aUser.getUsername());
        mockMvc.perform(post("/commodities/{id}/comment", aCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("comment added successfully!"));
    }

    @Test
    public void testAddCommentWhenUserNotExists() throws Exception {
        var body = JsonBuilder.addComment("non-existent-username");
        mockMvc.perform(post("/commodities/{id}/comment", aCommodity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User does not exist."));
    }

    @Test
    public void testGetCommentWithId() throws Exception {
        List<Comment> comments = Database.getInstance()
                .getComments().stream()
                .filter(comment -> comment.getCommodityId() == 1)
                .toList();

        mockMvc.perform(get("/commodities/{id}/comment", aCommodity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(comments)));
    }

    @Test
    public void testSearchByName() throws Exception {
        var body = JsonBuilder.searchCommodities(aCommodity.getName(), "name");
        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(
                        List.of(aCommodity)
                )));
    }

    @Test
    public void testSearchByCategory() throws Exception {
        var body = JsonBuilder.searchCommodities(aCommodity.getCategories().get(0), "category");
        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(commodities)));
    }

    @Test
    public void testSearchByProvider() throws Exception {
        var body = JsonBuilder.searchCommodities("apple", "provider");
        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(
                        List.of(aCommodity)
                )));
    }

    @Test
    public void testSearchWhenNoMatchFound() throws Exception {
        var body = JsonBuilder.searchCommodities("non-existent-name", "name");
        mockMvc.perform(post("/commodities/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(
                        List.of()
                )));
    }

    @Test
    public void testGetSuggested() throws Exception {
        mockMvc.perform(get("/commodities/{id}/suggested", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string(JsonBuilder.toJsonString(
                        List.of(aCommodity))));
    }

    @Test
    public void testGetSuggestedWhenCommodityNotExists() throws Exception {
        mockMvc.perform(get("/commodities/non-existent-commodity/suggested"))
                .andExpect(status().isNotFound());
    }
}
