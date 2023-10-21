package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User aUser;
    private static Commodity commodity;

    @BeforeAll
    static void setupCommodity() {
        commodity = new Commodity();
        commodity.setId("1");
    }

    @BeforeEach
    void setup() {
        aUser = new User(
                "folani",
                "folanPass",
                "folan@folan.com",
                "1380.02.27",
                "folan city"
        );
    }

    @Test
    void testAddCreditAddsCreditCorrectly() throws InvalidCreditRange {
        var initialCredit = aUser.getCredit();
        float creditAmount =  50F;
        aUser.addCredit(creditAmount);
        assertThat(aUser.getCredit()).isEqualTo(initialCredit + creditAmount);
    }

    @Test
    void testAddCreditWithNegativeAmountThrowsException() throws InvalidCreditRange {
        float creditAmount =  -50F;
        assertThrows(InvalidCreditRange.class ,()->  aUser.addCredit(creditAmount));
    }

    @Test
    void testWithdrawCredit() throws InsufficientCredit {
        aUser.setCredit(60F);
        float withdrawalAmount =  50F;
        aUser.withdrawCredit(withdrawalAmount);
        assertThat(aUser.getCredit()).isEqualTo(10F );
    }

    @Test
    void testWithdrawCreditWithInsufficientCreditThrowsException() throws InvalidCreditRange {
        aUser.setCredit(50F);
        assertThrows(InsufficientCredit.class ,()->  aUser.withdrawCredit(60F));
    }

    @Test
    void testAddNewItemWorksCorrectly() {
        aUser.addBuyItem(commodity);
        assertTrue(aUser.getBuyList().containsKey(commodity.getId()));
        assertThat(aUser.getBuyList().get(commodity.getId())).isEqualTo(1);
    }

    @Test
    void testAddExistingItemQuantityInButListIncrements() {
        aUser.addBuyItem(commodity);
        aUser.addBuyItem(commodity);
        assertThat(aUser.getBuyList().get(commodity.getId())).isEqualTo(2);
    }

    @Test
    void testNewPurchasedItemQuantityIsCorrect() {
        aUser.addPurchasedItem("1", 10);
        assertThat(aUser.getPurchasedList().size()).isEqualTo(1);
        assertThat(aUser.getPurchasedList().get("1")).isEqualTo(10);
    }

    @Test
    void testExistingPurchasedItemsQuantityIncrements() {
        aUser.addPurchasedItem("1", 10);
        aUser.addPurchasedItem("1", 20);

        assertThat(aUser.getPurchasedList().size()).isEqualTo(1);
        assertThat(aUser.getPurchasedList().get("1")).isEqualTo(30);
    }

    @Test
    void removeItemFromBuyList() throws CommodityIsNotInBuyList {
        aUser.addBuyItem(commodity);
        assertThat(aUser.getBuyList().size()).isEqualTo(1);
        aUser.removeItemFromBuyList(commodity);
        assertThat(aUser.getBuyList().size()).isEqualTo(0);
    }
    @Test
    void removeItemFromBuyLisThrowsCommodityIsNotInBuyList() throws CommodityIsNotInBuyList {
        assertThrows(CommodityIsNotInBuyList.class,()->aUser.removeItemFromBuyList(commodity));
    }

}