package com.skilfully.etheros.service;

import com.skilfully.etheros.data.GlobalData;
import com.skilfully.etheros.utils.messenger.Messenger;
import lombok.Getter;

public class ECMessenger {

    @Getter
    private static final Messenger messenger = new Messenger(GlobalData.pluginName);

}
