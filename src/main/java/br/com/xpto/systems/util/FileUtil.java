package br.com.xpto.systems.util;

import br.com.xpto.systems.entity.City;
import br.com.xpto.systems.entity.State;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class FileUtil {
    public static List<List<String>> readFile(MultipartFile file){
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file.getOriginalFilename()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public static List<City> saveDataIntoListCity(List<List<String>> list) {
        List<City> listCity = new ArrayList<>();
        List<String> cabecalho = list.remove(0);
        Map<Integer, String> positionMapCSV = new HashMap<Integer, String>();
        List<Map> listInsert = new ArrayList<>();
        for (int i = 0; i < cabecalho.size(); i++) {
            positionMapCSV.put(i, cabecalho.get(i));
        }

        for (int i = 0; i < list.size(); i++) {
            List<String> line = list.get(i);
            HashMap<String, String> l = new HashMap<>();
            for (int j = 0; j < line.size(); j++) {
                l.put(cabecalho.get(j), line.get(j));
            }
            listInsert.add(l);
        }

        for (Map m : listInsert) {
            City city = new City();
            if (m.get("ibge_id") != null) {
                city.setIbgeId(Integer.valueOf(m.get("ibge_id").toString()));
            }
            if (m.get("uf") != null) {
                city.setUf(State.valueOf(m.get("uf").toString()));
            }
            if (m.get("name") != null) {
                city.setName(m.get("name").toString());
            }
            if (m.get("capital") != null && !m.get("capital").toString().isEmpty()) {
                city.setCapital(true);
            } else {
                city.setCapital(false);
            }
            if (m.get("lon") != null) {
                city.setLon(new BigDecimal(m.get("lon").toString()));
            }
            if (m.get("lat") != null) {
                city.setLat(new BigDecimal(m.get("lat").toString()));
            }
            if (m.get("no_accents") != null) {
                city.setNo_accents(m.get("no_accents").toString());
            }
            if (m.get("alternative_names") != null) {
                city.setAlternative_names(m.get("alternative_names").toString());
            }
            if (m.get("microregion") != null) {
                city.setMicroregion(m.get("microregion").toString());
            }
            if (m.get("mesoregion") != null) {
                city.setMesoregion(m.get("mesoregion").toString());
            }
            listCity.add(city);
        }

        return listCity;
    }

    public static List<String> filterValueByColumn(List<List<String>> list, String column, String filter) {
        List<String> cabecalho = list.remove(0);
        Map<Integer, String> positionMapCSV = new HashMap<Integer, String>();
        List<Map> listInsert = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < cabecalho.size(); i++) {
            positionMapCSV.put(i, cabecalho.get(i));
        }

        for (int i = 0; i < list.size(); i++) {
            List<String> line = list.get(i);
            HashMap<String, String> l = new HashMap<>();
            for (int j = 0; j < line.size(); j++) {
                l.put(cabecalho.get(j), line.get(j));
            }
            listInsert.add(l);
        }

        for (Map m : listInsert) {
            if (m.get(column) != null) {
                if (m.get(column).toString().contains(filter)) {
                    result.add(m.get(column).toString());
                }
            }
        }
        return result;
    }

    public static Integer countTotalValueByColumnWiothOutDuplicateValue(List<List<String>> list, String column) {
        List<String> cabecalho = list.remove(0);
        Map<Integer, String> positionMapCSV = new HashMap<Integer, String>();
        List<Map> listInsert = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < cabecalho.size(); i++) {
            positionMapCSV.put(i, cabecalho.get(i));
        }

        for (int i = 0; i < list.size(); i++) {
            List<String> line = list.get(i);
            HashMap<String, String> l = new HashMap<>();
            for (int j = 0; j < line.size(); j++) {
                l.put(cabecalho.get(j), line.get(j));
            }
            listInsert.add(l);
        }

        for (Map m : listInsert) {
            if (m.get(column) != null) {
                result.add(m.get(column).toString());
            }
        }
        Set<String> set = new HashSet<>(result);
        result.clear();
        result.addAll(set);

        return result.size();
    }

    public static Integer returnTotalData(List<List<String>> list) {
        return saveDataIntoListCity(list).size();
    }
}
