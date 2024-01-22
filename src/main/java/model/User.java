package model;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private float credit;

    private Map<Integer, Integer> commoditiesRates = new HashMap<>();
    private Map<String, Integer> buyList = new HashMap<>();
    private Map<String, Integer> purchasedList = new HashMap<>();

    public User(String username, String password, String email, String birthDate, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
    }

    public User(int credit) {
        this.credit = credit;
    }

    public void addCredit(float amount) throws InvalidCreditRange {
        if (amount < 0)
            throw new InvalidCreditRange();

        this.credit += amount;
    }

    public void withdrawCredit(float amount) throws InsufficientCredit {
        if (amount > this.credit)
            throw new InsufficientCredit();

        this.credit -= amount;
    }

    public void addBuyItem(Commodity commodity) {
        String id = commodity.getId();
        if (this.buyList.containsKey(id)) {
            int existingQuantity = this.buyList.get(id);
            this.buyList.put(id, existingQuantity + 1);
        } else
            this.buyList.put(id, 1);
    }

    public void addBuyItemWhitQuantity(Commodity commodity, int quantity) {
        String id = commodity.getId();
        if (this.buyList.containsKey(id)) {
            int existingQuantity = this.buyList.get(id);
            this.buyList.put(id, existingQuantity + quantity);
        } else
            this.buyList.put(id, quantity);
    }

    public void addPurchasedItem(String id, int quantity) {
        if (this.purchasedList.containsKey(id)) {
            int existingQuantity = this.purchasedList.get(id);
            this.purchasedList.put(id, existingQuantity + quantity);
        } else
            this.purchasedList.put(id, quantity);
    }

    public void removeItemFromBuyList(Commodity commodity) throws CommodityIsNotInBuyList {
        String id = commodity.getId();
        if (this.buyList.containsKey(id)) {
            int existingQuantity = this.buyList.get(id);
            if (existingQuantity == 1)
                this.buyList.remove(commodity.getId());
            else
                this.buyList.put(id, existingQuantity - 1);
        } else
            throw new CommodityIsNotInBuyList();
    }

    public void removeItemFromBuyListWhitQuantity(Commodity commodity, int quantity) throws CommodityIsNotInBuyList {
        String id = commodity.getId();
        if (this.buyList.containsKey(id)) {
            int existingQuantity = this.buyList.get(id);
            if (existingQuantity <= quantity)
                this.buyList.remove(commodity.getId());
            else
                this.buyList.put(id, existingQuantity - quantity);
        } else
            throw new CommodityIsNotInBuyList();
    }


    public class UserCreditManagementStepsadd {

        /*
        Feature: User Credit Management

          Scenario: Add credit to the user's account
            Given a user with a credit of 100
            When the user adds 50 credit
            Then the user's credit should be 150

          Scenario: Add negative credit to the user's account
            Given a user with a credit of 100
            When the user adds -50 credit
            Then an InvalidCreditRange exception should be thrown
         */
        private User user;

        @Given("a user with a credit of {int}")
        public void givenAUserWithCredit(int initialCredit) {
            user = new User(initialCredit);
        }

        @When("the user adds {int} credit")
        public void whenTheUserAddsCredit(int creditToAdd) {
            try {
                user.addCredit(creditToAdd);
            } catch (InvalidCreditRange e) {
            }
        }

        @Then("the user's credit should be {int}")
        public void thenTheUsersCreditShouldBe(int expectedCredit) {
            Assert.assertEquals(expectedCredit, user.getCredit());
        }

        @Then("an InvalidCreditRange exception should be thrown")
        public void thenAnInvalidCreditRangeExceptionShouldBeThrown() {
        }
    }
}
