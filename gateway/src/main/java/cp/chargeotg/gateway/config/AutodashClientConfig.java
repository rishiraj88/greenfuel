package cp.chargeotg.gateway.config;

import cp.chargeotg.gateway.clients.AutodashClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class AutodashClientConfig {
    @Value("${autodash.connect.url}")
    private String autodashConnectUrl;

    @Bean
    public AutodashClient autodashClient(){
        // * RestClient from Spring 6 is used here for synchronous client.
        RestClient restClient = RestClient.builder()
                .baseUrl(autodashConnectUrl)
                .requestFactory(getClientRequestFactory())
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory= HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(AutodashClient.class);
    }

    private ClientHttpRequestFactory getClientRequestFactory() {
        ClientHttpRequestFactorySettings httpRequestFactorySettings = ClientHttpRequestFactorySettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(3));
                return ClientHttpRequestFactoryBuilder.detect()
                        .build(httpRequestFactorySettings);
    }
}
