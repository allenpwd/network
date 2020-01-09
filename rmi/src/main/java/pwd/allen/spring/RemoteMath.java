package pwd.allen.spring;

/**
 * @author lenovo
 * @create 2020-01-09 11:13
 **/
public class RemoteMath implements IRemoteMath {

    private int numberOfComputations;

    protected RemoteMath() {
        numberOfComputations = 0;
    }

    public double add(double a, double b) {
        numberOfComputations++;
        System.out.println("Number of computations performed so far = "
                + numberOfComputations);
        return (a+b);
    }

    public double subtract(double a, double b) {
        numberOfComputations++;
        System.out.println("Number of computations performed so far = "
                + numberOfComputations);
        return (a-b);
    }
}
