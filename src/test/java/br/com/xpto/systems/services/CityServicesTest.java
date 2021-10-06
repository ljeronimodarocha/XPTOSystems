package br.com.xpto.systems.services;

import br.com.xpto.systems.dto.CityDTO;
import br.com.xpto.systems.dto.StateDTO;
import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CityServicesTest {

    @Autowired
    private CityServices cityServices;

    public void init() {
        File file = new File("D:\\git\\systems\\src\\main\\resources\\fileTest\\Desafio Cidades - Back End.csv");
        MultipartFile mockMultipartFile = null;
        try {
            mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            cityServices.saveFileIntoDB(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void returnCityThatAreCapitals() {
        this.init();
        List<City> capitals = this.cityServices.returnCapitals();

        Assertions.assertNotNull(capitals);
        Assertions.assertNotEquals(null, capitals.stream().filter(capital -> capital.getName().equals("Manaus")));
        Assertions.assertEquals(27, capitals.size());
    }

    @Test
    public void returnStatHigherCity() {
        this.init();
        Map<String, BigInteger> t = cityServices.returnStatNameHigherCity();

        Assertions.assertNotNull(t);
        Assertions.assertEquals("MG", t.get("UF"));
        Assertions.assertEquals(new BigInteger("853"), t.get("VALUE"));
    }

    @Test
    public void returnStatMinorCity() {
        this.init();
        Map<String, BigInteger> t = cityServices.returnStatNameMinorCity();

        Assertions.assertNotNull(t);
        Assertions.assertEquals("DF", t.get("UF"));
        Assertions.assertEquals(new BigInteger("1"), t.get("VALUE"));
    }

    @Test
    public void returnCityByIbgeID() {
        this.init();
        City city = this.cityServices.returnCityByIbgeID(1300904);

        Assertions.assertNotNull(city);
        Assertions.assertEquals(1300904, city.getIbgeId());
    }

    @Test
    public void returnCitiesByUF() {
        this.init();
        List<CityDTO> cityDTOS = this.cityServices.returnCitiesByUF(State.SP);
        Assertions.assertEquals(645, cityDTOS.size());
    }

    @Test
    public void returnSavedCity() {
        City city = getCity();

        City citySaved = cityServices.saveCity(city);
        Assertions.assertNotNull(citySaved);
        Assertions.assertEquals(3550308, citySaved.getIbgeId());
    }

    @Test
    public void deleteCityByIbgeIDWithOutError() {
        City city = getCity();
        City citySaved = cityServices.saveCity(city);
        Assertions.assertDoesNotThrow(() -> this.cityServices.deleteCity(citySaved.getIbgeId()));
    }

    @Test
    public void returnTotalCitiesByStates() {
        this.init();
        List<StateDTO> stateDTOS = this.cityServices.returnTotalCitiesByStates();
        StateDTO goiais = stateDTOS.stream().filter(s -> "Goiás".equals(s.getName()))
                .findAny()
                .orElse(null);
        Assertions.assertNotNull(stateDTOS);
        Assertions.assertEquals(new BigInteger("246"), goiais.getTotal());
    }

    @Test
    public void getHeigherDistanceBeteewnCities(){
        this.init();
        List<City> cityList = this.cityServices.getHeigherDistanceBeteewnCities();
        Assertions.assertNotNull(cityList);
    }

    private City getCity() {
        return City.builder()
                .ibgeId(3550308).uf(State.SP).name("São Paulo").capital(false)
                .lon(new BigDecimal("-46.5703831821")).lat(new BigDecimal("-23.5673865"))
                .no_accents("Sao Paulo").alternative_names("").microregion("So Paulo")
                .mesoregion("Metropolitana de So Paulo").build();
    }
}
