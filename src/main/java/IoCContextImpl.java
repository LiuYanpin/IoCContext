import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class IoCContextImpl implements IoCContext{
    private Map<String, Class> currentBeanMap = new HashMap<>();
    private Stack<Class> classStack = new Stack<>();
    private boolean ifStartGetBean = false;
    private boolean isClosed = false;
    private final Class<CreateOnTheFly> createOnTheFlyAnnotation = CreateOnTheFly.class;

    public boolean isClosed() {
        return isClosed;
    }

    public <T> String[] getAllDependencyByClass(Class<T> currentClass) {
        Field[] fields = getDependencyFields(currentClass);
        ArrayList<String> list = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            list.add(field.getName());
        }
        return list.toArray(new String[0]);
    }

    @Override
    public void registerBean(Class<?> beanClazz) throws ConstructorException {
        if (ifStartGetBean) {
            throw new IllegalStateException();
        }
        ifClazzNullOrAbstract(beanClazz);
        ifClazzHasDefaultConstructor(beanClazz);
        ifHasConstructorException(beanClazz);

        if (currentBeanMap.containsKey(beanClazz.getName())) {
            return;
        }
        currentBeanMap.put(beanClazz.getName(), beanClazz);
        classStack.push(beanClazz);
    }

    private void ifClazzNullOrAbstract(Class<?> beanClazz) {
        if (beanClazz == null) {
            throw new IllegalArgumentException("beanClazz is mandatory");
        }
        if (beanClazz.isInterface() || Modifier.isAbstract(beanClazz.getModifiers())) {
            throw new IllegalArgumentException(beanClazz.getName() + " is abstract.");
        }
    }

    private void ifClazzHasDefaultConstructor(Class<?> beanClazz) {
        Constructor<?>[] constructors = beanClazz.getDeclaredConstructors();
        boolean hasDefaultConstructor = false;
        for (Constructor constructor: constructors) {
            if (constructor.getParameterCount() == 0) {
                hasDefaultConstructor = true;
                break;
            }
        }
        if (!hasDefaultConstructor) {
            throw new IllegalArgumentException(beanClazz.getName() + " has no default constructor.");
        }
    }

    private void ifHasConstructorException(Class<?> beanClazz) throws ConstructorException {
        try {
            beanClazz.newInstance();
        }catch (Exception e) {
            throw new ConstructorException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public <T> void registerBean(Class<? super T> resolveClazz, Class<T> beanClazz) throws ConstructorException {
        this.registerBean(beanClazz);
        currentBeanMap.put(resolveClazz.getName(), beanClazz);
    }

    @Override
    public <T> T getBean(Class<T> resolveClazz) {
        ifStartGetBean = true;
        T instance = null;
        getBeanNullOrNotRegister(resolveClazz);

        try {
            Class<T> currentClass = currentBeanMap.get(resolveClazz.getName());
            instance = initialDependency(currentClass, getDependencyFields(currentClass));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private <T> void getBeanNullOrNotRegister(Class<T> resolveClazz) {
        if (resolveClazz == null) {
            throw new IllegalArgumentException();
        }
        if (!currentBeanMap.containsKey(resolveClazz.getName())) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void close() throws Exception {
        ArrayList<Exception> allException = new ArrayList<>();
        boolean throwFlag = false;
        while (!classStack.empty()) {
            Class<?> currentClass = classStack.pop();
            if (currentClass.getSuperclass().equals(AutoCloseable.class)) {
                Exception exception = handleCloseable(currentClass);
                if (exception != null) {
                    allException.add(exception);
                    throwFlag = true;
                }
            }
        }
        isClosed = true;
        if (throwFlag) {
            throw allException.get(0);
        }
    }

    private Exception handleCloseable(Class<?> closeable) {
        if (closeable != null) {
            try {
                closeable.getClass().getMethod("close").invoke(closeable);
            }catch (Exception exception) {
                return exception;
            }
        }
        return null;
    }

    private <T> T initialDependency(Class<T> currentClass, Field[] dependencyFields) throws Exception {
        T instance = currentClass.newInstance();
        for (Field field: dependencyFields) {
            field.setAccessible(true);
            field.set(instance, getBean(field.getType()));
        }
        return instance;
    }

    private <T> Field[] getDependencyFields(Class<T> currentClass) {
        ArrayList<Field> allDependency = new ArrayList<>();
        ArrayList<Field> dependency = new ArrayList<>();
        while (!currentClass.equals(Object.class)) {
            Field[] declaredFields = currentClass.getDeclaredFields();
            for (Field field: declaredFields) {
                field.setAccessible(true);
                if (field.getAnnotation(createOnTheFlyAnnotation) != null) {
                    dependency.add(field);
                }
            }
            allDependency.addAll(0, dependency);
            dependency.clear();
            currentClass = (Class<T>) currentClass.getSuperclass();
        }
        return allDependency.toArray(new Field[0]);
    }
}
