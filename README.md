# EtherosCore

EtherosCore是由Etheros Group开发的一款适用于Minecraft服务器的管理插件，用于管理其他E家插件以及为其提供依赖

### 命令
***etheroscore可以简写为ec***
```text
根命令：/etheroscore

/ec info 查看EtherosCore信息 
/ec reload config 重新加载配置文件
/ec reload language 重新加载所有语言配置文件
/ec reload language <name> 重新加载指定语言配置文件

# 插件更新相关
/ec update plugin 更新全部插件
/ec update plugin <name> 更新指定插件
/ec update check 检查所有插件更新
/ec update check <name> 检查指定插件更新

# 插件管理相关
/ec plugin info <name> 查看指定插件信息
/ec plugin disable <name> 禁用指定插件
/ec plugin enable <name> 启用指定插件
/ec plugin reload <name> 重新加载指定插件
/ec plugin install <url> 安装指定插件
```


### 配置文件

- **setting.yml**
```yaml
# 本地服务器配置
server:

    # 此服务器的ID。在群组中不应该重复。
    id: 'lobby'

    # 此服务器的名称
    name: '大厅服务器'

    # 启用的语言文件，根据用户环境自行适配语言。第一个将作为插件语言。
    languages:
        - 'zh-CN.yml'
        - 'en-US.yml'

    # 插件更新
    plugin-update:
        # 是否启用插件更新功能
        enabled: true
        # 插件更新时，若未指定更新地址，则从下方的仓库中获取
        repositories:
            - 'https://repositories.etheros.skilfully.cn/'
        # 自动更新
        auto-update:
            # 是否启用自动更新
            enabled: false
            # 调度方式，可选：new-version，date-<月>-<日>
            # new-version：每天、每次启动服务器时检测，当插件有新版本时进行更新。
            # date-<月>-<日>：指定日期更新。
            type: 'new-version'
            # 不进行更新的列表。
            # | Do-Not-Update List.
            no-updates:
                - 'example'

# 群组设置
group:

    # 是否加入群组
    enabled: false

    # 上游代理服务器配置
    proxy-server:
        # 上游代理地址
        address: 'https://localhost:9530'

    # 使用代理服务器的全局配置
    proxy-config-use:
        server-languages: true
        plugin-update-repositories: true
```

- **zh-CN.yml**
```yaml
prefix: "[EtherosCore] "

plugin_language_loaded: "zh-CN.yml 已作为插件语言加载！"
language_loaded: "zh-CN.yml已加载！"
```

- **en-US.yml**
```yaml
prefix: "[EtherosCore] "

plugin_language_loaded: "en-US.yml has been loaded as a plugin language!"
language_loaded: "en-US.yml Loaded!"
```