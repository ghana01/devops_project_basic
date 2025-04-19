package com.myapp.monitor;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AppTest {
    @Test
    public void shouldReturnTrue() {
        assertTrue(true);
    }
    
    @Test
    public void applicationShouldStart() {
        App app = new App();
        // Simply verifying the app can be instantiated without errors
        assertTrue(app instanceof App);
    }
}