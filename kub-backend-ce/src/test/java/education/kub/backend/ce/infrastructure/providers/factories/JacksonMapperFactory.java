package education.kub.backend.ce.infrastructure.providers.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

@Component
@TestPropertySource(locations = {"classpath:test.application.properties"})
public class JacksonMapperFactory {
    private static PropertyNamingStrategy namingStrategy;

    @Autowired
    private void setNamingStrategy(@Value("${spring.jackson.property-naming-strategy}") PropertyNamingStrategy _namingStrategy) {
        namingStrategy = _namingStrategy;
    }

    public static MappingJackson2HttpMessageConverter createJacksonMapper() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper( new ObjectMapper().setPropertyNamingStrategy(namingStrategy));
        return mappingJackson2HttpMessageConverter;
    }
}
