package pwd.allen.spring;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author lenovo
 * @create 2020-01-09 11:33
 **/
public class RMITest {

    /**
     * 启动RMI服务端，注册myMath服务
     */
    @Test
    public void startServer() {
        GenericApplicationContext applicationContext = new AnnotationConfigApplicationContext(RMIServerConfig.class);
        System.out.println("RMI服务端启动");
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * 调用RMI服务端的myMath服务abc
     */
    @Test
    public void startClient() {
        GenericApplicationContext applicationContext = new AnnotationConfigApplicationContext(RMIClientConfig.class);
        IRemoteMath remoteMath = applicationContext.getBean(IRemoteMath.class);
        System.out.println("add结果：" + remoteMath.add(12.5d, 13.6d));
        System.out.println("subtract结果：" + remoteMath.subtract(22.5d, 13.6d));
    }
}
