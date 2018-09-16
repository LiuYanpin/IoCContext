public class MySubClassWithDependency extends MySuperClassWithDependency{
    @CreateOnTheFly
    private MyDependency subClassDependency;

    public MySubClassWithDependency() {
    }

    public MySubClassWithDependency(MyDependency subClassDependency) {
        this.subClassDependency = subClassDependency;
    }

}
