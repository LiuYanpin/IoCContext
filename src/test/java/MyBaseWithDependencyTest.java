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
        MyDependency baseDependency = myBaseInstance.getDependency();
        assertEquals(MyDependency.class, baseDependency.getClass());
        assertNotNull(baseDependency);
    }
}