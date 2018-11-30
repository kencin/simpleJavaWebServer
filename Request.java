import java.io.IOException;
import java.io.InputStream;

public class Request{
	
	private InputStream input;
	private String uri = null;

	public Request(InputStream input) {
		this.input = input;
	}
	
	public void parse() {               //uri分析
		StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        
        try {
            i = input.read(buffer); 
       } catch (IOException e) {
            e.printStackTrace();
            i = -1;
       }
        
        for (int j=0; j<i; j++)
        {
             request.append((char) buffer[j]);
        }
        System.out.print(request.toString());
        
        uri = parseUri(request.toString());
	}
	private String parseUri(String requestString) {
		int index1, index2;
		index1 = requestString.indexOf(' ');
		//POST //examples/default.jsp HTTP/1.1
		if (index1 != -1) {
			index2 = requestString.indexOf(' ', index1 + 1); //从第一个空格开始，寻找到最后一个空格为止
			if (index2 > index1)
				return requestString.substring(index1 + 1, index2); //返回等同一个文件目录
		}
		return null;
	}

	public String getUri() {
		return uri;
	}
	
}
