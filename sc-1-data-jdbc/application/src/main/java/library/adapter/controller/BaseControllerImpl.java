package library.adapter.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import library.dto.UIConfig;

public abstract class BaseControllerImpl {
    @Value("#{new Boolean('${library.red-background:false}')}")
    private boolean FIXED_BACKGROUND;
    
    protected UIConfig getUIConfig() {
        boolean isTanzu = System.getenv("CF_INSTANCE_INDEX") != null;
        String instanceIndex = FIXED_BACKGROUND ? "-1" : Optional.ofNullable(System.getenv("CF_INSTANCE_INDEX")).orElse("0");
        return UIConfig.of(isTanzu, Integer.valueOf(instanceIndex));
    } 
}
