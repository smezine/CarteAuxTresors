package org.smezine;

import org.smezine.model.Carte;
import org.smezine.service.impl.JeuService;
import org.smezine.utils.CarteUtils;

public class Main {
    public static void main(String[] args) {
        try {
            var jeuService = new JeuService();

            System.out.println("Initialisation de la carte à partir du fichier DonneesJeu.txt ..");
            Carte carte = jeuService.chargerCarte();

            System.out.println("Affichage de la carte initiale : ");
            CarteUtils.afficherCarte(carte);

            System.out.println("Lancement du jeu : ");
            jeuService.lancerTours(carte);

            System.out.println("Fin de jeu sans erreur ! Affichage de la carte finale : ");
            CarteUtils.afficherCarte(carte);

            System.out.println("Generation du fichier de sortie : ResultatFinal.txt");
            CarteUtils.genererFichierSortie(carte);

        } catch (Exception e){
            System.out.println("Fin de jeu en erreur." + e.getMessage());
        }
    }
}