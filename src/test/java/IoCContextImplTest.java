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
    void should_throw_abstract_message_when_register_class() {
        IoCContext context = new IoCContextImpl();
        try {
            context.registerBean(MyAbstractClass.class);
        }catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertEquals("MyAbstract is abstract.", e.getMessage());
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
    void should_get_void_if_duplicate_bean() throws InstantiationException, IllegalAccessException {
        IoCContext context = new IoCContextImpl();
        IoCContext originContext = context;
        context.registerBean(String.class);
        IoCContext contextAfterRegisterOnce = context;
        context.registerBean(String.class);
        IoCContext contextAfterRegisterTwice = context;
        assertEquals(originContext, contextAfterRegisterOnce);
        assertEquals(originContext, contextAfterRegisterTwice);
        assertEquals(contextAfterRegisterOnce, contextAfterRegisterTwice);
    }

    @Test
    void should_not_get_unregister_clazz() {
        IoCContext context = new IoCContextImpl();
        try {
            context.getBean(String.class);
        }catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    @Test
    void should_get_exception_if_constructor_throw_exception() throws InstantiationException, IllegalAccessException {
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

    @Test
    void should_get_instance_if_get_bean() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(String.class);
        String instance = context.getBean(String.class);
        assertEquals("", instance);
    }

    @Test
    void should_get_different_instance_if_get_twice() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanWithDefaultConstructor.class);
        MyBeanWithDefaultConstructor myBean1 = context.getBean(MyBeanWithDefaultConstructor.class);
        assertTrue(MyBeanWithDefaultConstructor.class.isInstance(myBean1));
        MyBeanWithDefaultConstructor myBean2 = context.getBean(MyBeanWithDefaultConstructor.class);
        assertNotSame(myBean1, myBean2);
    }

    @Test
    void should_get_different_class_if_register_two_class() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(String.class);
        context.registerBean(ArrayList.class);
        String instanceString = context.getBean(String.class);
        ArrayList instanceArrayList = context.getBean(ArrayList.class);
        assertTrue(String.class.isInstance(instanceString));
        assertTrue(ArrayList.class.isInstance(instanceArrayList));
    }

    @Test
    void should_register_bean_by_superclass_and_subclass() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBeanBaseCooler.class);
        MyBeanBase myBeanBaseInstance = context.getBean(MyBeanBase.class);
        assertTrue(MyBeanBaseCooler.class.isInstance(myBeanBaseInstance));
    }

    @Test
    void should_register_bean_by_super_and_sub_class_and_get_later_subclass() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBeanBaseFancy.class);
        context.registerBean(MyBeanBase.class, MyBeanBaseCooler.class);
        MyBeanBase myBeanBaseInstance = context.getBean(MyBeanBase.class);
        assertTrue(MyBeanBaseCooler.class.isInstance(myBeanBaseInstance));
    }

    @Test
    void should_register_bean_by_interface_and_subclass() throws Exception{
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanInterface.class, MyBeanInterfaceFancy.class);
        MyBeanInterface myBeanInterfaceInstance = context.getBean(MyBeanInterface.class);
        assertEquals(myBeanInterfaceInstance.getClass(), MyBeanInterfaceFancy.class);
    }

    @Test
    void should_register_bean_by_interface_and_subclass_and_cover_previous() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanInterface.class, MyBeanInterfaceFancy.class);
        context.registerBean(MyBeanInterface.class, MyBeanInterfaceCooler.class);
        MyBeanInterface myBeanInterfaceInstance = context.getBean(MyBeanInterface.class);
        assertEquals(myBeanInterfaceInstance.getClass(), MyBeanInterfaceCooler.class);
    }

    @Test
    void should_get_register_bean_both_by_interface_or_superclass() throws Exception{
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBeanExtendsBaseImplementsInterface.class);
        context.registerBean(MyBeanInterface.class, MyBeanExtendsBaseImplementsInterface.class);
        MyBeanBase myBeanBaseInstance = context.getBean(MyBeanBase.class);
        MyBeanInterface myBeanInterfaceInstance = context.getBean(MyBeanInterface.class);
        assertEquals(myBeanBaseInstance.getClass(), MyBeanExtendsBaseImplementsInterface.class);
        assertEquals(myBeanInterfaceInstance.getClass(), MyBeanExtendsBaseImplementsInterface.class);
        assertNotSame(myBeanBaseInstance, myBeanInterfaceInstance);
    }

    @Test
    void should_get_instance_by_subclass() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBeanBaseCooler.class);
        MyBeanBaseCooler myBeanBaseCoolerInstance = (MyBeanBaseCooler) context.getBean(MyBeanBase.class);
        assertEquals(myBeanBaseCoolerInstance.getClass(), MyBeanBaseCooler.class);
    }

    @Test
    void should_get_instance_by_subclass_by_superclass() throws Exception {
        IoCContext context = new IoCContextImpl();
        context.registerBean(MyBeanBase.class, MyBeanBaseCooler.class);
        MyBeanBaseCooler myBeanBaseCoolerInstance = context.getBean(MyBeanBaseCooler.class);
        assertEquals(myBeanBaseCoolerInstance.getClass(), MyBeanBaseCooler.class);
    }
}