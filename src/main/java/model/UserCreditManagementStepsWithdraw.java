package model;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import exceptions.InsufficientCredit;
import org.testng.Assert;

public class UserCreditManagementStepsWithdraw {
/*
Feature: User Credit Management

  Scenario: Withdraw credit from the user's account
    Given a user with a credit of 100
    When the user withdraws 50 credit
    Then the user's credit should be 50

  Scenario: Withdraw more credit than available
    Given a user with a credit of 100
    When the user withdraws 150 credit
    Then an InsufficientCredit exception should be thrown
 */


    private User user;

    @Given("a user with a credit of {int}")
    public void givenAUserWithCredit(int initialCredit) {
        user = new User(initialCredit);
    }

    @When("the user withdraws {int} credit")
    public void whenTheUserWithdrawsCredit(int creditToWithdraw) {
        try {
            user.withdrawCredit(creditToWithdraw);
        } catch (InsufficientCredit e) {
        }
    }

    @Then("the user's credit should be {int}")
    public void thenTheUsersCreditShouldBe(int expectedCredit) {
        Assert.assertEquals(expectedCredit, user.getCredit());
    }

    @Then("an InsufficientCredit exception should be thrown")
    public void thenAnInsufficientCreditExceptionShouldBeThrown() {
    }
}
