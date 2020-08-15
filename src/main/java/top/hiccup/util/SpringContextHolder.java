package top.hiccup.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Spring容器Holder
 *
 * @author wenhy
 * @date 2018/6/14
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return context;
    }

    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return context.getBean(clazz);
    }

    private static void checkApplicationContext() {
        if (context == null) {
            throw new IllegalStateException("The context has not initialized!");
        }
    }
}
