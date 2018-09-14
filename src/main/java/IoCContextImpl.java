import com.sun.prism.es2.ES2Graphics;

import java.util.HashMap;
import java.util.HashSet;

public class IoCContextImpl implements IoCContext {
    private HashSet<Class> currentBeanSet = new HashSet<>();
    private HashMap<String, Class> currentBeanMap = new HashMap<>();
    private boolean ifStartGetBean = false;
    @Override
    public void registerBean(Class<?> beanClazz) {
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
        currentBeanMap.putIfAbsent(beanClazz.getName(), beanClazz);

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
            instance = (T) currentBeanMap.get(resolveClazz.getName()).newInstance();
        }catch (Exception e) {
            throw e;
        }
        return instance;
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {
        this.registerBean(resolveClazz);
        this.registerBean(beanClazz);
        currentBeanMap.put(resolveClazz.getName(), beanClazz);
    }

    private <T> void removeBean(Class<T> toBeRemoveClass) {
        currentBeanSet.remove(toBeRemoveClass);
    }
}
