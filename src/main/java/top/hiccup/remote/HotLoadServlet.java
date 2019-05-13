package top.hiccup.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 热加载的Servlet
 *
 * @author wenhy
 * @date 2019/5/7
 */
@MultipartConfig
public class HotLoadServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotLoadServlet.class);

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        if (requestURI != null && requestURI.startsWith(ServletConst.BASE_PATH)) {
            String path = requestURI.substring(ServletConst.BASE_PATH.length());
            if ("/execute".equals(path)) {
                // 判断request的请求方式是否为post并且contentType是否以multipart/开头
                if (!ServletFileUpload.isMultipartContent(request)) {
                    return;
                }
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
                try {
                    for (FileItem item : servletFileUpload.parseRequest(request)) {
                        if ("file".equals(item.getFieldName())) {
                            byte[] bytes = item.get();
                            Object result = ByteCodeExecuter.execute(bytes);
                            response.getWriter().write(String.valueOf(result));
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("deal bytecode error", e);
                }
            } else {
                returnResourceFile(path, response);
            }
        }
    }


    protected void returnResourceFile(String fileName, HttpServletResponse response) throws IOException {
        String filePath = "web/" + fileName;
        if (filePath.endsWith(".html")) {
            response.setContentType("text/html; charset=utf-8");
        } else if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        // 这里直接读取二进制，省去JVM对字符的编码
        response.getOutputStream().write(readBytesFromResource(filePath));
    }

    private static byte[] readBytesFromResource(String resource) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            if (in == null) {
                in = HotLoadServlet.class.getResourceAsStream(resource);
            }
            if (in == null) {
                return "404".getBytes();
            }
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024 * 4];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
            byte[] ret = out.toByteArray();
            return ret;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                LOGGER.debug("close stream error", e);
            }
        }
    }
}


