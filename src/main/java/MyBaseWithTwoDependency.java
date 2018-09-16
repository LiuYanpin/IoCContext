public class MyBaseWithTwoDependency {
    @CreateOnTheFly
    MyDependency firstDependency;
    @CreateOnTheFly
    MyDependency secondDependency;

    public MyDependency getFirstDependency() {
        return firstDependency;
    }

    public MyDependency getSecondDependency() {
        return secondDependency;
    }

    public MyBaseWithTwoDependency() {
    }

    public MyBaseWithTwoDependency(MyDependency firstDependency, MyDependency secondDependency) {
        this.firstDependency = firstDependency;
        this.secondDependency = secondDependency;
    }
}
