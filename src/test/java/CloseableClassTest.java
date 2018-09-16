import org.junit.jupiter.api.Test;

import java.io.IOError;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CloseableClassTest {
    @Test
    void should_close_one_closeable_class() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyCloseableClass.class);
        context.close();
        assertTrue(((IoCContextImpl) context).isClosed());
    }

    @Test
    void should_throw_first_exception_when_close() throws ConstructorException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyCloseableClass.class);
        context.registerBean(MyCloseableClassWithException.class);
        try {
            context.close();
        }catch (Exception e) {
            assertEquals(ClassNotFoundException.class, e.getClass());
        }
    }

    @Test
    void should_throw_first_exception_if_have_two_exception() throws ConstructorException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyCloseableClass.class);
        context.registerBean(MyCloseableClassWithAnotherException.class);
        context.registerBean(MyCloseableClassWithException.class);
        try {
            context.close();
        }catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
    }

    @Test
    void should_throw_first_exception_when_have_ordinary_class_and_closeable_class() throws ConstructorException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyDependency.class);
        context.registerBean(MyCloseableClass.class);
        context.registerBean(MyCloseableClassWithException.class);
        try {
            context.close();
        }catch (Exception e) {
            assertEquals(ClassNotFoundException.class, e.getClass());
        }
    }
}
