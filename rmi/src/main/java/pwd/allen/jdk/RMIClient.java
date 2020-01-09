package pwd.allen.jdk;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * @author lenovo
 * @create 2020-01-09 10:47
 **/
public class RMIClient {

    public static void main(String[] args) {
        try {
            //默认端口1099
            Registry registry = LocateRegistry.getRegistry("localhost");

            String[] list = registry.list();
            System.out.println("远程RMI registry的服务：" + Arrays.toString(list));

            // 从Registry中检索远程对象的存根/代理
            IRemoteMath remoteMath = (IRemoteMath)registry.lookup("Compute");

            // 调用远程对象的方法
            double addResult = remoteMath.add(5.0, 3.0);
            System.out.println("5.0 + 3.0 = " + addResult);

            double subResult = remoteMath.subtract(5.0, 3.0);
            System.out.println("5.0 - 3.0 = " + subResult);

        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
