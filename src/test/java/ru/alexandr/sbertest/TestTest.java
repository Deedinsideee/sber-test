package ru.alexandr.sbertest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class TestTest {
    private static final Logger log = LoggerFactory.getLogger(TestTest.class);

    @Test
    void testForTest(){
        int one = 1;
        log.error("wae ");
        assertEquals(1, one);
    }
}
