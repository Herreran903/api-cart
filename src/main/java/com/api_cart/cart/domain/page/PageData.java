package com.api_cart.cart.domain.page;

import java.math.BigDecimal;
import java.util.List;

public class PageData<T> {
    private List<T> data;
    private int pageNumber;
    private int totalElements;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;
    private BigDecimal total;

    public PageData(
            List<T> data,
            int pageNumber,
            int totalElements,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious) {
        this.data = data;
        this.pageNumber = pageNumber;
        this.totalElements = totalElements;
        this.isFirst = isFirst;
        this.isLast = isLast;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> list) {
        this.data = list;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }
}