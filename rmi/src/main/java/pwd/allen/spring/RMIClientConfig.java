package pwd.allen.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * @author lenovo
 * @create 2020-01-09 11:18
 **/
@Configuration
public class RMIClientConfig {

    @Value("myMath")
    private String serviceName;

    @Bean
    public RmiProxyFactoryBean getMyMath() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://127.0.0.1:2002/" + serviceName);
        rmiProxyFactoryBean.setServiceInterface(IRemoteMath.class);
        return rmiProxyFactoryBean;
    }
}
