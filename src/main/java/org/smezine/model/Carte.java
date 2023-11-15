package org.smezine.model;

import lombok.Data;
import org.smezine.model.enumeration.TypologieCase;

import java.util.ArrayList;
import java.util.List;

@Data
public class Carte {

    private int hauteur;

    private int largeur;

    private List<Montagne> montagnes = new ArrayList<>();

    private List<Tresor> tresors = new ArrayList<>();

    private List<Aventurier> aventuriers = new ArrayList<>();

    private Case[][] cases;

    public void initialisationTailleCarte(int hauteur, int largeur) {
        cases = new Case[hauteur][largeur];
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                cases[i][j] = new Case(i, j, TypologieCase.P);
            }
        }
    }

}
