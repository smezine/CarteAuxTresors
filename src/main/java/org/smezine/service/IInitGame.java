package org.smezine.service;

import org.smezine.model.Carte;

public interface IInitGame {

    public Carte chargerCarte();

    public void lancerTour(Carte carte);
}
