package org.errorzhu.flow.plugin;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.errorzhu.flow.base.exception.FlowConfigException;
import org.errorzhu.flow.base.plugin.IPlugin;
import org.errorzhu.flow.base.plugin.PluginType;
import org.errorzhu.flow.shade.com.typesafe.config.Config;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class PluginLoader {


    public static final String BASE_SOURCE_CLASS = "org.errorzhu.flow.base.source.ISource";
    public static final String BASE_TRANSFORM_CLASS = "org.errorzhu.flow.base.transform.ITransform";
    public static final String BASE_SINK_CLASS = "org.errorzhu.flow.base.sink.ISink";
    public static final String PLUGIN_NAME = "pluginName";
    private final Config config;
    private String pluginsDir;

    public PluginLoader(Config config, String pluginsDir) {
        this.config = config;
        this.pluginsDir = pluginsDir;
        addPluginJars(this.pluginsDir);
    }

    private void loadJar(File file) {
        //从URLClassLoader类加载器中获取类的addURL方法
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        // 获取方法的访问权限
        boolean accessible = method.isAccessible();
        try {
            //修改访问权限为可写
            if (accessible == false) {
                method.setAccessible(true);
            }
            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            //获取jar文件的url路径
            URL url = file.toURI().toURL();
            //jar路径加入到系统url路径里
            method.invoke(classLoader, url);
        } catch (Exception e) {
            //
        } finally {
            method.setAccessible(accessible);
        }
    }

    private List<File> findJars(String pluginsDir) {
        List<File> jars = Lists.newLinkedList();
        Iterator<File> files = Files.fileTraverser().breadthFirst(new File(pluginsDir)).iterator();
        while (files.hasNext()) {
            File file = files.next();
            if (file.getName().endsWith(".jar")) {
                jars.add(file);
            }

        }
        return jars;
    }

    private void addPluginJars(String pluginsDir) {
        List<File> jars = findJars(pluginsDir);
        for (File jar : jars) {
            loadJar(jar);
        }
    }


    private String getPluginName(Config config) {
        if (!config.hasPath(PLUGIN_NAME)) {
            throw new FlowConfigException("error config format!");
        }
        return config.getString(PLUGIN_NAME);
    }

    public <T extends IPlugin> List<T> load(PluginType type) throws ClassNotFoundException {
        Objects.requireNonNull(type, "PluginType can not be null when create plugins!");
        List<T> plugins = Lists.newLinkedList();
        if (!config.hasPath(type.getType())) {
            return plugins;
        }
        List<? extends Config> pluginConfigs = config.getConfigList(type.getType());
        pluginConfigs.forEach(plugin -> {
            String pluginName = getPluginName(plugin);
            try {
                T p = initPlugin(pluginName, type);
                p.init(plugin);
                plugins.add(p);
            } catch (ClassNotFoundException e) {
                //
            }
        });
        return plugins;
    }

    private <T extends IPlugin> T initPlugin(String pluginName, PluginType type) throws ClassNotFoundException {
        ServiceLoader<T> plugins;
        switch (type) {
            case SOURCE:
                Class<T> baseSource = (Class<T>) Class.forName(BASE_SOURCE_CLASS);
                plugins = ServiceLoader.load(baseSource);
                break;
            case TRANSFORM:
                Class<T> baseTransform = (Class<T>) Class.forName(BASE_TRANSFORM_CLASS);
                plugins = ServiceLoader.load(baseTransform);
                break;
            case SINK:
                Class<T> baseSink = (Class<T>) Class.forName(BASE_SINK_CLASS);
                plugins = ServiceLoader.load(baseSink);
                break;
            default:
                throw new FlowConfigException("PluginType not support : [" + type + "]");
        }

        for (Iterator<T> it = plugins.iterator(); it.hasNext(); ) {
            try {
                T plugin = it.next();
                Class<?> serviceClass = plugin.getClass();
                String serviceClassName = serviceClass.getName();
                String clsNameToLower = serviceClassName.toLowerCase();
                String[] split = clsNameToLower.split("\\.");
                String clsPluginName = split[split.length - 1];
                if (clsPluginName.equals(pluginName.toLowerCase())) {
                    return plugin;
                }
            } catch (ServiceConfigurationError e) {

            }
        }
        return null;
    }
}
