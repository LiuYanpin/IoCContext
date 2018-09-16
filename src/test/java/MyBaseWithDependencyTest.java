import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyBaseWithDependencyTest {
    @Test
    void should_create_class_with_dependency() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyDependency.class);
        context.registerBean(MyBaseWithDependency.class);
        MyBaseWithDependency myBaseInstance = context.getBean(MyBaseWithDependency.class);
        assertEquals(MyBaseWithDependency.class, myBaseInstance.getClass());
    }

    @Test
    void should_create_class_and_its_dependency() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBaseWithDependency.class);
        context.registerBean(MyDependency.class);
        MyBaseWithDependency myBaseInstance = context.getBean(MyBaseWithDependency.class);
        assertEquals(MyBaseWithDependency.class, myBaseInstance.getClass());
        MyDependency baseDependency = myBaseInstance.getDependency();
        assertEquals(MyDependency.class, baseDependency.getClass());
        assertNotNull(baseDependency);
    }

    @Test
    void should_throw_exception_if_not_register_dependency() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBaseWithDependency.class);
        try {
            context.getBean(MyBaseWithDependency.class);
        }catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    @Test
    void should_throw_exception_if_not_register_class_with_dependency() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyDependency.class);
        try {
            context.getBean(MyBaseWithDependency.class);
        }catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }
}