package com.api_cart.cart.domain.page;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageDataTest {

    @Test
    void shouldUpdatePageDataSuccessfully() {
        List<String> initialData = List.of("Item1", "Item2");
        PageData<String> pageData = new PageData<>(
                initialData, 1, 2, true, false, true, false
        );

        List<String> newData = List.of("NewItem1", "NewItem2");
        int newPageNumber = 2;
        int newTotalElements = 5;
        boolean newIsFirst = false;
        boolean newIsLast = true;
        boolean newHasNext = false;
        boolean newHasPrevious = true;

        pageData.setData(newData);
        pageData.setPageNumber(newPageNumber);
        pageData.setTotalElements(newTotalElements);
        pageData.setFirst(newIsFirst);
        pageData.setLast(newIsLast);
        pageData.setHasNext(newHasNext);
        pageData.setHasPrevious(newHasPrevious);

        assertEquals(newData, pageData.getData());
        assertEquals(newPageNumber, pageData.getPageNumber());
        assertEquals(newTotalElements, pageData.getTotalElements());
        assertFalse(pageData.isFirst());
        assertTrue(pageData.isLast());
        assertFalse(pageData.isHasNext());
        assertTrue(pageData.isHasPrevious());
    }
}