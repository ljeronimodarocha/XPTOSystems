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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CityServices {

    private final CityRepository cityRepository;

    public void saveFileIntoDB(MultipartFile file) throws   IllegalFormatException {

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
        return cityRepository.findByIbgeId(ibgeID).orElseThrow();
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
}
