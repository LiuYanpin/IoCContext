import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    @Test
    void should_throw_no_default_constructor() {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(MyBean.class);
        }catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("MyBean has no constructor.", e.getMessage());
        }
    }

    @Test
    void should_get_void_if_dulplicate_bean() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(String.class);
            context.registerBean(String.class);
        }catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    void should_not_get_unregister_clazz() {
        IoCContext context = new IoCContextImpl();
        try {
            context.getBean(String.class);
        }catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    void should_get_exception_if_construtor_throw_exception() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(ConstructorWithExceptionClass.class);
        try {
            context.getBean(ConstructorWithExceptionClass.class);
        }catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("ConstructorWithExceptionClass", e.getMessage());
        }
    }

    @Test
    void should_not_register_bean_if_start_get_bean() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(String.class);
        context.getBean(String.class);
        try {
            context.registerBean(ArrayList.class);
        }catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }
}