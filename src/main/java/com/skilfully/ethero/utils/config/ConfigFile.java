package com.skilfully.ethero.utils.config;

import java.io.IOException;
import java.util.Optional;

/**
 * 统一配置文件操作接口
 * @author Etheros Group
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ConfigFile {

    /**
     * 设置配置项的值
     * @param path 配置路径，如 "database.host"
     * @param value 新值
     */
    void set(String path, Object value);

    /**
     * 获取配置项的值（可能为空）
     * @param path 配置路径
     * @return 配置值，若不存在或值为null则返回Optional.empty()
     */
    Optional<Object> get(String path);

    /**
     * 获取配置项的值，不存在时返回默认值
     * @param path 配置路径
     * @param def 默认值
     * @return 配置值或默认值
     */
    Object get(String path, Object def);

    /**
     * 保存/写入配置文件
     * @throws IOException IO异常
     */
    void save() throws IOException;


}
