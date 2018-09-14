import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class IoCContextImplTest {

    @Test
    void should_throw_null_message() {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(null);
        }catch (Exception  e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("beanClazz is mandatory", e.getMessage());
        }
    }

    @Test
    void should_throw_abstract_message() {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(Iterator.class);
        }catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("java.util.Iterator is abstract.", e.getMessage());
        }
    }
}