package br.com.xpto.systems.dto;

import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CityDTO {

    private String name;
    private State uf;

    public CityDTO(City city) {

        this.name = city.getName();
        this.uf = city.getUf();
    }
}
