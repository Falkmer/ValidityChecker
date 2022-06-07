import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrgNumTests {
    @Test
    void isOrgNumValid_shortNoDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isOrgNumValid("5566143185"));
    }

    @Test
    void isOrgNumValid_shortDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isOrgNumValid("556614-3185"));
    }

    @Test
    void isOrgNumValid_longNoDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isOrgNumValid("16556614-3185"));
    }

    @Test
    void isOrgNumValid_longDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isOrgNumValid("16556614-3185"));
    }

    @Test
    void isOrgNumValid_nonBasicFormat_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isOrgNumValid("55s614-3185a"));
    }

    @Test
    void isOrgNumValid_invalidOrgNumSemantics_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isOrgNumValid("551614-3185"));
        Assertions.assertFalse(v.isOrgNumValid("15551614-3185"));
    }

    @Test
    void isOrgNumValid_incorrectControlDigit_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isOrgNumValid("19091079-9826"));
    }
}
