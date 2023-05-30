package gateway;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.stereotype.Component;

// Temporary fix as per https://github.com/reactor/reactor-netty/issues/1431
@Component
public class DnsResolutionFixer implements HttpClientCustomizer {
    @Override
    public reactor.netty.http.client.HttpClient customize(reactor.netty.http.client.HttpClient httpClient) {
        return httpClient.resolver(DefaultAddressResolverGroup.INSTANCE);
    }
}
