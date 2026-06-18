package com.myapp;

import com.myapp.service.HelloService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloServiceTest {

    @Test
    void testMessage() {
        HelloService service = new HelloService();
        assertEquals("Hello Nuka vijay", service.getMessage());
    }
}
