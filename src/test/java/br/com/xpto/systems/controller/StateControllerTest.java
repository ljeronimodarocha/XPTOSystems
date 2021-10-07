package br.com.xpto.systems.controller;

import br.com.xpto.systems.dto.StateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class StateControllerTest {

    @Autowired
    @Qualifier(value = "testRestTemplate")
    private TestRestTemplate testRestTemplate;

    private MockMultipartFile mockMultipartFile;

    @Autowired
    private MockMvc mockMvc;

    public void init() {
        File file = new File("src/test/fileTest/Desafio Cidades - Back End.csv");
        try {
            mockMultipartFile = new MockMultipartFile("file", file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            this.mockMvc.perform(multipart("/city/file").file(mockMultipartFile)).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TestConfiguration
    @Lazy
    static class Config {
        @Bean(name = "testRestTemplate")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilderUser = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port);
            return new TestRestTemplate(restTemplateBuilderUser);
        }
    }

    @Test
    public void returnStatNameHigherCityAndMinorCity() {
        this.init();
        List<StateDTO> result = this.testRestTemplate.exchange("/state/status", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<StateDTO>>() {
                }).getBody();
        assertThat(result).isNotNull();
        assertThat(result.get(0).getTotal()).isEqualTo(853);
        assertThat(result.get(0).getUf()).isEqualTo("MG");
        assertThat(result.get(1).getTotal()).isEqualTo(1);
        assertThat(result.get(1).getUf()).isEqualTo("DF");
    }

    @Test
    public void returnTotalCitiesByStates() {
        this.init();
        List<StateDTO> result = this.testRestTemplate.exchange("/state", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<StateDTO>>() {
                }).getBody();
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.size()).isEqualTo(27);

        StateDTO RJ = result.stream().filter(s -> "Rio de Janeiro".equals(s.getName()))
                .findAny()
                .orElse(null);

        StateDTO RS = result.stream().filter(s -> "Rio Grande do Sul".equals(s.getName()))
                .findAny()
                .orElse(null);

        assertThat(RJ.getUf()).isEqualTo("RJ");
        assertThat(RJ.getTotal()).isEqualTo(92);

        assertThat(RS.getUf()).isEqualTo("RS");
        assertThat(RS.getTotal()).isEqualTo(496);
    }

}