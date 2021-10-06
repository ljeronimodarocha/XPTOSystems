package br.com.xpto.systems.services;

import br.com.xpto.systems.dto.CityDTO;
import br.com.xpto.systems.dto.StateDTO;
import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import br.com.xpto.systems.repository.CityRepository;
import br.com.xpto.systems.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CityServices {

    private final CityRepository cityRepository;

    public void saveFileIntoDB(MultipartFile file) throws IllegalFormatException {

        List<List<String>> lists = FileUtil.readFile(file);
        List<City> cityList = FileUtil.saveDataIntoListCity(lists);
        for (City city : cityList) {
            cityRepository.save(city);
        }
    }

    public List<City> returnCapitals() {
        return cityRepository.returnCityThatAreCapitalsOrderByName().orElseThrow();
    }

    public Map<String, BigInteger> returnStatNameHigherCity() {
        Map<String, BigInteger> value = cityRepository.returnStatNameHigherCity();
        return value;
    }

    public Map<String, BigInteger> returnStatNameMinorCity() {
        Map<String, BigInteger> value = cityRepository.returnStatNameMinorCity();
        return value;
    }

    public List<StateDTO> returnTotalCitiesByStates() {
        List<Map<String, BigInteger>> value = cityRepository.returnTotalCitiesByStates().orElseThrow();
        StateDTO stateDTO = new StateDTO();
        return stateDTO.returnListStateDTOP(value);
    }

    public City returnCityByIbgeID(Integer ibgeID) {
        return cityRepository.findByIbgeId(ibgeID).orElse(null);
    }

    public List<CityDTO> returnCitiesByUF(State state) {
        List<City> list = this.cityRepository.findCitiesByUF(state).orElseThrow();
        List<CityDTO> cityDTOSList = new ArrayList<>();
        for (City city : list) {
            CityDTO cityDTO = new CityDTO(city);
            cityDTOSList.add(cityDTO);
        }
        return cityDTOSList;
    }

    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    public void deleteCity(Integer ibgeID) {
        this.cityRepository.deleteByIbgeId(ibgeID);
    }

    public List<City> getHeigherDistanceBeteewnCities() {
        double maior = 0;
        City origem = new City();
        City destino = new City();
        List<City> cityList = this.cityRepository.findAll();
        for (City city : cityList) {
            for (int i = 0; i < cityList.size(); i++) {
                if (city.getIbgeId() != cityList.get(i).getIbgeId()) {
                    double lon1 = Math.toRadians(city.getLon().doubleValue());
                    double lon2 = Math.toRadians(cityList.get(i).getLon().doubleValue());
                    double lat1 = Math.toRadians(city.getLat().doubleValue());
                    double lat2 = Math.toRadians(cityList.get(i).getLat().doubleValue());

                    double dlon = lon2 - lon1;
                    double dlat = lat2 - lat1;
                    double a = Math.pow(Math.sin(dlat / 2), 2)
                            + Math.cos(lat1) * Math.cos(lat2)
                            * Math.pow(Math.sin(dlon / 2), 2);

                    double c = 2 * Math.asin(Math.sqrt(a));
                    double r = 6371;

                    double result = (c * r);
                    if (result > maior) {
                        maior = result;
                        origem = city;
                        destino = cityList.get(i);
                    }
                }
            }
        }
        return Arrays.asList(origem, destino);

    }
}
