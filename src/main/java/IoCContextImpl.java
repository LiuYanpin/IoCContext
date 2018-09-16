import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

import static java.lang.Class.forName;

public class IoCContextImpl implements IoCContext {
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
        try {
            beanClazz.newInstance();
        }catch (Exception e) {
            throw new IllegalArgumentException(beanClazz.getName() + "has no default constructor.");
        }
        if (beanClazz.getDeclaredConstructors().length == 0) {
            throw new IllegalArgumentException(beanClazz.getName() + "has no default constructor.");
        }
        if (currentBeanMap.containsKey(beanClazz.getName())) {
            return;
        }
        currentBeanMap.put(beanClazz.getName(), beanClazz);

    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) throws Exception {
        ifStartGetBean = true;
        T instance;
        if (resolveClazz == null) {
            throw new IllegalArgumentException();
        }
        if (!currentBeanMap.containsKey(resolveClazz.getName())) {
            throw new IllegalStateException();
        }

        try {
            Class<T> currentClass = currentBeanMap.get(resolveClazz.getName());
            Constructor constructorWithParameter = getConstructorWithParameter(currentClass);
            constructorWithParameter.setAccessible(true);
            Object dependency = null;
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field: fields) {
                field.setAccessible(true);
                if (field.getAnnotation(CreateOnTheFly.class) != null) {
                    dependency = Class.forName(field.getGenericType().getTypeName()).newInstance();
                }
            }
            instance = (T) constructorWithParameter.newInstance(dependency);


        }catch (Exception e) {
            throw e;
        }
        return instance;
    }

    private <T> Constructor getConstructorWithParameter(Class<T> currentClass) {
        Constructor<?>[] constructors= currentClass.getDeclaredConstructors();
        Constructor constructorWithParameter = null;
        for (int constructorIndex = 0; constructorIndex < constructors.length; constructorIndex++) {
            constructorWithParameter = constructors[constructorIndex];
            if (constructorWithParameter.getParameterCount() != 0) {
                break;
            }
        }
        return constructorWithParameter;
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) {
        this.registerBean(beanClazz);
        currentBeanMap.put(resolveClazz.getName(), beanClazz);
    }

}
