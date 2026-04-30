package com.skilfully.ethero.service;

import com.skilfully.ethero.data.GlobalData;
import com.skilfully.ethero.utils.messenger.Messenger;
import lombok.Getter;

public class ECMessenger {

    @Getter
    private static final Messenger messenger = new Messenger(GlobalData.pluginName);

}
