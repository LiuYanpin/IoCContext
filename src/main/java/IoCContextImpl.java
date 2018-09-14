import com.sun.prism.es2.ES2Graphics;

import java.util.HashSet;

public class IoCContextImpl implements IoCContext {
    private HashSet<Class> currentBeanSet = new HashSet<>();
    private boolean ifStartGetBean = false;
    @Override
    public void registerBean(Class<?> beanClazz) throws IllegalAccessException, InstantiationException {
        if (ifStartGetBean) {
            throw new IllegalStateException();
        }
        if (beanClazz == null) {
            throw new IllegalArgumentException("beanClazz is mandatory");
        }
        if (beanClazz.isInterface()) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract.");
        }

        if (beanClazz.getDeclaredConstructors().length == 0) {
            throw new IllegalArgumentException(beanClazz.getName() + "has no default constructor.");
        }
        if (currentBeanSet.contains(beanClazz)) {
            return;
        }
        currentBeanSet.add(beanClazz);

    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws Exception {
        ifStartGetBean = true;
        T instance;
        if (resolveClazz == null) {
            throw new IllegalArgumentException();
        }
        if (!currentBeanSet.contains(resolveClazz)) {
            throw new IllegalStateException();
        }
        try {
            instance = resolveClazz.newInstance();
        }catch (Exception e) {
            throw e;
        }
        return instance;
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {

    }
}
