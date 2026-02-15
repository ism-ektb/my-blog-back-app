package ru.ism.myblogbackapp.mapper;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StringFilter {

    /**
     * Выделение из общей поисковой строки фразы для поиска названия
     * @param search
     * @return
     */
    public String findTitleSearch(String search) {
        return Arrays.stream(search.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> s.charAt(0) != 35)
                .collect(Collectors.joining(" ")).trim();
    }

    /**
     * Выделение из общей поисковой строки списка тагов
     * @param search
     * @return
     */
    public List<String> findTagSearch(String search) {
        return Arrays.stream(search.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> s.charAt(0) == 35)
                .map(s -> s.substring(1))
                .collect(Collectors.toList());
    }
}
