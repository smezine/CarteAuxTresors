package org.smezine.utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.smezine.model.*;
import org.smezine.model.enumeration.Orientation;
import org.smezine.model.enumeration.TypologieCase;

public abstract class CarteUtils {

    public static Carte readAllLines(BufferedReader reader) throws IOException {
        var carte = new Carte();
        String ligne;

        while ((ligne = reader.readLine()) != null) {
            var st = new StringTokenizer(ligne, "-");
            try {
                switch (TypologieCase.valueOf(st.nextToken().trim())){
                    case C -> CarteUtils.createCarte(st, carte);
                    case M -> CarteUtils.createMontage(st, carte);
                    case T -> CarteUtils.createTresors(st, carte);
                    case A -> CarteUtils.createJoueurs(st, carte);
                }
            } catch (IllegalArgumentException e){
                System.out.println("Un caractère en debut de ligne ne fait pas parti des carateres connus : " + Arrays.toString(TypologieCase.values()));
                throw e;
            } catch (NoSuchElementException e){
                System.out.println("Le format d'une ligne n'a pas été respectée.");
                throw e;
            } catch (IndexOutOfBoundsException e){
                System.out.println("Un des elements de la carte est positionné en dehors de la carte. Revoir la configuration ;)");
                throw e;
            }
        }
        return carte;
    }
    public static void createCarte(StringTokenizer st, Carte carte) {
        int largeur = Integer.parseInt(st.nextToken().trim());
        int hauteur = Integer.parseInt(st.nextToken().trim());
        carte.setHauteur(hauteur);
        carte.setLargeur(largeur);
        carte.initialisationTailleCarte(hauteur, largeur);
    }

    public static void createJoueurs(StringTokenizer st, Carte carte) {
        var aventurier = new Aventurier();
        aventurier.setNom(st.nextToken().trim());
        int positionHorizontale = Integer.parseInt(st.nextToken().trim());
        aventurier.setPositionHorizontale(positionHorizontale);
        int positionVerticale = Integer.parseInt(st.nextToken().trim());
        aventurier.setPositionVerticale(positionVerticale);
        aventurier.setOrientation(Orientation.valueOf(st.nextToken().trim()));
        aventurier.setDeplacements(st.nextToken().trim().toCharArray());
        carte.getAventuriers().add(aventurier);
        carte.getCases()[positionVerticale][positionHorizontale].setAventurier(aventurier);
    }

    public static void createTresors(StringTokenizer st, Carte carte) {
        int positionHorizontale = Integer.parseInt(st.nextToken().trim());
        int positionVerticale = Integer.parseInt(st.nextToken().trim());
        int nbTresor = Integer.parseInt(st.nextToken().trim());
        var tresor = new Tresor();
        tresor.setPositionVerticale(positionVerticale);
        tresor.setPositionHorizontale(positionHorizontale);
        tresor.setNbTresors(nbTresor);
        carte.getTresors().add(tresor);
        carte.getCases()[positionVerticale][positionHorizontale].setTypologieCase(TypologieCase.T);
        carte.getCases()[positionVerticale][positionHorizontale].setNbTresors(nbTresor);
    }

    public static void createMontage(StringTokenizer st, Carte carte) {
        int positionHorizontale = Integer.parseInt(st.nextToken().trim());
        int positionVerticale = Integer.parseInt(st.nextToken().trim());
        var montagne = new Montagne();
        montagne.setPositionHorizontale(positionHorizontale);
        montagne.setPositionVerticale(positionVerticale);
        carte.getMontagnes().add(montagne);
        carte.getCases()[positionVerticale][positionHorizontale].setTypologieCase(TypologieCase.M);
    }

    public static void afficherCarte(Carte carte) {

        // Loop through all rows
        for (Case[] ligne : carte.getCases()) {
            Arrays.stream(ligne).forEach((k) -> {
                if (k.getAventurier() != null) {
                    System.out.print(k.getAventurier().getNom().charAt(0) + "\t ");
                } else if (k.getTypologieCase() == TypologieCase.P) {
                    System.out.print(".\t   ");
                } else if (k.getTypologieCase() == TypologieCase.T) {
                    System.out.print(k.getTypologieCase().name() + "(" + k.getNbTresors() + ")\t");
                } else {
                    System.out.print(k.getTypologieCase().name() + "\t");
                }
            });
            System.out.println();
        }
    }
    public static void genererFichierSortie(Carte carte) throws IOException {
        FileWriter fileWriter = new FileWriter("ResultatFinal.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.println(TypologieCase.C + " - " + carte.getLargeur() + " - " + carte.getHauteur());

        carte.getMontagnes().forEach(montagne ->
            printWriter.println(TypologieCase.M + " - " + montagne.getPositionHorizontale() + " - " + montagne.getPositionVerticale()));

        carte.getTresors().forEach(tresor ->
            printWriter.println(TypologieCase.T + " - " + tresor.getPositionHorizontale() + " - " + tresor.getPositionVerticale()
                    + " - " + tresor.getNbTresors()));

        carte.getAventuriers().forEach(aventurier ->
            printWriter.println(TypologieCase.A + " - " + aventurier.getNom() + " - " + aventurier.getPositionHorizontale()
                    + " - " + aventurier.getPositionVerticale() + " - " + aventurier.getOrientation() + " - " + aventurier.getNbTresorsRamasses()));

        printWriter.close();
    }
}
