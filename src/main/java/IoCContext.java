public interface IoCContext extends AutoCloseable{
    void registerBean(Class<?> beanClazz) throws ConstructorException;
    <T> T getBean(Class<T> resolveClazz) throws Exception;
    <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) throws ConstructorException;
}
