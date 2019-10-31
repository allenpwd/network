package pwd.allen.handler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * @author 门那粒沙
 * @create 2019-10-27 18:25
 **/
public class HelloHandler extends SimpleChannelHandler {

    /**
     * 接收消息
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String msg = null;

        if (ChannelBuffer.class.isInstance(e.getMessage())) {
            ChannelBuffer message = (ChannelBuffer) e.getMessage();
            msg = new String(message.array());
        } else if (e.getMessage() instanceof String) {
            //管道加上StringDecoder后，e.getMessage()返回的是String类型
            msg = (String) e.getMessage();
        }

        System.out.println("messageReceived：" + msg);

        //回写数据
        //管道加上StringEncoder后能直接写string类型，会报错unsupported message type: class java.lang.String
//        ChannelBuffer copiedBuffer = ChannelBuffers.copiedBuffer("hi".getBytes());
//        ctx.getChannel().write(copiedBuffer);
        if ("write".equals(msg)) {
            ctx.getChannel().write("hello!你好！");
        }

        super.messageReceived(ctx, e);
    }

    /**
     * 捕获异常
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");

        super.exceptionCaught(ctx, e);
    }

    /**
     * 有新连接
     *
     * 用途：一般用来检测IP是否为黑名单
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected");

        super.channelConnected(ctx, e);
    }

    /**
     * 关闭连接，必须是连接已经建立，关闭通道时才会触发
     *
     * 用途：可以在用户断线的时候清除缓存数据
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");

        super.channelDisconnected(ctx, e);
    }

    /**
     * 关闭连接时触发
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");

        super.channelClosed(ctx, e);
    }
}
