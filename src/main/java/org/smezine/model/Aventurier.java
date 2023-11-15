package org.smezine.model;

import lombok.*;
import org.smezine.model.enumeration.Orientation;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Aventurier extends Case {

    private String nom;

    private Orientation orientation;

    private char[] deplacements;

    private int numeroDeplacement = 0;

    private int nbTresorsRamasses;

    public void deplacementSuivant(){
        this.numeroDeplacement++;
    }

    public char getDeplacement(){
        return deplacements[numeroDeplacement];
    }
}
