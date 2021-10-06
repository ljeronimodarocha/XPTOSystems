package br.com.xpto.systems.controller;

import br.com.xpto.systems.dto.StateDTO;
import br.com.xpto.systems.services.CityServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/state/")
public class StateController {


    @Autowired
    private CityServices cityServices;

    @GetMapping(value = "status")
    @Operation(description = "get state with more and less cities")
    public ResponseEntity<Object> getStatNameHigherCityAndMinorCity() {
        Map<String, BigInteger> stateWithMoreCities = cityServices.returnStatNameHigherCity();
        Map<String, BigInteger> stateWithLessCities = cityServices.returnStatNameMinorCity();

        StateDTO stateDTOMoreCities = new StateDTO(stateWithMoreCities);
        StateDTO stateDTOLessCities = new StateDTO(stateWithLessCities);
        List<StateDTO> listState = new ArrayList<>();
        listState.add(stateDTOMoreCities);
        listState.add(stateDTOLessCities);

        return new ResponseEntity<>(listState, HttpStatus.OK);
    }

    @GetMapping
    @Operation(description = "return total cities by states")
    public ResponseEntity<Object> returnTotalCitiesByStates() {
        return new ResponseEntity<>(cityServices.returnTotalCitiesByStates(), HttpStatus.OK);
    }


}
