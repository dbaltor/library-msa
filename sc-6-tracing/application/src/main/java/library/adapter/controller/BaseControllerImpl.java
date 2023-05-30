package library.adapter.controller;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import library.dto.UIConfig;

public abstract class BaseControllerImpl {
    @Value("#{new Boolean('${library.red-background:false}')}")
    private boolean FIXED_BACKGROUND;

    @Value("${mysecret:}")
    private String secret;
//curl -k -i -H "Content-Type: application/json" -H "Authorization: $(cf oauth-token)" https://config-server-b81d8bf8-5339-46ae-93c3-a7257537989c.apps.hillsborough.cf-app.com/secrets/library-msa/cloud/master/mysecret -X PUT --data '{"mysecret": "XXXX"}'    
//curl -k -i -H "Content-Type: application/json" -H "Authorization: $(cf oauth-token)" https://config-server-b81d8bf8-5339-46ae-93c3-a7257537989c.apps.hillsborough.cf-app.com/secrets/library-msa/cloud/master/mysecret -X DELETE 
    @PostConstruct
    private void printSecret() {
        System.out.println("##############################################");
        System.out.println("Testing CredHub secret = " + secret);
        System.out.println("##############################################");
    }

    protected UIConfig getUIConfig() {
        boolean isTanzu = System.getenv("CF_INSTANCE_INDEX") != null;
        String instanceIndex = FIXED_BACKGROUND ? "-1" : Optional.ofNullable(System.getenv("CF_INSTANCE_INDEX")).orElse("0");
        return UIConfig.of(isTanzu, Integer.valueOf(instanceIndex));
    } 
}
