package com.example.blackjackuus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kaardipakk {
    private ArrayList<Kaardid> kaardipakk;

    public Kaardipakk() {
        this.kaardipakk = new ArrayList<>();
    }

    public void teeKaardipakk(){ //meetod, mis teeb uue kaardipaki, lisades sinna kõik 52 kaarti ja segades selle selliselt, et järjekord on suvaline
        kaardipakk.clear();
        String mast = null;
        for (int i = 0; i < 4; i++) {
            if (i == 0)
                mast = "Ärtu";
            if (i == 1)
                mast = "Ruutu";
            if (i == 2)
                mast = "Risti";
            if (i == 3)
                mast = "Poti";
            for (int nimi = 2; nimi < 15; nimi++) {
                if (nimi < 11) {
                    Kaardid k = new Kaardid(Integer.toString(nimi), mast);
                    k.setVäärtus(nimi);
                    kaardipakk.add(k);
                }else if (nimi == 11) {
                    Kaardid k = new Kaardid("J", mast);
                    k.setVäärtus(10);
                    kaardipakk.add(k);
                }else if (nimi == 12) {
                    Kaardid k = new Kaardid("Q", mast);
                    k.setVäärtus(10);
                    kaardipakk.add(k);
                }else if (nimi == 13) {
                    Kaardid k = new Kaardid("K", mast);
                    k.setVäärtus(10);
                    kaardipakk.add(k);
                }else {
                    Kaardid k = new Kaardid("A", mast);
                    k.setVäärtus(11);
                    kaardipakk.add(k);
                }
            }
        }
        Collections.shuffle((List<?>) kaardipakk);
    }

    public Kaardid võtaKaart(){ //meetod pakist ühe kaardi võtmiseks
        Kaardid kaart = kaardipakk.get(0);
        kaardipakk.remove(0);
        return kaart;
    }

    @Override
    public String toString() {
        for (Kaardid el : kaardipakk) {
            System.out.println(el);
        }
        return null;
    }
}
