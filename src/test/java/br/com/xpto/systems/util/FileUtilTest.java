package br.com.xpto.systems.util;


import br.com.xpto.systems.entity.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class FileUtilTest {

    @Test
    public void readFile() {
        File file = new File("src/test/fileTest/Desafio Cidades - Back End.csv");
        MultipartFile mockMultipartFile = null;
        List<List<String>> list = null;
        try {
            mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            list = FileUtil.readFile(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(list);
        Assertions.assertNotEquals(0, list.size());
    }

    @Test
    public void returnDataOrganizedIntoACityEntity() {
        File file = new File("src/test/fileTest/Desafio Cidades - Back End.csv");
        MultipartFile mockMultipartFile = null;
        List<List<String>> list = null;
        try {
            mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            list = FileUtil.readFile(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<City> listCity = FileUtil.saveDataIntoListCity(list);

        Assertions.assertNotNull(listCity);
        Assertions.assertNotEquals(0, listCity.size());
    }

    @Test
    public void getColumnFilterValue() {
        File file = new File("src/test/fileTest/Desafio Cidades - Back End.csv");
        MultipartFile mockMultipartFile = null;
        List<List<String>> list = null;
        try {
            mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            list = FileUtil.readFile(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> result = FileUtil.filterValueByColumn(list, "name", "Jaru");

        Assertions.assertNotEquals(0, result.size());
        Assertions.assertEquals("Jaru", result.get(0));
    }

    @Test
    public void countTotalValueByColumnWiothOutDuplicateValue() {
        File file = new File("src/test/fileTest/Desafio Cidades - Back End.csv");
        MultipartFile mockMultipartFile = null;
        List<List<String>> list = null;
        try {
            mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            list = FileUtil.readFile(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = FileUtil.countTotalValueByColumnWiothOutDuplicateValue(list, "mesoregion");

        Assertions.assertEquals(137, result);
    }

    @Test
    public void returnTotalData() {
        File file = new File("src/test/fileTest/Desafio Cidades - Back End.csv");
        MultipartFile mockMultipartFile = null;
        List<List<String>> list = null;
        try {
            mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), "csv", Files.readAllBytes(Paths.get(file.getAbsolutePath().toString())));
            list = FileUtil.readFile(mockMultipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer result = FileUtil.returnTotalData(list);

        Assertions.assertEquals(5565, result);
    }

    @Test
    public void readFileWithNullMultipartFileShouldReturnError() {
        List<List<String>> list = null;

        try {
            list = FileUtil.readFile(null);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.assertNotEquals(null, e);
            Assertions.assertNotNull(e);
            Assertions.assertNull(list);
            Assertions.assertEquals(NullPointerException.class, e);
        }
    }
}

