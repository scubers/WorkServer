package com.jrwong.modules.iOSPackage.service;

import com.jrwong.modules.iOSPackage.bean.IOSConfig;

/**
 * Created by J on 16/8/25.
 */

public interface IOSConfigService {
    public void saveConfig(IOSConfig config);
    public IOSConfig getConfig();
}
