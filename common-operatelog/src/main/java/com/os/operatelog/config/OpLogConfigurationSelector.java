package com.os.operatelog.config;

import com.os.operatelog.annotation.EnableOpLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

@Slf4j
public class OpLogConfigurationSelector extends AdviceModeImportSelector<EnableOpLog> {

    private static final String
            OP_LOG_PROXY_AUTO_CONFIGURATION = "com.os.operatelog.config.OpLogProxyAutoConfiguration";

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        if(adviceMode == AdviceMode.PROXY)
            return new String[]{OpLogProxyAutoConfiguration.class.getName()};
        else
            return new String[]{OP_LOG_PROXY_AUTO_CONFIGURATION};
    }
}
