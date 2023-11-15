package org.smezine.service;

import org.smezine.model.Carte;

public interface IJeuService {

    Carte chargerCarte();

    void lancerTours(Carte carte);
}
