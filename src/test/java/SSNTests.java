import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SSNTests {
    @Test
    void isSSNValid_shortNoDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isSSNValid("1412062380"));
    }

    @Test
    void isSSNValid_shortDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isSSNValid("141206+2380"));
        Assertions.assertTrue(v.isSSNValid("141206-2380"));
    }

    @Test
    void isSSNValid_longNoDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isSSNValid("191412062380"));
    }

    @Test
    void isSSNValid_longDiv_returnsTrue(){
        Validator v = new Validator();
        Assertions.assertTrue(v.isSSNValid("19141206-2380"));
    }

    @Test
    void isSSNValid_nonBasicFormat_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isSSNValid("1914a20s-2380afs"));
    }

    @Test
    void isSSNValid_invalidDate_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isSSNValid("21141206-2380"));
    }

    @Test
    void isSSNValid_incorrectControlDigit_returnsFalse(){
        Validator v = new Validator();
        Assertions.assertFalse(v.isSSNValid("19141206-2381"));
    }

}

