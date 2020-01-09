package pwd.allen.jdk;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * 远程接口实现，在RMI服务端创建并注册到RMI注册中心
 *

 * 远程对象必须实现 {@link UnicastRemoteObject} 类，这样才能保证客户端访问获得远程对象时，
 * 该远程对象将会把自身的一个拷贝以Socket的形式传输给客户端，此时客户端所获得的这个拷贝称为“存根”，
 * 而服务器端本身已存在的远程对象则称之为“骨架”。其实此时的存根是客户端的一个代理，用于与服务器端的通信，
 * 而骨架也可认为是服务器端的一个代理，用于接收客户端的请求之后调用远程方法来响应客户端的请求。
 *
 */
public class RemoteMath extends UnicastRemoteObject implements IRemoteMath {

    private int numberOfComputations;

    protected RemoteMath() throws RemoteException {
        numberOfComputations = 0;
    }

    public double add(double a, double b) throws RemoteException {
        numberOfComputations++;
        System.out.println("Number of computations performed so far = "
                + numberOfComputations);
        return (a+b);
    }

    public double subtract(double a, double b) throws RemoteException {
        numberOfComputations++;
        System.out.println("Number of computations performed so far = "
                + numberOfComputations);
        return (a-b);
    }

}