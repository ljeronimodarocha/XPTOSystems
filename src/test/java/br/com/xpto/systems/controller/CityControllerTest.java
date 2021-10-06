package br.com.xpto.systems.controller;

import br.com.xpto.systems.dto.CityDTO;
import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import br.com.xpto.systems.repository.CityRepository;
import br.com.xpto.systems.services.CityServices;
import br.com.xpto.systems.util.FileUtil;
import org.assertj.core.api.Assertions;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.math.BigDecimal;
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
class CityControllerTest {

    @Autowired
    private CityServices cityServices;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private MockMvc mockMvc;

    private MockMultipartFile mockMultipartFile = null;


    @Autowired
    @Qualifier(value = "testRestTemplate")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

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

    public void init() {
        File file = new File("D:\\git\\systems\\src\\main\\resources\\fileTest\\Desafio Cidades - Back End.csv");
        try {
            mockMultipartFile = new MockMultipartFile("file", file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            this.mockMvc.perform(multipart("/city/file").file(mockMultipartFile)).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void saveFileShouldsucceed() {
        this.init();
        Integer totalFile = FileUtil.readFile(mockMultipartFile).size() - 1;
        Integer total = cityRepository.findAll().size();
        assertThat(total).isNotNull().isEqualTo(totalFile);
    }

    @Test
    public void returnCapitalsShouldsucceed() {
        this.init();
        List<City> response = this.testRestTemplate.exchange("/city/capitals", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<City>>() {
                }).getBody();
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(27);

    }

    @Test
    public void returnCityByIbgeIDShouldSucceed() {
        this.init();
        City cityFound = this.testRestTemplate.getForEntity("/city/{ibgeID}", City.class, 3550308).getBody();
        assertThat(cityFound).isNotNull();
        assertThat(cityFound.getIbgeId()).isEqualTo(3550308);
        assertThat(cityFound.getNo_accents()).isEqualTo("Sao Paulo");
    }

    @Test
    public void returnCitesByUF() {
        this.init();
        List<CityDTO> cityDTOS = this.testRestTemplate.exchange("/city/find?state=df", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CityDTO>>() {
                }).getBody();

        assertThat(cityDTOS).isNotNull().isNotEmpty();
        assertThat(cityDTOS.size()).isEqualTo(1);
    }

    @Test
    public void returnCitySavedShouldSucceed() {
        ResponseEntity<City> citySaved = this.testRestTemplate.postForEntity("/city", getCity(), City.class);
        assertThat(citySaved).isNotNull();
        assertThat(citySaved.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(citySaved.getBody().getIbgeId()).isEqualTo(3550308);
    }

    @Test
    public void returnStatusNoContentCodeWhenDeleteCityShouldSucceed() {
        ResponseEntity<City> citySaved = this.testRestTemplate.postForEntity("/city", getCity(), City.class);
        this.testRestTemplate.delete("/city", 3550308, Void.class);
        ResponseEntity<Void> response = this.testRestTemplate
                .exchange("/city/{ibgeID}", HttpMethod.DELETE, null, Void.class, citySaved.getBody().getIbgeId());
        org.assertj.core.api.Assertions.assertThat(response)
                .isNotNull();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void returnValuesByColumnAndFilterShouldSucceed() {
        File file = new File("D:\\git\\systems\\src\\main\\resources\\fileTest\\Desafio Cidades - Back End.csv");
        try {
            mockMultipartFile = new MockMultipartFile("file", file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));

            ResultActions resultActions = this.mockMvc.perform(multipart("/city/file/filterValue?column=name&filter=Jaru").file(mockMultipartFile)).andExpect(status().isOk());
            assertThat(resultActions).isNotNull();
            assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
            assertThat(resultActions.andReturn().getResponse().getContentAsString().contains("Jaru"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void returntotalValuesByColumnShouldSucceed() {
        File file = new File("D:\\git\\systems\\src\\main\\resources\\fileTest\\Desafio Cidades - Back End.csv");
        try {
            mockMultipartFile = new MockMultipartFile("file", file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));

            ResultActions resultActions = this.mockMvc.perform(multipart("/city/file/totalByColumn?column=uf").file(mockMultipartFile)).andExpect(status().isOk());
            assertThat(resultActions).isNotNull();
            assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
            Integer total = Integer.parseInt(resultActions.andReturn().getResponse().getContentAsString());
            assertThat(total).isEqualTo(27);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void returntotalValuesShouldSucceed() {
        File file = new File("D:\\git\\systems\\src\\main\\resources\\fileTest\\Desafio Cidades - Back End.csv");
        try {
            mockMultipartFile = new MockMultipartFile("file", file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));

            ResultActions resultActions = this.mockMvc.perform(multipart("/city/file/total").file(mockMultipartFile)).andExpect(status().isOk());
            assertThat(resultActions).isNotNull();
            assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
            Integer total = Integer.parseInt(resultActions.andReturn().getResponse().getContentAsString());
            assertThat(total).isEqualTo(5565);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private City getCity() {
        return City.builder()
                .ibgeId(3550308).uf(State.SP).name("São Paulo").capital(false)
                .lon(new BigDecimal("-46.5703831821")).lat(new BigDecimal("-23.5673865"))
                .no_accents("Sao Paulo").alternative_names("").microregion("So Paulo")
                .mesoregion("Metropolitana de So Paulo").build();
    }
}