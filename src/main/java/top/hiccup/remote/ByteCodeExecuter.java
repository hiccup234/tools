package top.hiccup.remote;

import java.lang.reflect.Method;

/**
 * 执行动态上传的字节码
 *
 * @author wenhy
 * @date 2019/5/7
 */
public class ByteCodeExecuter {

    public static Object execute(byte[] bytes) {
        HotLoadClassLoader loader = new HotLoadClassLoader();
        Class clazz = loader.loadByteCode(bytes);
        Object result = null;
        try {
            Method method = clazz.getMethod("execute", new Class[]{});
            result = method.invoke(null, null);
        } catch (Throwable e) {
            e.printStackTrace(System.out);
        }
        return result;
    }
}
