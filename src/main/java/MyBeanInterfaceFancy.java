public class MyBeanInterfaceFancy implements MyBeanInterface{
    public MyBeanInterfaceFancy() {
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
