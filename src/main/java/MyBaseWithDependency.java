public class MyBaseWithDependency {
    public MyDependency getDependency() {
        return dependency;
    }

    public void setDependency(MyDependency dependency) {
        this.dependency = dependency;
    }

    @CreateOnTheFly
    private MyDependency dependency;

    public MyBaseWithDependency() {
    }

    public MyBaseWithDependency(MyDependency dependency) {
        this.dependency = dependency;
    }
}
