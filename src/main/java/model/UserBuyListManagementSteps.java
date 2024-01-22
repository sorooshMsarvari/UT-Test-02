package model;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import exceptions.CommodityIsNotInBuyList;
import org.testng.Assert;

public class UserBuyListManagementSteps {
    /*
    Feature: User Buy List Management

  Scenario: Remove an item from the user's buy list with quantity greater than 1
    Given a user with a buy list containing item X with quantity 2
    When the user removes 1 item X from the buy list
    Then the quantity of item X in the buy list should be 1

  Scenario: Remove the last item from the user's buy list
    Given a user with a buy list containing only 1 item Y
    When the user removes 1 item Y from the buy list
    Then the buy list should be empty

  Scenario: Remove an item that is not in the user's buy list
    Given a user with an empty buy list
    When the user tries to remove item Z from the buy list
    Then a CommodityIsNotInBuyList exception should be thrown
     */

    private User user;

    @Given("a user with a buy list containing item X with quantity {int}")
    public void givenAUserWithBuyListContainingItemX(int initialQuantity) {
        user = new User();
        user.addBuyItemWhitQuantity(new Commodity(), initialQuantity);
    }

    @When("the user removes {int} item X from the buy list")
    public void whenTheUserRemovesItemXFromBuyList(int quantityToRemove) {
        try {
            user.removeItemFromBuyListWhitQuantity(new Commodity(), quantityToRemove);
        } catch (CommodityIsNotInBuyList e) {

        }
    }

    @Then("the quantity of item X in the buy list should be {int}")
    public void thenTheQuantityOfItemXShouldBe(int expectedQuantity) {
        int actualQuantity = user.getBuyList().get("X");
        Assert.assertEquals(expectedQuantity, actualQuantity);
    }

    @Then("the buy list should be empty")
    public void thenTheBuyListShouldBeEmpty() {
        Assert.assertTrue(user.getBuyList().isEmpty());
    }

    @Then("a CommodityIsNotInBuyList exception should be thrown")
    public void thenACommodityIsNotInBuyListExceptionShouldBeThrown() {
    }
}
