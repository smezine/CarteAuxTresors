package org.smezine.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Case {

    private int positionVerticale;

    private int positionHorizontale;

    private TypologieCase typologieCase;

    private int nbTresors;

    private Aventurier aventurier;

    public Case(int positionVerticale, int positionHorizontale, TypologieCase typologieCase){
        this.positionVerticale = positionVerticale;
        this.positionHorizontale = positionHorizontale;
        this.typologieCase = typologieCase;
    }

    public void ajouterTresor(){
        nbTresors++;
    }
}
