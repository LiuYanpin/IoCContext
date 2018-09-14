import java.util.HashSet;

public class IoCContextImpl implements IoCContext {
    private HashSet<Class> currentHashSet = new HashSet<>();
    @Override
    public void registerBean(Class<?> beanClazz) throws IllegalAccessException, InstantiationException {
        if (beanClazz == null) {
            throw new IllegalArgumentException("beanClazz is mandatory");
        }
        try {
            beanClazz.newInstance();
        }catch (Exception e) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract.");
        }
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        return null;
    }
}
