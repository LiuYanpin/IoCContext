public class MyBeanInterfaceCooler implements MyBeanInterface{
    public MyBeanInterfaceCooler() {
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
