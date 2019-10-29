package pwd.allen.netty;

import pwd.allen.netty.handler.HelloHandler;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 门那粒沙
 * @create 2019-10-27 18:18
 **/
public class Client {

    public static void main(String[] args) {

        //服务类
        ClientBootstrap bootstrap = new ClientBootstrap();

        ExecutorService bossExecutor = Executors.newCachedThreadPool();
        ExecutorService workerExecutor = Executors.newCachedThreadPool();

        //设置niosocket工厂
        bootstrap.setFactory(new NioClientSocketChannelFactory(bossExecutor, workerExecutor));

        //设置管道的工厂
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();

                //加上接收到消息时的解码工具
                pipeline.addLast("decoder", new StringDecoder(Server.charset));
                //加上发送消息时的编码工具
                pipeline.addLast("encoder", new StringEncoder(Server.charset));
                //自定义处理类，处理连接建立与断开、消息接收与发送、异常处理等事件
                pipeline.addLast("hello", new HelloHandler());
                return pipeline;
            }
        });

        //连接服务端
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 10100));

        System.out.println("连接服务端!!!");

        //这个channel和helloHandler的ChannelHandlerContext.getChannel()是同一个对象
        Channel channel = channelFuture.getChannel();

        //向服务端输出控制台输入的内容

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入：");
            channel.write(scanner.next());
        }

    }
}
