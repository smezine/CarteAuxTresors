package org.smezine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Aventurier extends Case {

    private String nom;

    private Orientation orientation;

    private char[] deplacements;

    private int numeroDeplacement = 0;

    private int nbTresorsRamasses;

    public int nextDeplacement(){
        return this.numeroDeplacement++;
    }

    public char getDeplacement(){
        return deplacements[numeroDeplacement];
    }

    public int getNombreDeplacements(){
        return deplacements.length;
    }
}
