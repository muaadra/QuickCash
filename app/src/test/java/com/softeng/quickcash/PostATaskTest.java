package com.softeng.quickcash;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PostATaskTest {

    @Test
    public void checkIfAStringIsEmpty_Test1() {
        PostATaskActivity postATask = new PostATaskActivity();
        assertTrue(postATask.isStringEmpty(""));
        assertTrue(postATask.isStringEmpty(null));
        assertFalse(postATask.isStringEmpty("test"));
    }

}
