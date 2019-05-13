package top.hiccup.remote;

/**
 * 热加载类加载器
 *
 * @author wenhy
 * @date 2019/5/7
 */
public class HotLoadClassLoader extends ClassLoader {

    public HotLoadClassLoader() {
        super(HotLoadClassLoader.class.getClassLoader());
    }

    /**
     * 将defineClass暴露出来
     * @param bytes
     * @return
     */
    public Class loadByteCode(byte[] bytes) {
        return defineClass(null, bytes, 0, bytes.length);
    }
}
