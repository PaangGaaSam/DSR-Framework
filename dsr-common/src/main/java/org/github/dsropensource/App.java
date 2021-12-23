package org.github.dsropensource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info("1");
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Hello World!");
    }
}
