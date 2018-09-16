public class MySuperClassWithDependency {
    @CreateOnTheFly
    private MyDependency superClassDependency;

    public MySuperClassWithDependency() {
    }

    public MySuperClassWithDependency(MyDependency myDependency) {
        this.superClassDependency = myDependency;
    }
}
