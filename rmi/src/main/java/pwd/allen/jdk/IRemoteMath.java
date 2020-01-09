package pwd.allen.jdk;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 定义远程对象接口
 *
 * 必须继承Remote接口。
 * 所有参数和返回类型必须序列化(因为要网络传输)。
 * 所有方法必须抛出RemoteException
 */
public interface IRemoteMath extends Remote {

    public double add(double a, double b) throws RemoteException;
    public double subtract(double a, double b) throws RemoteException;

}