package org.smezine.service.impl;

import org.smezine.model.*;
import org.smezine.service.IInitGame;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class InitGame implements IInitGame {

    @Override
    public Carte chargerCarte() {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/DonneesJeu.txt"));
            return readAllLines(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void lancerTour(Carte carte) {

        OptionalInt nbDeplacement = carte.getAventuriers().stream().mapToInt(Aventurier::getNombreDeplacements).max();
        int nbDeplacementMax = nbDeplacement.isPresent() ? nbDeplacement.getAsInt() : 0;

        while (nbDeplacementMax > 0) {
            carte.getAventuriers().forEach(aventurier -> {
                switch (aventurier.getDeplacement()){
                    case 'G','D' -> gererOrientation(aventurier);
                    case 'A'-> gererDeplacement(aventurier, carte);
                }
            });
            nbDeplacementMax--;
        }
    }

    private void gererDeplacement(Aventurier aventurier, Carte carte) {
        switch (aventurier.getOrientation()){
            case S -> deplacementAventurierSud(carte, aventurier);
            case N ->  deplacementAventurierNord(carte, aventurier);
            case E ->  deplacementAventurierEst(carte, aventurier);
            case O -> deplacementAventurierOuest(carte, aventurier);
            default -> checkPresenceTresor(carte, aventurier);
        }
    }

    private void gererOrientation(Aventurier aventurier) {
        if (aventurier.getDeplacement() == 'G'){
            switch (aventurier.getOrientation()){
                case N -> aventurier.setOrientation(Orientation.O);
                case S -> aventurier.setOrientation(Orientation.E);
                case E -> aventurier.setOrientation(Orientation.N);
                case O -> aventurier.setOrientation(Orientation.S);
            }
            aventurier.nextDeplacement();
        } else if ( aventurier.getDeplacement() == 'D'){
            switch (aventurier.getOrientation()){
                case N -> aventurier.setOrientation(Orientation.E);
                case S -> aventurier.setOrientation(Orientation.O);
                case E -> aventurier.setOrientation(Orientation.S);
                case O -> aventurier.setOrientation(Orientation.N);
            }
            aventurier.nextDeplacement();
        }
    }

    private void deplacementAventurierSud(Carte carte, Aventurier aventurier) {
        int positionCible = aventurier.getPositionVerticale() + 1;

        //TODO : endOfMAP
        if(carte.getCases()[aventurier.getPositionHorizontale()][positionCible].getTypologieCase() != TypologieCase.M
        && carte.getCases()[aventurier.getPositionHorizontale()][positionCible].getAventurier() == null){
            carte.getCases()[aventurier.getPositionVerticale()][aventurier.getPositionHorizontale()].setAventurier(null);
            carte.getCases()[aventurier.getPositionHorizontale()][positionCible].setAventurier(aventurier);
            aventurier.setPositionVerticale(positionCible);
            aventurier.nextDeplacement();
        } else if (carte.getCases()[positionCible][aventurier.getPositionHorizontale()].getTypologieCase() == TypologieCase.M){
            System.out.println("Deplacement ignoré, présence d'une montagne");
        } else {
            System.out.println("Deplacement ignoré, présence d'un coequipier");
        }
    }

    private void deplacementAventurierNord(Carte carte, Aventurier aventurier) {
        int positionCible = aventurier.getPositionVerticale() - 1;

        //TODO : endOfMAP

        if(carte.getMontagnes().stream().anyMatch(montagne -> montagne.getPositionVerticale() == positionCible
                && montagne.getPositionHorizontale() == aventurier.getPositionHorizontale())){
            System.out.println("Deplacement ignoré, présence d'une montagne");
        } else {
            aventurier.setPositionVerticale(positionCible);
            aventurier.nextDeplacement();
        }
    }

    private void deplacementAventurierEst(Carte carte, Aventurier aventurier) {
        int positionCible = aventurier.getPositionHorizontale() + 1;

        //TODO : endOfMAP
        if(carte.getMontagnes().stream().anyMatch(montagne -> montagne.getPositionVerticale() == aventurier.getPositionVerticale()
                && montagne.getPositionHorizontale() == positionCible)){
            System.out.println("Deplacement ignoré, présence d'une montagne");
        } else {
            aventurier.setPositionHorizontale(positionCible);
            aventurier.nextDeplacement();
        }
    }

    private void deplacementAventurierOuest(Carte carte, Aventurier aventurier) {
        int positionCible = aventurier.getPositionHorizontale() - 1;

        //TODO : endOfMAP
        if(carte.getMontagnes().stream().anyMatch(montagne -> montagne.getPositionVerticale() == aventurier.getPositionVerticale()
                && montagne.getPositionHorizontale() == positionCible)){
            System.out.println("Deplacement ignoré, présence d'une montagne");
        } else {
            aventurier.setPositionHorizontale(positionCible);
            aventurier.nextDeplacement();
        }
    }


    private void checkPresenceTresor(Carte carte, Aventurier aventurier) {
        if(carte.getTresors().stream().anyMatch(tresor -> tresor.getPositionHorizontale() == aventurier.getPositionHorizontale()
                && tresor.getPositionVerticale() == aventurier.getPositionVerticale())){

            aventurier.setNbTresorsRamasses(aventurier.getNbTresorsRamasses() + 1);

        };
    }


    public Carte readAllLines(BufferedReader reader) throws IOException {
        var carte = new Carte();
        String ligne;

        while ((ligne = reader.readLine()) != null) {
            var st = new StringTokenizer(ligne, "-");
            var type = TypologieCase.valueOf(st.nextToken().trim());

            switch (type){
                case C -> createCarte(st, carte);
                case M -> createMontage(st, carte);
                case T -> createTresors(st, carte);
                case A -> createJoueurs(st, carte);
                default -> System.out.println("Le caractère est inconnu");
            }
        }
        System.out.println(carte);
        return carte;
    }

    private void createCarte(StringTokenizer st, Carte carte) {
        carte.setHauteur(Integer.parseInt(st.nextToken().trim()));
        carte.setLargeur(Integer.parseInt(st.nextToken().trim()));
        carte.initialisationTailleCarte(carte.getHauteur(), carte.getLargeur());
    }

    private void createJoueurs(StringTokenizer st, Carte carte) {
        var aventurier = new Aventurier();
        aventurier.setNom(st.nextToken().trim());
        int positionHorizontale = Integer.parseInt(st.nextToken().trim());
        aventurier.setPositionHorizontale(positionHorizontale);
        int positionVerticale = Integer.parseInt(st.nextToken().trim());
        aventurier.setPositionVerticale(positionVerticale);
        aventurier.setOrientation(Orientation.valueOf(st.nextToken().trim()));
        aventurier.setDeplacements(st.nextToken().trim().toCharArray());
        carte.getAventuriers().add(aventurier);
        // TODO : verifier si pas présence d'un joueur
        carte.getCases()[positionHorizontale][positionVerticale].setAventurier(aventurier);
    }

    private void createTresors(StringTokenizer st, Carte carte) {
        var tresor = new Tresor();
        tresor.setPositionHorizontale(Integer.parseInt(st.nextToken().trim()));
        tresor.setPositionVerticale(Integer.parseInt(st.nextToken().trim()));
        carte.getTresors().add(tresor);
        carte.getCases()[tresor.getPositionHorizontale()][tresor.getPositionVerticale()].setTypologieCase(TypologieCase.T);
        carte.getCases()[tresor.getPositionHorizontale()][tresor.getPositionVerticale()].ajouterTresor();
    }

    private void createMontage(StringTokenizer st, Carte carte) {
        var montage = new Montagne();
        try {
            montage.setPositionHorizontale(Integer.parseInt(st.nextToken().trim()));
            montage.setPositionVerticale(Integer.parseInt(st.nextToken().trim()));
            carte.getMontagnes().add(montage);
            carte.getCases()[montage.getPositionHorizontale()][montage.getPositionVerticale()].setTypologieCase(TypologieCase.M);

        } catch (NoSuchElementException e){
            System.out.println("Le format d'une ligne de type MONTAGNE n'a pas été respecté");
            throw e;
        }
    }
}
