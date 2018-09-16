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

    @Test
    void should_get_two_dependency_if_have_two_dependency() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBaseWithTwoDependency.class);
        context.registerBean(MyDependency.class);
        MyBaseWithTwoDependency twoDependencyInstance = context.getBean(MyBaseWithTwoDependency.class);
        MyDependency firstDependency = twoDependencyInstance.getFirstDependency();
        MyDependency secondDependency = twoDependencyInstance.getSecondDependency();
        assertEquals(MyDependency.class, firstDependency.getClass());
        assertEquals(MyDependency.class, secondDependency.getClass());
        assertNotSame(firstDependency, secondDependency);
    }

    @Test
    void should_get_dependency_if_dependency_inherit_from_super() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MySuperClassWithDependency.class, MySubClassWithDependency.class);
        context.registerBean(MyDependency.class);
        MySubClassWithDependency instance = context.getBean(MySubClassWithDependency.class);
        assertEquals(MySubClassWithDependency.class, instance.getClass());
    }

    @Test
    void should_get_ordered_dependency_if_have_dependency_inherit_form_super() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MySuperClassWithDependency.class, MySubClassWithDependency.class);
        context.registerBean(MyDependency.class);
        MySubClassWithDependency instance = context.getBean(MySubClassWithDependency.class);
        assertEquals(MySubClassWithDependency.class, instance.getClass());
        assertArrayEquals(
                new String[]{"superClassDependency", "subClassDependency"},
                ((IoCContextImpl) context).getAllDependencyByClass(MySubClassWithDependency.class)
        );
    }

    @Test
    void should_throw_exception_if_not_register() throws InstantiationException, IllegalAccessException {
        IoCContext context= new IoCContextImpl();
        context.registerBean(MySubClassWithDependency.class);
        try {
            context.getBean(MyDependency.class);
        }catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    @Test
    void should_throw_exception_if_register_null_class() {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(null);
        }catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }
}