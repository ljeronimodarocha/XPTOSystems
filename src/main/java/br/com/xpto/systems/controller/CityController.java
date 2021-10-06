package br.com.xpto.systems.controller;

import br.com.xpto.systems.dto.CityDTO;
import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import br.com.xpto.systems.services.CityServices;
import br.com.xpto.systems.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {

    private final CityServices cityServices;

    @PostMapping(value = "file")
    public ResponseEntity<Object> uploadFile(@RequestPart MultipartFile file) {

        cityServices.saveFileIntoDB(file);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "capitals")
    public ResponseEntity<List<City>> getCapitals() {
        List<City> capitals = cityServices.returnCapitals();
        return new ResponseEntity<>(capitals, HttpStatus.OK);
    }

    @GetMapping("{ibgeID}")
    public ResponseEntity<Object> getCityByIbgeID(@PathVariable Integer ibgeID) {
        City city = this.cityServices.returnCityByIbgeID(ibgeID);
        if (city != null) {
            return new ResponseEntity<>(this.cityServices.returnCityByIbgeID(ibgeID), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/find")
    @Operation(description = "get cities by state")
    public ResponseEntity<Object> returnCitiesByUF(@RequestParam String state) {
        if (!state.isEmpty()) {
            List<CityDTO> cityDTOS = this.cityServices.returnCitiesByUF(State.valueOf(state.toUpperCase()));
            return new ResponseEntity<>(cityDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<City> saveCity(@RequestBody @Valid City city) {
        return new ResponseEntity<>(this.cityServices.saveCity(city), HttpStatus.CREATED);
    }

    @DeleteMapping("{ibgeID}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer ibgeID) {
        this.cityServices.deleteCity(ibgeID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "file/filterValue")
    @Operation(description = "return values in a file by column and filter")
    public ResponseEntity<Object> filterValueInFileByColumnAndFilter(@RequestPart MultipartFile file, @RequestParam String column, @RequestParam String filter) {
        if (!column.isEmpty() && !filter.isEmpty() && !file.isEmpty()) {
            List<List<String>> lists = FileUtil.readFile(file);
            List<String> values = FileUtil.filterValueByColumn(lists, column, filter);
            return new ResponseEntity<>(values, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "file/totalByColumn")
    @Operation(description = "return total value in a file by column ")
    public ResponseEntity<Object> totalValuesByColumn(@RequestPart MultipartFile file, @RequestParam String column) {
        if (!column.isEmpty() && !file.isEmpty()) {
            List<List<String>> lists = FileUtil.readFile(file);
            Integer total = FileUtil.countTotalValueByColumnWiothOutDuplicateValue(lists, column);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "file/total")
    @Operation(description = "return total values")
    public ResponseEntity<Object> totalValues(@RequestPart MultipartFile file) {
        if (!file.isEmpty()) {
            List<List<String>> lists = FileUtil.readFile(file);
            Integer total = FileUtil.returnTotalData(lists);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
