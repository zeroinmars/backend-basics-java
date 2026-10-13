package kr.example.basics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskTitleValidatorTest {

    private final TaskTitleValidator validator = new TaskTitleValidator();

    @Test
    void acceptsNormalTitle() {
        assertEquals("자바 공부하기", validator.validate("자바 공부하기"));
    }

    @Test
    void rejectsEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(""));
    }

    @Test
    void rejectsSpacesOnlyTitle() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate("   "));
    }

    @Test
    void rejectsNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> validator.validate(null));
    }

    @Test
    void stripsSurroundingWhitespace() {
        assertEquals("자바 공부하기", validator.validate("  자바 공부하기 \t\n"));
    }
}
