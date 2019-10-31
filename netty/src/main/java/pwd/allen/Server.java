package pwd.allen;

import pwd.allen.handler.HelloHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 门那粒沙
 * @create 2019-10-27 18:18
 **/
public class Server {

    public static Charset charset = Charset.forName("GBK");

    public static void main(String[] args) {

        //服务类
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        ExecutorService bossExecutor = Executors.newCachedThreadPool();
        ExecutorService workerExecutor = Executors.newCachedThreadPool();

        //设置niosocket工厂
        serverBootstrap.setFactory(new NioServerSocketChannelFactory(bossExecutor, workerExecutor));

        //设置管道的工厂
        serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                //加上这个后
                pipeline.addLast("decoder", new StringDecoder(charset));
                pipeline.addLast("encoder", new StringEncoder(charset));
                pipeline.addLast("hello", new HelloHandler());
                return pipeline;
            }
        });

        //绑定端口
        serverBootstrap.bind(new InetSocketAddress(10100));

        System.out.println("服务端开启!!!");

    }
}
