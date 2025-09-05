public class AppFkt {//Variablendeklaration;
food: = leer;
drink: = leer;
//Eigentliches Programm;
public void order()
{
    System.out.println(Herzlich Willkommen  bei MD, Ihre Bestellung bitte!);
    food order() // Funktionsaufruf;
    System.out.println(Möchten Sie ein Getränk dazu bestellen? (j/n));
    j = System.console().readLine();
    if (Bestellvorgang fortsetzen? (Eingabe j) ?)
    {
        drink order() // Funktionsaufruf;
        System.out.println(Hier ist Ihre Bestellung: food und drink);
    } else {
        System.out.println(Hier ist Ihre Bestellung: food);
    }
    System.out.println(Vielen Dank, und Auf Wiedersehen!);
}
public void food order()
{
    System.out.println(Hamburger (1), Cheeseburger (2)oder Chilliburger (3) ?);
     1 = System.console().readLine();
    switch (Burgerauswahl)
    {
        case 1:
            food := Hamburger;
            break;
        case 2:
            food := Cheeseburger;
            break;
        case 3:
            food := Chilliburger;
            break;
    }
}
public void drink order()
{
    System.out.println(Cola (1) , Fanta (2), Sprite (3)?);
    E: 2 = System.console().readLine();
    switch (Getränkeauswahl)
    {
        case 1:
            drink := Cola ;
            break;
        case 2:
            drink := Fanta;
            break;
        case 3:
            drink := Sprite ;
            break;
    }
}


}
