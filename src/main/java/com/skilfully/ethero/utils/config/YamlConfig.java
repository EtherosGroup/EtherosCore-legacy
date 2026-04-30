package com.skilfully.ethero.utils.config;

import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Yaml配置文件管理实例
 * @author Etheros Grou[
 * @since 1.0.0
 * @version 1.0.0
 * @see YamlConfigFile
 * @see ConfigFile
 */
public class YamlConfig implements YamlConfigFile {

    private final File file;
    private YamlConfiguration config;

    public YamlConfig(@NonNull File configFile) throws FileNotFoundException {
        file = configFile;
        if (!file.exists()) {
            throw new FileNotFoundException(configFile.getAbsolutePath() + " 文件不存在");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * 获取文本配置值
     * @param node 配置节点
     * @return 配置值
     * @throws ClassCastException 不支持的类型
     */
    public @NonNull ListString getStringConfig(@NonNull String node) throws ClassCastException {
        return new ListString(config, node);
    }

    /**
     * @return "plugins/Etheros/{pluginName}/"
     */
    public static String getWorkDirectory(@NonNull String pluginName) {
        return "plugins/Etheros/" + pluginName + "/";
    }


    /**
     * 创建插件工作目录
     * @param pluginName 插件名
     * @return 插件工作目录/null（创建失败）
     */
    public static @Nullable File createWorkDirectory(@NonNull String pluginName) {
        String workDirectoryPath = "plugins/Etheros/" + pluginName;
        Path path = Paths.get(workDirectoryPath);
        File directory = path.toFile();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return null;
            }
        }
        return directory;
    }

    /**
     * 从jar文件的resources目录提取文件到指定路径
     * @param plugin 插件主类
     * @param resourcePath 资源文件，应传入resources下的文件
     * @param toFile 将文件复制到
     * @param overwrite 如果文件已存在是否覆盖
     * @return 复制后的文件对象/null（复制失败）
     */
    public static @Nullable File extractFileFromJarResources(
            @NonNull JavaPlugin plugin,
            @NonNull String resourcePath,
            @NonNull File toFile,
            boolean overwrite
    ) throws IOException {
        if (toFile.exists() && !overwrite) {
            return toFile;
        }

        File parentDir = toFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                return null;
            }
        }

        try (InputStream in = plugin.getResource(resourcePath);
             OutputStream out = Files.newOutputStream(toFile.toPath())) {

            if (in == null) {
                return null;
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            return toFile;
        }
    }

    /**
     * 重新加载配置文件（不会保存已做的更改，请使用{@code .save()}）
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void save() throws IOException {
        config.save(file);
    }

    @Override
    public void set(String path, Object value) {
        config.set(path, value);
    }

    @Override
    public Optional<Object> get(String path) {
        return Optional.ofNullable(config.get(path));
    }

    @Override
    public Object get(String path, Object def) {
        Object value = config.get(path);
        return value != null ? value : def;
    }

    @Override
    public boolean isString(String path) {
        return config.isString(path);
    }

    @Override
    public Optional<String> getString(String path) {
        return Optional.ofNullable(config.getString(path));
    }

    @Override
    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    @Override
    public boolean isInt(String path) {
        return config.isInt(path);
    }

    @Override
    public int getInt(String path) {
        return config.getInt(path);
    }

    @Override
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    @Override
    public boolean isBoolean(String path) {
        return config.isBoolean(path);
    }

    @Override
    public Optional<Boolean> getBoolean(String path) {
        Object value = config.get(path);
        if (value == null) {
            return Optional.empty();
        }
        if (value instanceof Boolean) {
            return Optional.of((Boolean) value);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean getBoolean(String path, Boolean def) {
        Object value = config.get(path);
        if (value == null) {
            return def;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return def;
        }
    }

    @Override
    public boolean isDouble(String path) {
        return config.isDouble(path);
    }

    @Override
    public Optional<Double> getDouble(String path) {
        Object value = config.get(path);
        if (value == null) {
            return Optional.empty();
        }
        if (value instanceof Double) {
            return Optional.of((Double) value);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Double getDouble(String path, Double def) {
        Object value = config.get(path);
        if (value == null) {
            return def;
        }
        if (value instanceof Double) {
            return (Double) value;
        } else {
            return def;
        }
    }

    @Override
    public boolean isLong(String path) {
        return config.isLong(path);
    }

    @Override
    public Optional<Long> getLong(String path) {
        Object value = config.get(path);
        if (value == null) {
            return Optional.empty();
        }
        if (value instanceof Long) {
            return Optional.of((Long) value);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Long getLong(String path, Long def) {
        Object value = config.get(path);
        if (value == null) {
            return def;
        }
        if (value instanceof Long) {
            return (Long) value;
        } else {
            return def;
        }
    }

    @Override
    public List<?> getList(String path) {
        return config.getList(path);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        return config.getList(path, def);
    }

    @Override
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return config.getFloatList(path);
    }

    @Override
    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    @Override
    public List<Byte> getByteList(String path) {
        return config.getByteList(path);
    }

    @Override
    public List<Character> getCharacterList(String path) {
        return config.getCharacterList(path);
    }

    @Override
    public List<Short> getShortList(String path) {
        return config.getShortList(path);
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

}
