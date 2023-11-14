package org.smezine;

import org.smezine.model.Carte;
import org.smezine.service.impl.InitGame;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Opt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        InitGame initGame = new InitGame();

        Carte carte = initGame.chargerCarte();

        System.out.println("La carte est chargée. Pour lancer le jeu, appuyez sur entrer ");

        initGame.lancerTour(carte);

        System.out.println("Fin de jeu : carte " + carte);

    }
}