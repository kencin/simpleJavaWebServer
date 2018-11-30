import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable{
	
	public static final int port = 8080; //默认监控端口
	public static final String FileRoot = System.getProperty("user.dir");  //默认文件目录
	
	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		Thread thread = new Thread(server);
		thread.start();
	}

	//多线程
	 public void run() {
			try {
			    	await();
				} catch (Exception e) {
					System.out.println(e);
				}
		    }

	
	@SuppressWarnings("resource")
	public void await() {
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1")); //监听地址
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//
		while(true) {
			Socket socket = null;
            InputStream input = null;
            OutputStream output = null;
            
            try {
            	socket = serverSocket.accept(); //打开一个套接字
            	input = socket.getInputStream(); //获得套接字输入流
                output = socket.getOutputStream(); //获得套接字输出流
                Request request = new Request(input);
                request.parse();
                Response response = new Response(output,request);
                response.sendResource();
                socket.close();
            }
            catch (IOException e) {
    			e.printStackTrace();
                System.exit(1);
    		}
		}
		
	}
}
