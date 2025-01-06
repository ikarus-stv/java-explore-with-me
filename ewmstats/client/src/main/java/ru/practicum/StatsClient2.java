package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Slf4j
public class StatsClient2  implements StatsClient {
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final RestTemplate restTemplate;
    private final String serverUrl;

    @Autowired
    public StatsClient2(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {
        this.serverUrl = serverUrl;

        this.restTemplate = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter(new ObjectMapper().registerModule(new JavaTimeModule())))
                .build();
    }

    @Override
    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        String urisString = String.join(",", uris);

        Map<String, Object> parameters = Map.of("start", start,
                "end", end,
                "uris", urisString,
                "unique", unique);

        log.info("StatsClient2.get params {}", parameters);

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;
        try {
            statsServerResponse = restTemplate.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}", HttpMethod.GET, requestEntity, Object.class, parameters);
        } catch (HttpStatusCodeException e) {
            return null;
        }
        return mapper.convertValue(statsServerResponse.getBody(), new TypeReference<List<ViewStatsDto>>() {});
    }

    @Override
    public EndpointHitDto save(EndpointHitDto dto) {
        return restTemplate.postForObject(serverUrl + "/hit", dto, EndpointHitDto.class);
    }
    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
