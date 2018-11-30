import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {
	private static final int BUFFER_SIZE = 1024;
	OutputStream output;
	Request request;
	DataOutputStream sout;   //字节流
	
	public Response(OutputStream output,Request request) {
		this.output=output;
		this.request=request;
		sout=new DataOutputStream(output);
	}
	
	public void sendResource() {
		FileInputStream fis =null;
		try{
			File file = new File(HttpServer.FileRoot, request.getUri());
			System.out.println(request.getUri());
			if(file.exists()&&!request.getUri().equals("/")) {  //之所以加上一个"/"判断，是因为在默认情况下，及只输入127.0.0.1且没有Index.html的情况下，会返回200 OK状态字
				String line="HTTP/1.1 200 OK \r\n";
				 System.out.print(line);
				 sout.write(line.getBytes());
				 String header="Content-Type: "+ contentType(request.getUri()) +"; charset=utf-8 \r\n"
              		 	  +"Content-length: "+file.length()+" \r\n\r\n";
                 System.out.print(header);             
                 sout.writeBytes(header);
                 
                 fis = new FileInputStream(file); 
                 
                 byte[] bytes = new byte[BUFFER_SIZE];
                 int ch = fis.read(bytes, 0, BUFFER_SIZE);                                      
                 while (ch!=-1) { //ch==-1表示读到末尾了
                	 sout.write(bytes, 0, ch);
                     ch = fis.read(bytes, 0, BUFFER_SIZE);
                 }
                 sout.close();
                    
			}
			else {
				 String errorMessage = "HTTP/1.1 404 File Not Found \r\n" 
    		 			 + "Content-Type: text/html \r\n" 
    		 			 + "Content-Length: 23 \r\n" 
    		 			 + "\r\n" 
    		 			 + "<h1>File Not Found</h1>";
				 System.out.println(errorMessage);
				 output.write(errorMessage.getBytes());
			}
			
		}
		catch (Exception e) {
			 e.printStackTrace();
       }
		finally {
            if (fis!=null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
       }
	}


	//文件后缀识别
	private static String contentType(String fileName) {
		 
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")|| fileName.endsWith(".txt")) {
               return "text/html";
        }
        if(fileName.endsWith(".jpg")) {
        	return "image/jpeg";
        }
        if(fileName.endsWith(".gif")) {
        	return "image/gif";
        }
        return "application/octet-stream";
 
    }
}
