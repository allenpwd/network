package pwd.allen.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * @author lenovo
 * @create 2020-01-09 11:11
 **/
@Configuration
public class RMIServerConfig {

    @Value("myMath")
    private String serviceName;

    @Bean
    public IRemoteMath remoteMath() {
        return new RemoteMath();
    }

    /**
     * 建立RMI服务
     * @return
     */
    @Bean
    public RmiServiceExporter getRmiServiceExporter(IRemoteMath remoteMath) {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName(serviceName);
        rmiServiceExporter.setService(remoteMath);
        rmiServiceExporter.setServiceInterface(IRemoteMath.class);
        rmiServiceExporter.setRegistryPort(2002);
        return rmiServiceExporter;
    }
}
