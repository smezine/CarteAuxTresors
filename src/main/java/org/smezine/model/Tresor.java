package org.smezine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Tresor extends Case{

    private int nbTresors;

    public void supprimerTresor(){
        nbTresors--;
    }
}
