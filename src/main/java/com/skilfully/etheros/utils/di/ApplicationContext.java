package com.skilfully.etheros.utils.di;

import com.skilfully.etheros.utils.di.annotations.Autowired;
import com.skilfully.etheros.utils.di.annotations.PostConstruct;
import com.skilfully.etheros.utils.di.annotations.Service;
import com.skilfully.etheros.utils.di.exception.DIException;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * DI容器核心类，负责Bean的扫描、注册、实例化、注入与生命周期管理
 * <p>
 * 使用方式一 (快捷, 推荐):
 * <pre>{@code
 * ApplicationContext context = ApplicationContext.run(Initializer.class);
 * }</pre>
 * <p>
 * 使用方式二 (手动):
 * <pre>{@code
 * ApplicationContext context = new ApplicationContext();
 * context.scan("com.skilfully.etheros").refresh();
 * ConfigManager config = context.getBean(ConfigManager.class);
 * }</pre>
 *
 * @author Etheros Group
 * @since 1.0.0
 * @version 1.0.0
 */
public class ApplicationContext {

    /**
     * 一键启动容器: 扫描primarySource所在包 → refresh
     * <p>
     * primarySource 若标注了 @Service 则同时注册为Bean，否则仅作扫描锚点。
     * <pre>{@code
     * // 在插件入口:
     * ApplicationContext.run(EtherosCore.class);
     * }</pre>
     *
     * @param primarySource 入口类（作为包扫描锚点）
     * @return 已refresh的容器
     */
    public static ApplicationContext run(Class<?> primarySource) {
        ApplicationContext context = new ApplicationContext();
        String packageName = primarySource.getPackage().getName();
        context.scan(packageName);
        if (primarySource.isAnnotationPresent(Service.class)) {
            context.register(primarySource);
        }
        context.refresh();
        return context;
    }

    private final List<Class<?>> serviceClasses = new ArrayList<>();
    private final Map<String, Object> beansByName = new LinkedHashMap<>();
    private final Map<Class<?>, Object> beansByType = new LinkedHashMap<>();
    private boolean refreshed = false;

    /**
     * 扫描指定包路径及子包下所有标注了 @Service 的类
     *
     * @param basePackage 基础包路径
     * @return this (流式API)
     */
    public ApplicationContext scan(String basePackage) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            String path = basePackage.replace('.', '/');
            Enumeration<URL> resources = cl.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                if ("jar".equals(protocol)) {
                    scanJar(resource, basePackage, path);
                } else if ("file".equals(protocol)) {
                    scanFile(new File(resource.toURI()), basePackage);
                }
            }
        } catch (DIException e) {
            throw e;
        } catch (Exception e) {
            throw new DIException("扫描包失败: " + basePackage, e);
        }
        return this;
    }

    /**
     * 手动注册一个 @Service 类
     *
     * @param clazz 标注了 @Service 的类
     * @return this (流式API)
     */
    public ApplicationContext register(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Service.class)) {
            throw new DIException(clazz.getName() + " 缺少 @Service 注解");
        }
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new DIException("@Service 不能标注在接口或抽象类上: " + clazz.getName());
        }
        if (!serviceClasses.contains(clazz)) {
            serviceClasses.add(clazz);
        }
        return this;
    }

    /**
     * 执行容器的完整初始化流程: 实例化 —> 依赖注入 —> @PostConstruct回调
     * 每个容器只能调用一次refresh
     */
    public void refresh() {
        if (refreshed) {
            throw new DIException("ApplicationContext 已经 refresh 过了");
        }
        refreshed = true;
        instantiateBeans();
        injectBeans();
        invokePostConstruct();
    }

    /**
     * 根据类型获取Bean实例
     *
     * @param clazz Bean的类型
     * @param <T>   泛型
     * @return Bean实例，未找到返回null
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        T bean = (T) beansByType.get(clazz);
        if (bean == null) {
            for (Map.Entry<Class<?>, Object> entry : beansByType.entrySet()) {
                if (clazz.isAssignableFrom(entry.getKey())) {
                    return (T) entry.getValue();
                }
            }
        }
        return bean;
    }

    /**
     * 获取已注册的Bean数量
     */
    public int getBeanCount() {
        return beansByType.size();
    }

    // 内部

    private void instantiateBeans() {
        for (Class<?> clazz : serviceClasses) {
            try {
                Object instance = clazz.newInstance();
                Service service = clazz.getAnnotation(Service.class);
                String name = service.value().isEmpty()
                        ? decapitalize(clazz.getSimpleName())
                        : service.value();
                beansByName.put(name, instance);
                beansByType.put(clazz, instance);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new DIException("无法实例化 " + clazz.getName() + ": 缺少可访问的无参构造器", e);
            }
        }
    }

    private void injectBeans() {
        for (Map.Entry<Class<?>, Object> entry : beansByType.entrySet()) {
            injectFields(entry.getValue(), entry.getKey());
        }
    }

    private void injectFields(Object bean, Class<?> beanClass) {
        Class<?> clazz = beanClass;
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    Class<?> fieldType = field.getType();
                    Object dependency = findBean(fieldType, beanClass);
                    if (dependency == null) {
                        if (autowired.required()) {
                            throw new DIException(
                                    "未找到类型为 " + fieldType.getName()
                                            + " 的Bean, 依赖注入失败: "
                                            + beanClass.getName() + "." + field.getName()
                            );
                        }
                        continue;
                    }
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                    } catch (IllegalAccessException e) {
                        throw new DIException(
                                "无法注入字段 " + beanClass.getName() + "." + field.getName(), e
                        );
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private Object findBean(Class<?> type, Class<?> requester) {
        Object bean = beansByType.get(type);
        if (bean != null) {
            return bean;
        }
        List<Object> candidates = new ArrayList<>();
        for (Map.Entry<Class<?>, Object> entry : beansByType.entrySet()) {
            if (!entry.getKey().equals(requester) && type.isAssignableFrom(entry.getKey())) {
                candidates.add(entry.getValue());
            }
        }
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        if (candidates.size() > 1) {
            throw new DIException(
                    "找到多个类型为 " + type.getName() + " 的Bean, 无法确定注入目标。"
                            + "请使用更具体的类型声明字段"
            );
        }
        return null;
    }

    private void invokePostConstruct() {
        for (Object bean : sortByDependency()) {
            Class<?> clazz = bean.getClass();
            while (clazz != Object.class) {
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(PostConstruct.class)) {
                        if (method.getParameterCount() != 0) {
                            throw new DIException(
                                    "@PostConstruct 方法必须是空参的: "
                                            + bean.getClass().getName() + "." + method.getName()
                            );
                        }
                        method.setAccessible(true);
                        try {
                            method.invoke(bean);
                        } catch (Exception e) {
                            throw new DIException(
                                    "执行 @PostConstruct 方法失败: "
                                            + bean.getClass().getName() + "." + method.getName(), e
                            );
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    /**
     * 按依赖关系拓扑排序，无依赖的Bean先执行@PostConstruct
     */
    private List<Object> sortByDependency() {
        Map<Class<?>, Object> classToBean = new LinkedHashMap<>(beansByType);
        Map<Object, Integer> inDegree = new LinkedHashMap<>();
        Map<Object, List<Object>> dependents = new LinkedHashMap<>();

        for (Object bean : beansByType.values()) {
            inDegree.put(bean, 0);
            dependents.put(bean, new ArrayList<>());
        }

        for (Map.Entry<Class<?>, Object> entry : beansByType.entrySet()) {
            Object bean = entry.getValue();
            Class<?> beanClass = entry.getKey();
            Class<?> current = beanClass;
            while (current != Object.class) {
                for (Field field : current.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Class<?> fieldType = field.getType();
                        Object dep = classToBean.get(fieldType);
                        if (dep == null) {
                            for (Map.Entry<Class<?>, Object> e : classToBean.entrySet()) {
                                if (fieldType.isAssignableFrom(e.getKey()) && !e.getKey().equals(beanClass)) {
                                    dep = e.getValue();
                                    break;
                                }
                            }
                        }
                        if (dep != null && dep != bean) {
                            inDegree.put(bean, inDegree.get(bean) + 1);
                            dependents.get(dep).add(bean);
                        }
                    }
                }
                current = current.getSuperclass();
            }
        }

        List<Object> sorted = new ArrayList<>();
        Queue<Object> queue = new LinkedList<>();
        for (Map.Entry<Object, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            Object bean = queue.poll();
            sorted.add(bean);
            for (Object dependent : dependents.get(bean)) {
                int degree = inDegree.get(dependent) - 1;
                inDegree.put(dependent, degree);
                if (degree == 0) {
                    queue.add(dependent);
                }
            }
        }

        if (sorted.size() != beansByType.size()) {
            throw new DIException("检测到循环依赖，@PostConstruct 无法排序");
        }

        return sorted;
    }

    // 扫描工具方法

    private void scanJar(URL resource, String basePackage, String path) throws Exception {
        String urlPath = resource.getPath();
        int separatorIndex = urlPath.lastIndexOf("!/");
        String jarPath = urlPath.substring(0, separatorIndex);
        if (jarPath.startsWith("file:")) {
            jarPath = jarPath.substring(5);
        }
        if (jarPath.startsWith("/") && jarPath.length() > 2 && jarPath.charAt(2) == ':') {
            jarPath = jarPath.substring(1);
        }
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class") && name.startsWith(path)) {
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    addServiceClass(className);
                }
            }
        }
    }

    private void scanFile(File dir, String basePackage) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanFile(file, basePackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + "."
                        + file.getName().substring(0, file.getName().length() - 6);
                addServiceClass(className);
            }
        }
    }

    private void addServiceClass(String className) {
        try {
            Class<?> clazz = Class.forName(className, false,
                    Thread.currentThread().getContextClassLoader());
            if (clazz.isAnnotationPresent(Service.class)) {
                if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                    throw new DIException(
                            "@Service 不能标注在接口或抽象类上: " + clazz.getName()
                    );
                }
                if (!serviceClasses.contains(clazz)) {
                    serviceClasses.add(clazz);
                }
            }
        } catch (DIException e) {
            throw e;
        } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
        }
    }

    private static String decapitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
