package org.smezine.service.impl;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.smezine.model.*;
import org.smezine.model.enumeration.Orientation;
import org.smezine.model.enumeration.TypologieCase;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class JeuServiceTest {

    private static Carte carte;

    private static Aventurier aventurier;

    @BeforeAll
    public static void buildCarte(){
        carte = new Carte();
        carte.setLargeur(3);
        carte.setHauteur(4);
        aventurier = new Aventurier("Lara", Orientation.S, new char[]{'A','A','D','A','D','A','G','G','A'},0, 0);
        aventurier.setPositionHorizontale(1);
        aventurier.setPositionVerticale(1);
        carte.getAventuriers().add(aventurier);
        carte.getMontagnes().add(buildMontagne(0, 1, TypologieCase.M));
        carte.getTresors().add(buildTresor(3,0,2));
        carte.getTresors().add(buildTresor(3,1,3));

        carte.setCases(new Case[][]{
                {new Case(0,0, TypologieCase.P), new Case(0,1,TypologieCase.P),
                        new Case(0,2,TypologieCase.P)},
                {new Case(1,0, TypologieCase.P), new Case(1,1,TypologieCase.M),
                        new Case(1,2,TypologieCase.P)},
                {new Case(2,0, TypologieCase.P), new Case(2,1,TypologieCase.P),
                        new Case(2,2,TypologieCase.M)},
                {new Case(3,0, 2), new Case(3,1,3),
                        new Case(3,2,TypologieCase.P)},
        });
    }

    @Test
    void testChargerCarte() {
        var jeuService = new JeuService();
        var carte = jeuService.chargerCarte();
        assertNotNull(carte);
    }

    @Test
    void testGererOrientationNord_DeplacementG() {
        var jeuService = new JeuService();
        var aventurier = buildAventurier(Orientation.N, 'G');
        jeuService.gererOrientation(aventurier);
        assertEquals(Orientation.O, aventurier.getOrientation());
    }

    @Test
    void testGererOrientationNord_DeplacementD() {
        var jeuService = new JeuService();
        var aventurier = buildAventurier(Orientation.N, 'D');
        jeuService.gererOrientation(aventurier);
        assertEquals(Orientation.E, aventurier.getOrientation());
    }

    @Test
    void testGererOrientationEst_DeplacementG() {
        var jeuService = new JeuService();
        var aventurier = buildAventurier(Orientation.E, 'G');
        jeuService.gererOrientation(aventurier);
        assertEquals(Orientation.N, aventurier.getOrientation());
    }

    @Test
    void testGererOrientationEst_DeplacementD() {
        var jeuService = new JeuService();
        var aventurier = buildAventurier(Orientation.E, 'D');
        jeuService.gererOrientation(aventurier);
        assertEquals(Orientation.S, aventurier.getOrientation());
    }

    @Test
    void testGererOrientationOuest_DeplacementG() {
        var jeuService = new JeuService();
        var aventurier = buildAventurier(Orientation.O, 'G');
        jeuService.gererOrientation(aventurier);
        assertEquals(Orientation.S, aventurier.getOrientation());
    }

    @Test
    void testGererOrientationOuest_DeplacementD() {
        var jeuService = new JeuService();
        var aventurier = buildAventurier(Orientation.O, 'D');
        jeuService.gererOrientation(aventurier);
        assertEquals(Orientation.N, aventurier.getOrientation());
    }

    @Test
    void testDeplacementAventurierVertical_OrientationSud() {
        var jeuService = new JeuService();
        jeuService.gererOrientation(aventurier);
        assertEquals(2, aventurier.getPositionVerticale());
    }

    @Test
    void testDeplacementAventurierHorizontal() {
        var jeuService = new JeuService();
        jeuService.lancerTours(carte);
        assertEquals(Orientation.S, carte.getAventuriers().get(0).getOrientation());
        assertEquals(0, carte.getAventuriers().get(0).getPositionHorizontale());
        assertEquals(3, carte.getAventuriers().get(0).getPositionVerticale());
        assertEquals(3, carte.getAventuriers().get(0).getNbTresorsRamasses());
    }

    private static Aventurier buildAventurier(Orientation orientation, char deplacement) {
        var aventurier = new Aventurier();
        aventurier.setOrientation(orientation);
        aventurier.setDeplacements(new char[1]);
        Arrays.fill(aventurier.getDeplacements(), deplacement);
        return aventurier;
    }

    private static Montagne buildMontagne(int v, int h, TypologieCase typologieCase) {
        var montagne = new Montagne();
        montagne.setPositionVerticale(v);
        montagne.setPositionHorizontale(h);
        montagne.setTypologieCase(typologieCase);
        return montagne;
    }

    private static Tresor buildTresor(int v, int h, int nbTresors) {
        Tresor tresor = new Tresor();
        tresor.setPositionVerticale(v);
        tresor.setPositionHorizontale(h);
        tresor.setNbTresors(nbTresors);
        return tresor;
    }
}
