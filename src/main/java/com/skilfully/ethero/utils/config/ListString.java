package com.skilfully.ethero.utils.config;

import lombok.NonNull;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListString {

    public final List<String> value;

    /**
     * 从YamlConfiguration构建StringList
     * @param config YamlConfiguration
     * @param node 配置节点
     * @throws ClassCastException 不支持的类型
     */
    public ListString(@NonNull YamlConfiguration config, @NonNull String node) throws ClassCastException {
        Object tmp = config.get(node);
        if (tmp == null) {
            value = Collections.emptyList();
        } else if (tmp instanceof String) {
            value = Collections.singletonList((String) tmp);
        } else if (tmp instanceof List<?>) {
            List<String> tempList = new ArrayList<>();
            for (Object o : (List<?>) tmp) {
                if (o == null) {
                    tempList.add(null);
                } else {
                    tempList.add(o.toString());
                }
            }
            value = tempList;
        } else {
            throw new ClassCastException("不支持的由 " + tmp.getClass().getSimpleName() + " 转换为 ListString");
        }
    }

}