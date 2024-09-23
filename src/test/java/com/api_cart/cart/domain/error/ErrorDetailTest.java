package com.api_cart.cart.domain.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailTest {

    @Test
    void testGettersAndSetters() {
        String field = "field";
        String message = "message";
        ErrorDetail errorDetail = new ErrorDetail(field, message);

        errorDetail.setField("newField");
        errorDetail.setMessage("newMessage");

        assertEquals("newField", errorDetail.getField());
        assertEquals("newMessage", errorDetail.getMessage());
    }

    @Test
    void testEquals_SameObject() {
        ErrorDetail errorDetail1 = new ErrorDetail("field", "message");

        assertEquals(errorDetail1, errorDetail1);
    }

    @Test
    void testEquals_DifferentObjectsWithSameValues() {
        ErrorDetail errorDetail1 = new ErrorDetail("field", "message");
        ErrorDetail errorDetail2 = new ErrorDetail("field", "message");

        assertEquals(errorDetail1, errorDetail2);
    }

    @Test
    void testEquals_DifferentField() {
        ErrorDetail errorDetail1 = new ErrorDetail("field1", "message");
        ErrorDetail errorDetail2 = new ErrorDetail("field2", "message");

        assertNotEquals(errorDetail1, errorDetail2);
    }

    @Test
    void testEquals_DifferentMessage() {
        ErrorDetail errorDetail1 = new ErrorDetail("field", "message1");
        ErrorDetail errorDetail2 = new ErrorDetail("field", "message2");

        assertNotEquals(errorDetail1, errorDetail2);
    }

    @Test
    void testEquals_NullObject() {
        ErrorDetail errorDetail = new ErrorDetail("field", "message");

        assertNotEquals(null, errorDetail);
    }

    @Test
    void testEquals_DifferentClass() {
        ErrorDetail errorDetail = new ErrorDetail("field", "message");
        String otherClassObject = "Some other object";

        assertNotEquals(errorDetail, otherClassObject);
    }

    @Test
    void testHashCode_SameValues() {
        ErrorDetail errorDetail1 = new ErrorDetail("field", "message");
        ErrorDetail errorDetail2 = new ErrorDetail("field", "message");

        assertEquals(errorDetail1.hashCode(), errorDetail2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues() {
        ErrorDetail errorDetail1 = new ErrorDetail("field1", "message");
        ErrorDetail errorDetail2 = new ErrorDetail("field2", "message");

        assertNotEquals(errorDetail1.hashCode(), errorDetail2.hashCode());
    }
}