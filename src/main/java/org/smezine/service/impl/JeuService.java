package org.smezine.service.impl;

import org.smezine.model.*;
import org.smezine.model.enumeration.Orientation;
import org.smezine.model.enumeration.TypologieCase;
import org.smezine.service.IJeuService;
import org.smezine.utils.CarteUtils;

import java.io.*;

public class JeuService implements IJeuService {

    @Override
    public Carte chargerCarte() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/DonneesJeu.txt"));
            return CarteUtils.readAllLines(bufferedReader);
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : "  + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du BufferedReader: "  + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void lancerTours(Carte carte) {
        carte.getAventuriers().forEach( aventurier -> {
            int nbDeplacement = aventurier.getDeplacements().length;

            while (nbDeplacement > 0){
                switch (aventurier.getDeplacement()){
                    case 'G','D' -> gererOrientation(aventurier);
                    case 'A'-> gererDeplacement(aventurier, carte);
                }
                nbDeplacement--;
            }
            System.out.println(String.format("Deplacement de l'aventurier %s fini. Carte actuelle : ", aventurier.getNom()));
        });
    }

    private void gererDeplacement(Aventurier aventurier, Carte carte) {
        switch (aventurier.getOrientation()){
            case S -> deplacementAventurierVertical(carte, aventurier, aventurier.getPositionVerticale() + 1);
            case N ->  deplacementAventurierVertical(carte, aventurier, aventurier.getPositionVerticale() - 1);
            case E ->  deplacementAventurierHorizontal(carte, aventurier, aventurier.getPositionHorizontale() + 1);
            case O -> deplacementAventurierHorizontal(carte, aventurier, aventurier.getPositionHorizontale() - 1);
        }
    }

    void gererOrientation(Aventurier aventurier) {
        if (aventurier.getDeplacement() == 'G'){
            switch (aventurier.getOrientation()){
                case N -> aventurier.setOrientation(Orientation.O);
                case S -> aventurier.setOrientation(Orientation.E);
                case E -> aventurier.setOrientation(Orientation.N);
                case O -> aventurier.setOrientation(Orientation.S);
            }
            aventurier.deplacementSuivant();
        } else if ( aventurier.getDeplacement() == 'D'){
            switch (aventurier.getOrientation()){
                case N -> aventurier.setOrientation(Orientation.E);
                case S -> aventurier.setOrientation(Orientation.O);
                case E -> aventurier.setOrientation(Orientation.S);
                case O -> aventurier.setOrientation(Orientation.N);
            }
            aventurier.deplacementSuivant();
        }
    }

    void deplacementAventurierVertical(Carte carte, Aventurier aventurier, int positionCible) {
        if(carte.getCases()[positionCible][aventurier.getPositionHorizontale()].getTypologieCase() != TypologieCase.M
                && carte.getCases()[positionCible][aventurier.getPositionHorizontale()].getAventurier() == null){
            carte.getCases()[aventurier.getPositionVerticale()][aventurier.getPositionHorizontale()].setAventurier(null);
            carte.getCases()[positionCible][aventurier.getPositionHorizontale()].setAventurier(aventurier);
            aventurier.setPositionVerticale(positionCible);
            aventurier.deplacementSuivant();
            verifierPresenceTresor(carte, aventurier);
        } else if (carte.getCases()[positionCible][aventurier.getPositionHorizontale()].getTypologieCase() == TypologieCase.M){
            System.out.printf(String.format("Deplacement ignoré pour %s, présence d'une montagne dans la case cible !", aventurier.getNom()));
        } else {
            System.out.println(String.format("Deplacement ignoré pour %s, présence d'un aventurier dans la case cible !", aventurier.getNom()));
        }
    }

    private void deplacementAventurierHorizontal(Carte carte, Aventurier aventurier, int positionCible) {
        if(carte.getCases()[aventurier.getPositionVerticale()][positionCible].getTypologieCase() != TypologieCase.M
                && carte.getCases()[aventurier.getPositionVerticale()][positionCible].getAventurier() == null){
            carte.getCases()[aventurier.getPositionVerticale()][aventurier.getPositionHorizontale()].setAventurier(null);
            carte.getCases()[aventurier.getPositionVerticale()][positionCible].setAventurier(aventurier);
            aventurier.setPositionHorizontale(positionCible);
            aventurier.deplacementSuivant();
            verifierPresenceTresor(carte, aventurier);
        } else if (carte.getCases()[positionCible][aventurier.getPositionVerticale()].getTypologieCase() == TypologieCase.M){
            System.out.printf(String.format("Deplacement ignoré pour %s, présence d'une montagne dans la case cible !", aventurier.getNom()));
        } else {
            System.out.println(String.format("Deplacement ignoré pour %s, présence d'un aventurier dans la case cible !", aventurier.getNom()));
        }
    }

    private void verifierPresenceTresor(Carte carte, Aventurier aventurier) {
        if(carte.getCases()[aventurier.getPositionVerticale()][aventurier.getPositionHorizontale()].getNbTresors() > 0){
            aventurier.setNbTresorsRamasses(aventurier.getNbTresorsRamasses() + 1);
            carte.getTresors().forEach(tresor -> {
                if (tresor.getPositionVerticale() == aventurier.getPositionVerticale() && tresor.getPositionHorizontale() == aventurier.getPositionHorizontale()){
                    tresor.supprimerTresor();
                }
            });
            carte.getCases()[aventurier.getPositionVerticale()][aventurier.getPositionHorizontale()].supprimerTresor();
        }
    }
}
