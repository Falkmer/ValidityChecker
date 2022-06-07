import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordNumTests {
    @Test
    void isCoordNumValid_shortNoDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isCoordNumValid("0910799824"));
    }

    @Test
    void isCoordNumValid_shortDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isCoordNumValid("091079+9824"));
        Assertions.assertTrue(v.isCoordNumValid("091079-9824"));
    }

    @Test
    void isCoordNumValid_longNoDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isCoordNumValid("190910799824"));
    }

    @Test
    void isCoordNumValid_longDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isCoordNumValid("19091079-9824"));
    }

    @Test
    void isCoordNumValid_nonBasicFormat_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isCoordNumValid("19ab1079-9824a"));
    }

    @Test
    void isCoordNumValid_invalidDate_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isCoordNumValid("19091039-9824"));
    }

    @Test
    void isCoordNumValid_incorrectControlDigit_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isCoordNumValid("19091079-9825"));
    }
}
