package org.smezine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.smezine.model.enumeration.TypologieCase;

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

    public Case(int positionVerticale, int positionHorizontale, int nbTresors){
        this.positionVerticale = positionVerticale;
        this.positionHorizontale = positionHorizontale;
        this.typologieCase = TypologieCase.T;
        this.nbTresors = nbTresors;
    }



    public void supprimerTresor(){
        nbTresors--;
    }
}
