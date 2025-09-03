package com.cc.java;

import java.util.Scanner;

public class Bestellung {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String food = "";
        String drink = "";

        System.out.println("Herzlich Willkommen bei MD, Ihre Bestellung bitte!");
        System.out.println("Hamburger (1), Cheeseburger (2) oder Chilliburger (3)?");

        int burgerAuswahl = scanner.nextInt();
        switch (burgerAuswahl) {
            case 1:
                food = "Hamburger";
                break;
            case 2:
                food = "Cheeseburger";
                break;
            case 3:
                food = "Chilliburger";
                break;
            default:
                System.out.println("Ungültige Eingabe – kein Burger bestellt.");
        }

        System.out.println("Möchten Sie ein Getränk dazu bestellen? (j/n)");
        String antwort = scanner.next();

        if (antwort.equalsIgnoreCase("j")) {
            System.out.println("Cola (1), Fanta (2), Sprite (3)?");

            int getraenkAuswahl = scanner.nextInt();
            switch (getraenkAuswahl) {
                case 1:
                    drink = "Cola";
                    break;
                case 2:
                    drink = "Fanta";
                    break;
                case 3:
                    drink = "Sprite";
                    break;
                default:
                    System.out.println("Ungültige Eingabe – kein Getränk bestellt.");
            }

            System.out.println("Hier ist Ihre Bestellung: " + food + " und " + drink);
        } else {
            System.out.println("Hier ist Ihre Bestellung: " + food);
        }

        System.out.println("Vielen Dank, und auf Wiedersehen!");
        scanner.close();
    }
}





