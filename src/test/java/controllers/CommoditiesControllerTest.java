package controllers;

import exceptions.NotExistentComment;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Comment;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import service.Baloot;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommoditiesControllerTest {
    @Mock
    Baloot baloot;
    @InjectMocks
    CommoditiesController controller;

    @Test
    void getCommoditiesSuccessfully() {
        assertThat(controller.getCommodities().getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getCommoditySuccessfully() throws NotExistentCommodity {
        when(baloot.getCommodityById("1")).thenReturn(new Commodity());
        assertThat(controller.getCommodity("1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getCommodityWhileNotFound() throws NotExistentCommodity {
        when(baloot.getCommodityById("2")).thenThrow(NotExistentCommodity.class);
        assertThat(controller.getCommodity("2").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void rateCommoditySuccessfully() throws NotExistentCommodity {
        when(baloot.getCommodityById("1")).thenReturn(new Commodity());
        var userRateMap = new HashMap<String, String>();
        userRateMap.put("rate", "2");
        userRateMap.put("username", "folani");
        assertThat(controller.rateCommodity("1", userRateMap).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void rateCommodityWhenRateInvalid() {
        var userRateMap = new HashMap<String, String>();
        userRateMap.put("rate", "folannumber");
        userRateMap.put("username", "folani");
        assertThat(controller.rateCommodity("1", userRateMap).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void rateCommodityWhenCommodityNotFound() throws NotExistentCommodity {
        when(baloot.getCommodityById("2")).thenThrow(NotExistentCommodity.class);
        var userRateMap = new HashMap<String, String>();
        userRateMap.put("rate", "2");
        userRateMap.put("username", "folani");
        assertThat(controller.rateCommodity("2", userRateMap).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void addCommodityCommentSuccessfully() throws NotExistentUser {
        when(baloot.generateCommentId()).thenReturn(1);
        when(baloot.getUserById("user1")).thenReturn(new User());

        var inputMap = new HashMap<String, String>();
        inputMap.put("username", "user1");
        inputMap.put("comment", "Nice product");

        assertThat(controller.addCommodityComment("1", inputMap).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addCommodityCommentWhenUserNotFound() throws NotExistentUser {
        when(baloot.getUserById("1")).thenThrow(NotExistentUser.class);
        var inputMap = new HashMap<String, String>();
        inputMap.put("username", "1");
        inputMap.put("comment", "Nice product");

        assertThat(controller.addCommodityComment("1", inputMap).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getCommodityCommentSuccessfully() {
        when(baloot.getCommentsForCommodity(1)).thenReturn(new ArrayList<>());
        assertThat(controller.getCommodityComment("1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void searchCommoditiesByNameSuccessfully() {
        var inputMap = new HashMap<String, String>();
        inputMap.put("searchOption", "name");
        inputMap.put("searchValue", "product");

        when(baloot.filterCommoditiesByName("product")).thenReturn(new ArrayList<>());

        assertThat(controller.searchCommodities(inputMap).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getSuggestedCommoditiesSuccessfully() throws NotExistentCommodity {
        when(baloot.getCommodityById("1")).thenReturn(new Commodity());
        when(baloot.suggestSimilarCommodities(any(Commodity.class))).thenReturn(new ArrayList<>());

        assertThat(controller.getSuggestedCommodities("1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getSuggestedCommoditiesNotFound() throws NotExistentCommodity {
        doThrow(NotExistentCommodity.class).when(baloot).getCommodityById("2");

        assertThat(controller.getSuggestedCommodities("2").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }




}
