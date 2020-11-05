package com.softeng.quickcash;
import org.junit.Test;

import java.util.ArrayList;
import android.content.Context;
import org.junit.Test;

import static org.junit.Assert.*;

public class test {

    @Test
            public void testList(){
             ArrayList<TaskPost> posts = new ArrayList<TaskPost>();
            retrieveMasterList r1 = new retrieveMasterList();

        posts = r1.getList();
            for(int i = 0; i < posts.size();i++){
                posts.get(i).getPostId();
            }
    }

}
