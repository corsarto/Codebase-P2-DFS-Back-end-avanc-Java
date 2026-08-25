package com.openclassrooms.p2dfsbea;

import com.openclassrooms.p2dfsbea.ui.ConsoleMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class P2DfsBeaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(P2DfsBeaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to P2 DFS BEA - Console Application");
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();
    }
}
