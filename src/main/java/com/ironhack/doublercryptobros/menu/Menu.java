package com.ironhack.doublercryptobros.menu;



import com.ironhack.doublercryptobros.console.ConsoleBuilder;
import com.ironhack.doublercryptobros.dto.CryptoDTO;
import com.ironhack.doublercryptobros.dto.ListCryptoResponse;
import com.ironhack.doublercryptobros.dto.UserDTO;
import com.ironhack.doublercryptobros.service.CryptoService;
import com.ironhack.doublercryptobros.service.UserService;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Menu {

    Scanner scanner;
    ConsoleBuilder consoleBuilder;
    CryptoService cryptoService;
    UserService userService;

    private String option;

    public Menu(CryptoService cryptoService, UserService userService) {
        this.scanner = new Scanner(System.in);
        this.consoleBuilder = new ConsoleBuilder(scanner);
        this.cryptoService = cryptoService;
        this.userService = userService;
    }

    public void start() throws InterruptedException {
        boolean exit = false;

        while (!exit) {
            List<String> options = Arrays.asList("Log In", "Sign Up", "About Us", "Exit");
            option = consoleBuilder.listConsoleInput("Welcome to CryptoBros Application. What would you like to do?", options);
            switch (option) {
                case "LOG IN" -> logIn();
                case "SIGN UP" -> signUp();
                case "ABOUT US" -> System.out.println("Somos los crypto bro");
                case "EXIT" -> exit = true;
                default -> System.out.println("Choose a correct option.");
            }
        }
    }

    private void logIn() throws InterruptedException {
        System.out.println("Enter you username: ");
        String user = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        Boolean canAccess = userService.authenticate(user, password);
        boolean exit = false;

        if (canAccess) {
            loginMenu();
        } else {
            logIn();
        }
    }




    public void loginMenu() throws InterruptedException {
        boolean exit = false;

        while (!exit) {
            List<String> options = Arrays.asList("Show all cryptos", "Search by name", "Exit");
            option = consoleBuilder.listConsoleInput("Choose what you want to do: ", options);
            switch (option) {
                case "SHOW ALL CRYPTOS" -> findCryptos();
                case "SEARCH BY NAME" -> findByName();
                case "EXIT" -> exit = true;
                default -> System.out.println("Choose a correct option.");
            }
        }
    }

    private void findByName(){
        System.out.println("Which crypto do you want to see?");
        String id = scanner.nextLine();
        findCryptos(Optional.ofNullable(id));
    }

    private void findCryptos(Optional<String> optionalId){
        System.out.println("Loading...");
        System.out.println("------------------------");
        if (optionalId.isPresent()){
            CryptoDTO cryptoById = cryptoService.findCryptoById(optionalId.get());
        }else {
            List<CryptoDTO> list = cryptoService.findAllCryptos().getData();
            //Añadir título a la lista y formatear price Usd y market cap a 2 decimales.
            list.forEach((cryptoDTO -> System.out.println(cryptoDTO)));
//        System.out.println(cryptoService.findAllCryptos().getData());
        }
    }
    private void signUp() throws InterruptedException {
        //Console console = System.console();
        System.out.println("Enter you username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        //String password = new String(System.console().readPassword("Enter your password: "));
        //char[] password = console.readPassword("%s", "Enter your password");
        //String password = new jline.ConsoleReader().readLine(new Character('*'));
        try {
            userService.register(username, password);
            System.out.println("User successfully registered");
            loginMenu();
        } catch (Exception error) {
            System.out.println("Invalid username, try again");
        }
    }



}
