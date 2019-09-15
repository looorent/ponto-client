package be.looorent.ponto.client.http

import be.looorent.ponto.client.Configuration
import com.fasterxml.jackson.databind.ObjectMapper

trait RestTrait {
    Configuration configuration = Configuration.fromEnvironmentVariables()
    ObjectMapper mapper = JsonMapper.create()
    HttpClient http = new HttpClient(configuration, mapper)
}