package ru.ism.myblogbackapp.mapper;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringFilterTest {

    private StringFilter stringFilter = new StringFilter();

    @Test
    void findTitleSearch_returnsTitle() {
        String title = "test #tag3  title #tag1 #tag2";
        assertEquals("test title", stringFilter.findTitleSearch(title));
    }

    @Test
    void findTitleSearch_returns_EmptyString() {
        String title = " #tag1 #tag2";
        assertTrue(stringFilter.findTitleSearch(title).isEmpty());
    }

    @Test
    void findTitleSearchEmpty_returns_EmptyString() {
        String title = "";
        assertTrue(stringFilter.findTitleSearch(title).isEmpty());
    }


    @Test
    void findTagSearch() {
        String title = "test title #tag1 #tag2";
        assertEquals(List.of("tag1", "tag2"), stringFilter.findTagSearch(title));
    }


    @Test
    void findTagSearch_returns_EmptyList() {
        String title = "test title ";
        assertTrue(stringFilter.findTagSearch(title).isEmpty());
    }
}