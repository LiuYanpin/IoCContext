import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GreetingTest {
    @Test
    void should_get_hello_world() {
        String actual = new Greeting().greet();
        assertTrue(actual.equals("Hello world!"));
    }
}