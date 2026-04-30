package com.skilfully.ethero.utils.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Yaml类型文件操作接口
 * @author Etheros Group
 * @since 1.0.0
 * @version 1.0.0
 * @see ConfigFile
 */
public interface YamlConfigFile extends ConfigFile {

    /**
     * 判断配置项的值是否为字符串类型
     * @param path 配置路径
     * @return 是字符串返回true，否则false
     */
    boolean isString(String path);

    /**
     * 获取字符串类型的配置值
     * @param path 配置路径
     * @return 字符串值，不存在或类型不匹配时返回Optional.empty()
     */
    Optional<String> getString(String path);

    /**
     * 获取字符串类型的配置值，不存在时返回默认值
     * @param path 配置路径
     * @param def 默认值
     * @return 字符串值或默认值
     */
    String getString(String path, String def);

    /**
     * 判断配置项的值是否为整型
     */
    boolean isInt(String path);

    /**
     * 获取整型配置值
     */
    int getInt(String path);

    /**
     * 获取整型配置值，不存在时返回默认值
     */
    int getInt(String path, int def);

    /**
     * 判断配置项的值是否为布尔类型
     */
    boolean isBoolean(String path);

    /**
     * 获取布尔类型配置值
     */
    Optional<Boolean> getBoolean(String path);

    /**
     * 获取布尔类型配置值，不存在时返回默认值
     */
    Boolean getBoolean(String path, Boolean def);

    /**
     * 判断配置项的值是否为双精度浮点数
     */
    boolean isDouble(String path);

    /**
     * 获取双精度浮点型配置值
     */
    Optional<Double> getDouble(String path);

    /**
     * 获取双精度浮点型配置值，不存在时返回默认值
     */
    Double getDouble(String path, Double def);

    /**
     * 判断配置项的值是否为长整型
     */
    boolean isLong(String path);

    /**
     * 获取长整型配置值
     */
    Optional<Long> getLong(String path);

    /**
     * 获取长整型配置值，不存在时返回默认值
     */
    Long getLong(String path, Long def);

    /**
     * 获取列表类型的配置值（元素类型混合时使用）
     * @param path 配置路径
     * @return 列表，不存在时返回空列表
     */
    List<?> getList(String path);

    /**
     * 获取列表类型的配置值，不存在时返回默认列表
     */
    List<?> getList(String path, List<?> def);

    /**
     * 获取字符串列表
     */
    List<String> getStringList(String path);

    /**
     * 获取整型列表
     */
    List<Integer> getIntegerList(String path);

    /**
     * 获取布尔列表
     */
    List<Boolean> getBooleanList(String path);

    /**
     * 获取双精度浮点列表
     */
    List<Double> getDoubleList(String path);

    /**
     * 获取单精度浮点列表
     */
    List<Float> getFloatList(String path);

    /**
     * 获取长整型列表
     */
    List<Long> getLongList(String path);

    /**
     * 获取字节列表
     */
    List<Byte> getByteList(String path);

    /**
     * 获取字符列表
     */
    List<Character> getCharacterList(String path);

    /**
     * 获取短整型列表
     */
    List<Short> getShortList(String path);

    /**
     * 获取Map对象列表
     */
    List<Map<?, ?>> getMapList(String path);
}