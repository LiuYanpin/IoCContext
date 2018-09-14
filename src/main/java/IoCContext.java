public interface IoCContext {
    void registerBean(Class<?> beanClazz) throws IllegalAccessException, InstantiationException;
    <T> T getBean(Class<T> resolveClazz) throws Exception;
    <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz);
}
