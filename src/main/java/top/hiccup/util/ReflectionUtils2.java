////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package top.hiccup.util;
//
//import org.aopalliance.intercept.MethodInterceptor;
//import org.aopalliance.intercept.MethodInvocation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.asm.AnnotationVisitor;
//import org.springframework.asm.ClassReader;
//import org.springframework.asm.ClassVisitor;
//import org.springframework.asm.Type;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.util.Assert;
//
//import java.beans.IntrospectionException;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileFilter;
//import java.io.IOException;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.util.*;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.jar.JarEntry;
//
//public abstract class ReflectionUtils2 {
//    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
//
//
//
//    public static final ClassLoader getDefaultClassLoader() {
//        try {
//            return Thread.currentThread().getContextClassLoader();
//        } catch (Throwable var5) {
//            try {
//                return ReflectionUtils.class.getClassLoader();
//            } catch (SecurityException var4) {
//                try {
//                    return ClassLoader.getSystemClassLoader();
//                } catch (IllegalStateException | Error | SecurityException var3) {
//                    return null;
//                }
//            }
//        }
//    }
//
//    public static final Class<?> loadClass(String className) {
//        try {
//            return getDefaultClassLoader().loadClass(className);
//        } catch (ClassNotFoundException var4) {
//            try {
//                return Class.forName(className);
//            } catch (ClassNotFoundException var3) {
//                throw new RuntimeException(String.format("类[%s]不存在", new Object[]{className}), var3);
//            }
//        }
//    }
//
//    public static final <T> Class<T> loadClass(String className, Class<T> clazz) {
//        Class<?> type = loadClass(className);
//        if (clazz.isAssignableFrom(type)) {
//            return (Class<T>) type;
//        } else {
//            throw new RuntimeException(String.format("类[%s]不可以转换为[%s]", new Object[]{className, clazz.getName()}));
//        }
//    }
//
//    public static final <T> T newInstance(Class<T> clazz) {
//        try {
//            Constructor<?> constructor = findConstructor(clazz, new Class[0]);
//            if (constructor == null) {
//                throw new RuntimeException(String.format("无法实例化类[%s]，因为该类未定义默认构造函数", new Object[]{clazz}));
//            } else {
//                makeAccessibleConstructor(constructor);
//                return clazz.newInstance();
//            }
//        } catch (IllegalAccessException | InstantiationException var2) {
//            throw new RuntimeException(String.format("实例化类[%s]失败", new Object[]{clazz}), var2);
//        }
//    }
//
//    public static final Field getField(Class<?> clazz, String fieldName) {
//        Field field;
//        for (field = null; clazz != null && field == null; clazz = clazz.getSuperclass()) {
//            try {
//                field = clazz.getDeclaredField(fieldName);
//            } catch (NoSuchFieldException var4) {
//                ;
//            }
//        }
//
//        return field;
//    }
//
//    public static final void setFieldValue(Object object, String fieldName, Object value) {
//        Assert.notNull(object);
//        Class<?> objectClass = object.getClass();
//        Field field = getField(objectClass, fieldName);
//        if (field != null) {
//            setFieldValue(object, field, conversion(field.getType(), value));
//        } else {
//            Method method = getWriteMethod(objectClass, fieldName);
//            if (method != null) {
//                Class<?>[] types = method.getParameterTypes();
//                if (types.length == 1) {
//                    invokeMethod(object, method, new Object[]{conversion(types[0], value)});
//                    return;
//                }
//            }
//
//            throw new RuntimeException(String.format("类[%s]中不存在属性[%s]", new Object[]{object.getClass(), fieldName}));
//        }
//    }
//
//    public static final <T> T getFieldValue(Object object, String fieldName, Class<T> fieldClass) {
//        Assert.notNull(object);
//        Class<?> objectClass = object.getClass();
//        Field field = getField(objectClass, fieldName);
//        if (field != null) {
//            return getFieldValue(object, field, fieldClass);
//        } else {
//            Method method = getReadMethod(objectClass, fieldName);
//            if (method == null) {
//                throw new RuntimeException(String.format("类[%s]中不存在属性[%s]", new Object[]{object.getClass(), fieldName}));
//            } else {
//                return (T) invokeMethod(object, method, new Object[0]);
//            }
//        }
//    }
//
//    public static final void setStaticFieldValue(Class<?> objectClass, String fieldName, Object value) {
//        if (null == objectClass) {
//            throw new RuntimeException(String.format("请指定属性[%s]所在类", new Object[]{fieldName}));
//        } else {
//            Field field = getField(objectClass, fieldName);
//            if (null == field) {
//                throw new RuntimeException(String.format("属性[%s]在类[%s]中不存在", new Object[]{fieldName, objectClass.getName()}));
//            } else {
//                setFieldValue((Object) null, (Field) field, value);
//            }
//        }
//    }
//
//    public static final <T> T getStaticFieldValue(Class<?> objectClass, String fieldName, Class<T> fieldClass) {
//        if (null == objectClass) {
//            throw new RuntimeException(String.format("请指定属性[%s]所在类", new Object[]{fieldName}));
//        } else {
//            Field field = getField(objectClass, fieldName);
//            if (null == field) {
//                throw new RuntimeException(String.format("属性[%s]在类[%s]中不存在", new Object[]{fieldName, objectClass.getName()}));
//            } else {
//                return getFieldValue((Object) null, (Field) field, fieldClass);
//            }
//        }
//    }
//
//    public static final void setStaticFinalFieldValue(Class<?> objectClass, String fieldName, Object value) {
//        if (null == objectClass) {
//            throw new RuntimeException(String.format("请指定属性[%s]所在类", new Object[]{fieldName}));
//        } else {
//            Field field = getField(objectClass, fieldName);
//            if (null == field) {
//                throw new RuntimeException(String.format("属性[%s]在类[%s]中不存在", new Object[]{fieldName, objectClass.getName()}));
//            } else {
//                setFieldValue(field, (String) "modifiers", Integer.valueOf(field.getModifiers() & -17));
//                setFieldValue((Object) null, (Field) field, value);
//            }
//        }
//    }
//
//    public static final Constructor<?> findConstructor(Class<?> clazz, Class<?>[] parameterTypes) {
//        try {
//            return clazz.getDeclaredConstructor(parameterTypes);
//        } catch (NoSuchMethodException var3) {
//            return null;
//        }
//    }
//
//    public static final Object invokeConstructor(Constructor<?> constructor, Object[] args, boolean makeAccessible) {
//        boolean isAccessible = constructor.isAccessible();
//        if (!isAccessible) {
//            constructor.setAccessible(true);
//        }
//
//        Object var4;
//        try {
//            var4 = constructor.newInstance(args);
//        } catch (ReflectiveOperationException var8) {
//            throw new RuntimeException(String.format("使用参数为[%s]的构造函数[%s]进行实例化失败", new Object[]{Arrays.toString(args), constructor}), var8);
//        } finally {
//            if (!isAccessible) {
//                constructor.setAccessible(false);
//            }
//
//        }
//
//        return var4;
//    }
//
//    public static void makeAccessibleConstructor(final Constructor<?> constructor) {
//        if (!constructor.isAccessible()) {
//            AccessController.doPrivileged(new PrivilegedAction<Object>() {
//                public Object run() {
//                    constructor.setAccessible(true);
//                    return null;
//                }
//            });
//        }
//
//    }
//
//    public static final Class<?>[] findInterfaces(Object object) {
//        return (Class[]) findInterfaceList(object).toArray(new Class[0]);
//    }
//
//    public static final Vector<Class<?>> findInterfaceList(Object object) {
//        Assert.notNull(object);
//        Vector<Class<?>> interfaceList = new Vector();
//
//        for (Class clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
//            Class<?>[] interfaces = clazz.getInterfaces();
//            interfaceList.addAll(Arrays.asList(interfaces));
//        }
//
//        return interfaceList;
//    }
//
//    public static final Set<Class<?>> findAssignedClassList(Class<?>[] types, Class... annotations) {
//        return findAssignedClassList(StringUtils.asArray(PathUtils.getScanPath()), types, annotations);
//    }
//
//    public static final Set<Class<?>> findAssignedClassList(String[] paths, Class<?>[] types, Class... annotations) {
//        final Set<String> pathSet = new HashSet();
//        final Set<String> targetAnnotationNameSet = new LinkedHashSet();
//        final Set<String> targetTypeNameSet = ThreadUtils.createConcurrentSet(String.class);
//        final Set<String> ignoreTypeNameSet = ThreadUtils.createConcurrentSet(String.class);
//        final LinkedBlockingQueue<String> pathQueue = new LinkedBlockingQueue();
//        Class[] var8 = types;
//        int var9 = types.length;
//
//        int var10;
//        Class annotation;
//        for (var10 = 0; var10 < var9; ++var10) {
//            annotation = var8[var10];
//            targetTypeNameSet.add(annotation.getName());
//        }
//
//        var8 = annotations;
//        var9 = annotations.length;
//
//        for (var10 = 0; var10 < var9; ++var10) {
//            annotation = var8[var10];
//            targetAnnotationNameSet.add(annotation.getName());
//        }
//
//        ignoreTypeNameSet.add("java.lang.Object");
//        String[] var15 = paths;
//        var9 = paths.length;
//
//        for (var10 = 0; var10 < var9; ++var10) {
//            String path = var15[var10];
//            pathQueue.add(path);
//        }
//
//        logger.debug("开始查找{}的实现类或者子类", targetTypeNameSet);
//
//        try {
//            ThreadUtils.parallelAndWait(new ThreadItem() {
//                public void task() {
//                    while (true) {
//                        try {
//                            String path;
//                            if ((path = (String) pathQueue.poll()) != null) {
//                                ReflectionUtils.findAssignedClassList(targetTypeNameSet, targetAnnotationNameSet, ignoreTypeNameSet, path, pathSet);
//                                continue;
//                            }
//                        } catch (Exception var3) {
//                            var3.printStackTrace();
//                        }
//
//                        return;
//                    }
//                }
//            }, "scan-thread");
//            Set<Class<?>> classSet = new HashSet();
//            Iterator var17 = pathSet.iterator();
//
//            while (var17.hasNext()) {
//                String path = (String) var17.next();
//                classSet.add(loadClass(StringUtils.resourcePath2ClassPath(path.substring(0, path.length() - 6))));
//            }
//
//            logger.debug("查找结果：{}", classSet);
//            return classSet;
//        } finally {
//            pathSet.clear();
//            targetTypeNameSet.clear();
//            ignoreTypeNameSet.clear();
//        }
//    }
//
//    public static final boolean implementsInterface(Class<?> clazz, String interfaceName) {
//        Assert.notNull(clazz);
//        Assert.notNull(interfaceName);
//
//        for (; clazz != null; clazz = clazz.getSuperclass()) {
//            Class<?>[] interfaces = clazz.getInterfaces();
//            if (interfaces != null) {
//                Class[] var3 = interfaces;
//                int var4 = interfaces.length;
//
//                for (int var5 = 0; var5 < var4; ++var5) {
//                    Class<?> iface = var3[var5];
//                    if (interfaceName.equals(iface.getName())) {
//                        return true;
//                    }
//                }
//            }
//        }
//
//        return false;
//    }
//
//    public static final boolean isSubclass(Class<?> clazz, String parentClassName) {
//        Assert.notNull(clazz);
//        Assert.notNull(parentClassName);
//
//        while (clazz != null) {
//            Class<?> superClass = clazz.getSuperclass();
//            if (superClass != null && parentClassName.equals(superClass.getName())) {
//                return true;
//            }
//
//            clazz = superClass;
//        }
//
//        return false;
//    }
//
//    public static final Object invokeMethod(Object target, String methodName, Object... args) {
//        Class<?>[] paramTypes = new Class[args.length];
//
//        for (int i = 0; i < paramTypes.length; ++i) {
//            paramTypes[i] = args[i].getClass();
//        }
//
//        Method findMethod = findMethod(target.getClass(), methodName, paramTypes);
//        if (findMethod == null) {
//            throw new UnsupportedOperationException(String.format("参数为[%s]，的方法[%s]不存在", new Object[]{Arrays.toString(args), methodName}));
//        } else {
//            return invokeMethod(target, findMethod, args);
//        }
//    }
//
//    public static final Object invokeMethod(Object target, Method method, Object... args) {
//        boolean isAccessible = method.isAccessible();
//        if (!isAccessible) {
//            method.setAccessible(true);
//        }
//
//        Object var4;
//        try {
//            var4 = method.invoke(target, args);
//        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException var8) {
//            throw new RuntimeException(String.format("无法执行参数为[%s]，的方法[%s]", new Object[]{Arrays.toString(args), method.getName()}), var8);
//        } finally {
//            if (!isAccessible) {
//                method.setAccessible(false);
//            }
//
//        }
//
//        return var4;
//    }
//
//    public static final Method getWriteMethod(Class<?> clazz, String name) {
//        try {
//            return getReadOrWriteMethods(clazz, name, false);
//        } catch (IntrospectionException var3) {
//            throw new RuntimeException(String.format("在类[%s]中获取属性[%s]写入方法异常，原因：", new Object[]{clazz, name}), var3);
//        }
//    }
//
//    public static final Method getReadMethod(Class<?> clazz, String name) {
//        try {
//            return getReadOrWriteMethods(clazz, name, false);
//        } catch (IntrospectionException var3) {
//            throw new RuntimeException(String.format("在类[%s]中获取属性[%s]读取方法异常，原因：", new Object[]{clazz, name}), var3);
//        }
//    }
//
//    public static final Map<String, Method> findWriteMethods(Class<?> clazz) {
//        try {
//            return findReadOrWriteMethods(clazz, false);
//        } catch (IntrospectionException var2) {
//            throw new RuntimeException(String.format("在类[%s]中查找写入方法异常，原因：", new Object[]{clazz}), var2);
//        }
//    }
//
//    public static final Map<String, Method> findReadMethods(Class<?> clazz) {
//        try {
//            return findReadOrWriteMethods(clazz, true);
//        } catch (IntrospectionException var2) {
//            throw new RuntimeException(String.format("在类[%s]中查找读取方法异常，原因：", new Object[]{clazz}), var2);
//        }
//    }
//
//    public static final Method findMethod(Class<?> clazz, String name, Class... paramTypes) {
//        Assert.notNull(clazz);
//        Assert.notNull(name);
//
//        while (!Object.class.equals(clazz) && clazz != null) {
//            Method[] methods = clazz.isInterface() ? clazz.getMethods() : clazz.getDeclaredMethods();
//
//            for (int i = 0; i < methods.length; ++i) {
//                Method method = methods[i];
//                if (name.equals(method.getName()) && paramTypes.length == method.getParameterTypes().length) {
//                    boolean found = true;
//                    Class<?>[] methodParameterTypes = method.getParameterTypes();
//
//                    for (int j = 0; j < methodParameterTypes.length; ++j) {
//                        found = methodParameterTypes[j].isAssignableFrom(paramTypes[j]);
//                        if (!found) {
//                            break;
//                        }
//                    }
//
//                    if (found) {
//                        return method;
//                    }
//                }
//            }
//
//            clazz = clazz.getSuperclass();
//        }
//
//        return null;
//    }
//
//    public static final <T> T createProxy(Class<T> interfaceClass, final InterfaceHandler handler) {
//        return ProxyFactory.getProxy(interfaceClass, new MethodInterceptor() {
//            public Object invoke(MethodInvocation invocation) throws Throwable {
//                Method method = invocation.getMethod();
//                Object[] parameters = invocation.getArguments();
//                return method.getDeclaringClass() == Object.class ? method.invoke(this, parameters) : handler.invoke(method, parameters);
//            }
//        });
//    }
//
//    public static final <T> T createProxy(Class<T> clazz, ClassHandler handler) {
//        if (clazz.isInterface()) {
//            throw new RuntimeException("指定的类型不可以为接口。如果想代理接口，请将参数ClassHandler改为InterfaceHandler");
//        } else {
//            return createProxy(newInstance(clazz), handler, clazz);
//        }
//    }
//
//    public static final <T> T createProxy(final Object object, final ClassHandler handler, Class<T> clazz) {
//        ProxyFactory factory = new ProxyFactory(object);
//        if (!clazz.isInterface()) {
//            factory.setInterfaces(new Class[0]);
//        }
//
//        factory.addAdvice(new MethodInterceptor() {
//            public Object invoke(MethodInvocation invocation) throws Throwable {
//                Method method = invocation.getMethod();
//                Object[] parameters = invocation.getArguments();
//                return method.getDeclaringClass() == Object.class ? method.invoke(this, parameters) : handler.invoke(object, method, parameters);
//            }
//        });
//        return (T) factory.getProxy();
//    }
//
//    public static Object conversion(Class<?> type, Object object) {
//        return !type.equals(Integer.TYPE) && !type.equals(Integer.class) ? (!type.equals(Double.TYPE) && !type.equals(Double.class) ? (!type.equals(Float.TYPE) && !type.equals(Float.class) ? (!type.equals(Long.TYPE) && !type.equals(Long.class) ? (!type.equals(Boolean.TYPE) && !type.equals(Boolean.class) ? object.toString() : Boolean.valueOf(object.toString())) : Long.valueOf(object.toString())) : Float.valueOf(object.toString())) : Double.valueOf(object.toString())) : Integer.valueOf(object.toString());
//    }
//
//    private static final Method getReadOrWriteMethods(Class<?> clazz, String name, boolean isRead) throws IntrospectionException {
//        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
//        PropertyDescriptor[] var4 = propertyDescriptors;
//        int var5 = propertyDescriptors.length;
//
//        for (int var6 = 0; var6 < var5; ++var6) {
//            PropertyDescriptor propertyDescriptor = var4[var6];
//            if (propertyDescriptor.getName().equals(name)) {
//                return isRead ? propertyDescriptor.getReadMethod() : propertyDescriptor.getWriteMethod();
//            }
//        }
//
//        return null;
//    }
//
//    private static final Map<String, Method> findReadOrWriteMethods(Class<?> clazz, boolean isRead) throws IntrospectionException {
//        Map<String, Method> result = new HashMap();
//        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
//        PropertyDescriptor[] var4 = propertyDescriptors;
//        int var5 = propertyDescriptors.length;
//
//        for (int var6 = 0; var6 < var5; ++var6) {
//            PropertyDescriptor propertyDescriptor = var4[var6];
//            Method method = isRead ? propertyDescriptor.getReadMethod() : propertyDescriptor.getWriteMethod();
//            if (null != method) {
//                result.put(propertyDescriptor.getName(), method);
//            }
//        }
//
//        return result;
//    }
//
//    private static void setFieldValue(Object object, Field field, Object value) {
//        boolean isAccessible = field.isAccessible();
//        if (!isAccessible) {
//            field.setAccessible(true);
//        }
//
//        try {
//            field.set(object, value);
//        } catch (IllegalAccessException | IllegalArgumentException var8) {
//            throw new RuntimeException(var8);
//        } finally {
//            if (!isAccessible) {
//                field.setAccessible(false);
//            }
//
//        }
//
//    }
//
//    private static <T> T getFieldValue(Object object, Field field, Class<T> clazz) {
//        boolean isAccessible = field.isAccessible();
//        if (!isAccessible) {
//            field.setAccessible(true);
//        }
//
//        T var4;
//        try {
//            var4 = ObjectUtils.cast(field.get(object), clazz);
//        } catch (IllegalAccessException | IllegalArgumentException var8) {
//            throw new RuntimeException(var8);
//        } finally {
//            if (!isAccessible) {
//                field.setAccessible(false);
//            }
//
//        }
//        return var4;
//    }
//
//    private static void findAssignedClassList(final Set<String> targetTypeNameSet, final Set<String> targetAnnotationNameSet, final Set<String> ignoreTypeNameSet, final String path, Set<String> pathSet) {
//        logger.debug("检索[{}]", path);
//        if (path.endsWith(".jar")) {
//            pathSet.addAll(JarUtils.getPathSet(path, new EntryFilter() {
//                public boolean accept(JarEntry entry) {
//                    String filePath = entry.getName();
//                    return !filePath.endsWith(".class") ? false : ReflectionUtils.isMatch(targetTypeNameSet, targetAnnotationNameSet, ignoreTypeNameSet, filePath);
//                }
//            }));
//        } else {
//            pathSet.addAll(FileUtils.getPathSet(path, new FileFilter() {
//                public boolean accept(File file) {
//                    String filePath = file.getPath();
//                    if (!filePath.endsWith(".class")) {
//                        return false;
//                    } else {
//                        filePath = filePath.substring(path.length() + 1);
//                        boolean result = ReflectionUtils.isMatch(targetTypeNameSet, targetAnnotationNameSet, ignoreTypeNameSet, filePath);
//                        if (result) {
//                            ReflectionUtils.setFieldValue(file, (String) "path", filePath.replaceAll("\\\\", "/"));
//                        }
//
//                        return result;
//                    }
//                }
//            }));
//        }
//
//    }
//
//    private static boolean isMatch(Set<String> targetTypeNameSet, Set<String> targetAnnotationNameSet, Set<String> ignoreTypeNameSet, String name) {
//        ReflectionUtils.AssignedMetadataVisitor visit = visit(name);
//        Set<String> parents = new HashSet();
//        if (!ignoreTypeNameSet.contains(visit.superClassName)) {
//            parents.add(visit.superClassName);
//        }
//
//        String[] var6 = visit.interfaceNames;
//        int var7 = var6.length;
//
//        for (int var8 = 0; var8 < var7; ++var8) {
//            String interfaceName = var6[var8];
//            parents.add(interfaceName);
//        }
//
//        Iterator var11 = parents.iterator();
//
//        String annotationName;
//        while (var11.hasNext()) {
//            annotationName = (String) var11.next();
//
//            try {
//                if (targetTypeNameSet.contains(annotationName) || isMatch(targetTypeNameSet, targetAnnotationNameSet, ignoreTypeNameSet, StringUtils.classPath2ResourcePath(annotationName) + ".class")) {
//                    targetTypeNameSet.add(visit.className);
//                    return true;
//                }
//            } catch (RuntimeException var10) {
//                logger.warn("类[{}]解析失败，原因：{}。系统直接进行忽略", annotationName, var10.getMessage());
//                ignoreTypeNameSet.add(annotationName);
//            }
//        }
//
//        var11 = visit.annotationNames.iterator();
//
//        do {
//            if (!var11.hasNext()) {
//                ignoreTypeNameSet.add(visit.className);
//                return false;
//            }
//
//            annotationName = (String) var11.next();
//        } while (!targetAnnotationNameSet.contains(annotationName));
//
//        targetTypeNameSet.add(visit.className);
//        return true;
//    }
//
//    private static ReflectionUtils.AssignedMetadataVisitor visit(String classPath) {
//        BufferedInputStream is = null;
//
//        ClassReader classReader;
//        try {
//            is = new BufferedInputStream((new ClassPathResource(classPath)).getInputStream());
//            classReader = new ClassReader(is);
//        } catch (IOException var11) {
//            throw new RuntimeException(String.format("解析类[%s]失败，原因：", new Object[]{classPath}), var11);
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException var10) {
//                    throw new RuntimeException(String.format("关闭类[%s]输入流失败，原因：", new Object[]{classPath}), var10);
//                }
//            }
//
//        }
//
//        ReflectionUtils.AssignedMetadataVisitor visit = new ReflectionUtils.AssignedMetadataVisitor();
//        classReader.accept(visit, 2);
//        return visit;
//    }
//
//    private static class AssignedMetadataVisitor extends ClassVisitor {
//        private String className;
//        private String superClassName;
//        private String[] interfaceNames;
//        private Set<String> annotationNames;
//
//        private AssignedMetadataVisitor() {
//            super(327680);
//            this.annotationNames = new LinkedHashSet();
//        }
//
//        @Override
//        public void visit(int version, int access, String name, String signature, String supername, String[] interfaces) {
//            this.className = StringUtils.resourcePath2ClassPath(name);
//            this.superClassName = StringUtils.resourcePath2ClassPath(supername);
//            int length = interfaces.length;
//            this.interfaceNames = new String[length];
//
//            for (int i = 0; i < length; ++i) {
//                this.interfaceNames[i] = StringUtils.resourcePath2ClassPath(interfaces[i]);
//            }
//
//        }
//
//        @Override
//        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//            this.annotationNames.add(Type.getType(desc).getClassName());
//            return null;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("className：%s；superClassName：%s；interfaceNames：%s；annotationNames：%s", new Object[]{this.className, this.superClassName, StringUtils.array2String(this.interfaceNames), StringUtils.collection2String(this.annotationNames)});
//        }
//    }
//}
