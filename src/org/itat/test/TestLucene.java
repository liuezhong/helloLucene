package org.itat.test;

import org.junit.Test;

/**
 * Created by Administrator on 2018/2/28.
 */
public class TestLucene {
    @Test
    public void testIndex(){
        HelloLucene h1 = new HelloLucene();
        h1.index();
    }
    @Test
    public void testSearcher(){
        HelloLucene h2 = new HelloLucene();
        h2.searcher();
    }
}
