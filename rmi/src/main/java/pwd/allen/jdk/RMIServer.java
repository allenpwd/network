package pwd.allen.jdk;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * 注册远程对象,向客户端提供远程对象服务。
 * 远程对象是在远程服务上创建的，你无法确切地知道远程服务器上的对象的名称，
 * 但是,将远程对象注册到RMI Registry之后,
 * 客户端就可以通过RMI Registry请求到该远程服务对象的stub，
 * 利用stub代理就可以访问远程服务对象了。
 *
 * main函数执行完成后退出，只剩下rmi服务继续运行
 *
 * @author lenovo
 * @create 2020-01-09 10:43
 **/
public class RMIServer {
    public static void main(String[] args)  {

        try {

            IRemoteMath remoteMath = new RemoteMath();
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Compute", remoteMath);

            // 如果不想再让该对象被继续调用，使用下面一行
//             UnicastRemoteObject.unexportObject(remoteMath, false);

            String[] list = registry.list();
            System.out.println("Math server ready，注册的服务：" + Arrays.toString(list));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
