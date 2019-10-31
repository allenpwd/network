package pwd.allen.nio;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 演示NIO通信
 *
 * 特点：底层是IO多路复用，用一个线程就能监控通信
 *
 * 问题：客户端关闭会抛异常死循环
 * 处理：channel.read返回的如果不大于0则关闭key
 *
 * 一般不会去注册SelectionKey.OP_WRITE，因为它表示底层缓冲区是否有空间，一般缓冲区都是空闲的，所以这个事件会一直接收到
 *
 * @author 门那粒沙
 * @create 2019-10-20 23:33
 **/
public class Server {

    //通道管理器
    private Selector selector;

    //编码解码的字符集
    private Charset charset = Charset.forName("UTF-8");

    /**
     * 获得一个ServerSocket通道，并对该通道做一些初始化的工作
     * @param port  绑定的端口号
     * @throws IOException
     */
    public void initServer(int port) throws IOException {
        //获取一个ServerSocket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置通道为非阻塞
        serverSocketChannel.configureBlocking(false);
        //将该通道对应的ServerSocket绑定到port端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //获取一个通道管理器
        this.selector = Selector.open();
        //将通道管理器和该通道绑定，并为钙通道注册SelectionKey.OP_ACCEPT事件，注册该事件后
        //当该事件到达时，selector.select()会返回，否则会一直阻塞
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void listen() throws IOException {
        System.out.println("服务端启动成功！");
        // 轮询访问selector
        while (true) {
            //当注册的事件到达时，方法返回；否则,该方法会一直阻塞
            selector.select();


            //当注册的事件到达时，方法返回；超过1秒也会返回，返回0
//            selector.select(1000);
            //唤醒select，使当前阻塞的线程立刻获取返回值
//            selector.wakeup();

            // 获得selector中选中的项的迭代器，选中的项为注册的事件
            Iterator ite = this.selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();
                // 删除已选的key,以防重复处理
                ite.remove();

                //这里不能异步处理，否则可能selector.select()时请求还没处理，然后又返回一次handlerAccept，而server.accept只能接收成功一次，第二次返回null
                handler(key);
            }
        }
    }

    /**
     * 处理请求
     * @param key
     */
    public void handler(SelectionKey key) throws IOException {
        //客户端请求连接 事件
        if (key.isAcceptable()) {
            handlerAccept(key);
        } else if (key.isReadable()) {//可读事件
            handlerRead(key);
        }
    }

    /**
     * 处理连接请求
     * @param key
     */
    public void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        // 获得和客户端连接的通道
        SocketChannel channel = server.accept();

        // 设置成非阻塞
        channel.configureBlocking(false);

        System.out.println("接收到新客户端：" + channel.getRemoteAddress());

        //在这里可以给客户端发送信息哦
        channel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes(Charset.forName("GBK"))));
        //在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
        channel.register(this.selector, SelectionKey.OP_READ);
    }

    /**
     * 处理读的事件
     * @param key
     * @throws IOException
     */
    public void handlerRead(SelectionKey key) throws IOException {
        // 服务器可读取消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        // 创建读取的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);

        StringBuilder sb = new StringBuilder();

        int read = channel.read(buffer);
        //如果客户端关闭，会一直有读事件，但read=0，所以要做判断
        if (read <= 0) {
            System.out.println("客户端关闭");
            key.cancel();
            if (key.channel() != null) key.channel().close();
            return;
        }

        do {
            buffer.flip();
            sb.append(charset.decode(buffer));
            buffer.clear();
        } while (channel.read(buffer) > 0);
        System.out.println("服务端收到信息：" + sb.toString());

        if ("write".equals(sb.toString())) {
            String msg = String.format("你连接的是：%s", Inet4Address.getLocalHost().getHostAddress());
//            channel.write(charset.encode(msg));
            ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes("GBK"));
            channel.write(outBuffer);
        }

    }

    /**
     * 启动服务端测试
     *
     * 用cmd测试
     * telnet localhost 8000        //连接到8000端口
     * ctrl + ]                     //进入telnet命令界面
     * >sen hello world             //输出"hello world"
     * >c                           //关闭连接
     * >q                           //退出
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.initServer(8000);
        server.listen();
    }
}
