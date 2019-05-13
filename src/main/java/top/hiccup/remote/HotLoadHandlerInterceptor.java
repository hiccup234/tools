package top.hiccup.remote;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 热加载拦截器，跟Spring配合
 *
 * @author wenhy
 * @date 2019/5/7
 */
public class HotLoadHandlerInterceptor implements HandlerInterceptor, ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(HotLoadHandlerInterceptor.class);

    private static ApplicationContext context;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServlet servlet =  HotLoadHandlerHolder.getServlet(request.getServletContext(), context);
        servlet.service(request, response);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    static class HotLoadHandlerHolder {

        private static final Logger LOGGER = LoggerFactory.getLogger(HotLoadHandlerHolder.class);

        private static volatile boolean hasInitialized = false;

        private static HttpServlet hotLoadServlet;

        private static final ReentrantLock lock = new ReentrantLock();

        public static void init(ServletContext servletContext, ApplicationContext applicationContext) {
            lock.lock();
            try {
                if (hasInitialized) {
                    return ;
                }
                LOGGER.info("HotLoadHandlerHolder initialize begin");
                long startTime = System.currentTimeMillis();
                String rootContext = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
                // Maxwell mock时ServletContext中没有IOC容器
                if (null == servletContext.getAttribute(rootContext)) {
                    servletContext.setAttribute(rootContext, applicationContext);
                }
                hotLoadServlet = new HotLoadServlet();
                hotLoadServlet.init(new ServletConfig() {
                    private Map<String, String> initParameters;
                    {
                        initParameters = new HashMap<>();
                        initParameters.put("loginUsername", "hiccup");
                        initParameters.put("loginPassword", "qwerasdf");
                    }
                    @Override
                    public String getServletName() {
                        return "hotLoadhServlet";
                    }

                    @Override
                    public ServletContext getServletContext() {
                        return servletContext;
                    }

                    @Override
                    public String getInitParameter(String name) {
                        return initParameters.get(name);
                    }

                    @Override
                    public Enumeration<String> getInitParameterNames() {
                        return Collections.enumeration(this.initParameters.keySet());
                    }
                });
                hasInitialized = true;
                LOGGER.info("HotLoadHandlerHolder initialize completed: {}", System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                LOGGER.error("HotLoadHandlerHolder initialize fail: ", e);
                throw new RuntimeException("HotLoadHandlerHolder initialize fail: ", e);
            } finally {
                lock.unlock();
            }
        }

        public static HttpServlet getServlet(ServletContext servletContext, ApplicationContext applicationContext) {
            if (!hasInitialized) {
                init(servletContext, applicationContext);
            }
            return hotLoadServlet;
        }
    }
}
