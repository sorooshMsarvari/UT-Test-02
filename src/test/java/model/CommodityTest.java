package model;

import exceptions.NotInStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommodityTest {

    private Commodity aCommodity;
    final float INIT_RATE = 10.0F;

    @BeforeEach
    void setup() {
        aCommodity = new Commodity();
        aCommodity.setInStock(50);
        aCommodity.setInitRate(INIT_RATE);
    }

    @Test
    void testIncreaseInStock() throws NotInStock {
        aCommodity.updateInStock(40);
        assertThat(aCommodity.getInStock()).isEqualTo(90);
    }

    @Test
    void testDecreaseInStock() throws NotInStock {
        aCommodity.updateInStock(-30);
        assertThat(aCommodity.getInStock()).isEqualTo(20);
    }

    @Test
    void testDecreaseInStockThrowsException() throws NotInStock {
        assertThrows(
            NotInStock.class, ()-> {
                aCommodity.updateInStock(-51);
            }
        );
    }

    @Test
    void testAddRateSavesUserScore() {
        aCommodity.addRate("folani", 10);
        assertThat(aCommodity.getUserRate()).hasSize(1);
    }

    @Test
    void testRatingIsCorrectlyCalculated() {
        aCommodity.addRate("folani", 10);
        aCommodity.addRate("folani2",40);
        assertThat(aCommodity.getRating()).isEqualTo(20.0F);
    }
}