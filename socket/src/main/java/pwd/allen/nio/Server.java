package pwd.allen.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author 门那粒沙
 * @create 2019-10-20 23:33
 **/
public class Server {

    //通道管理器
    private Selector selector;

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

    public void listen() throws IOException {
        while(true) {
            //当事件到达时，selector.select()会返回，否则会一直阻塞
            selector.select();
            //获取selector中选中的项的迭代器，选中的项为注册的事件
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //删除已选的key，以防止重复chuli
                iterator.remove();

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

        }
    }

    /**
     * 处理连接请求
     * @param key
     */
    public void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel)key.channel();
        //获取和客户端连接的通道
        SocketChannel channel = server.accept();
    }
}
