package pwd.allen.oio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示ServerSocket建立连接
 *
 * 特点：阻塞，不适合长连接，因为一个连接占用一个县城
 *
 * @author 门那粒沙
 * @create 2019-10-20 21:35
 **/
public class Server {

    public static void main(String[] args) throws IOException {

        ExecutorService threadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(1000);
        System.out.println("服务器启动！");
        while(true) {
            //获取一个套接字（阻塞）
            final Socket socket = serverSocket.accept();
            System.out.println("接受到一个新连接：" + socket.getRemoteSocketAddress());

            //使用线程池去处理建立连接后的套接字，使主线程能继续等待接收新连接
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //业务处理
                    handler(socket);
                }
            });
        }
    }

    /**
     * 读取数据
     * @param socket
     * @throws IOException
     */
    public static void handler(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()));

            String line = null;

            //readLine读取输入流直到遇到换行
            while((line = br.readLine()) != null) {
                System.out.println(line);
                if ("write".equals(line)) {
                    socket.getOutputStream().write("hello".getBytes());
                }
            }

            //实时获取字节流，直到流末尾
//            while (true) {
//                byte[] bytes = new byte[1024];
//                int read = is.read(bytes);
//                if (read != -1) {
//                    line = new String(bytes, 0, read, "GBK");
//                    System.out.println(line);
//                } else {
//                    break;
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
