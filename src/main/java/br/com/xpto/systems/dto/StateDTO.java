package br.com.xpto.systems.dto;

import br.com.xpto.systems.entity.State;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class StateDTO {

    private String name;
    private String uf;
    private BigInteger total;

    public StateDTO(Map<String, BigInteger> stateMap) {
        if (stateMap.size() >= 2) {
            this.uf = String.valueOf(stateMap.get("UF"));
            this.name = State.valueOf(this.uf).getNome();
            this.total = stateMap.get("VALUE");
        }
    }
    public List<StateDTO> returnListStateDTOP(List<Map<String, BigInteger>> mapList) {
        List<StateDTO> list = new ArrayList<>();
        for (Map<String, BigInteger> map : mapList) {
            list.add(new StateDTO(map));
        }
        return list;
    }

}
