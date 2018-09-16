public interface IoCContext extends AutoCloseable{
    void registerBean(Class<?> beanClazz) throws Exception;
    <T> T getBean(Class<T> resolveClazz);
    <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) throws Exception;
}
