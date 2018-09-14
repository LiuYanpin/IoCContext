public interface IoCContext {
    void registerBean(Class<?> beanClazz) throws IllegalAccessException, InstantiationException;
    <T> T getBean(Class<T> resolveClazz);
}
