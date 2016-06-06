/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Třída pro seznam shluků.
 */
public class Clusters extends ArrayList<PointSet> {

    /**
     * Konstruktor pro nový prázdný seznam shluků.
     */
    public Clusters() {
    }

    /**
     * Konstruktor pro nový seznam shluků inicializovaný kolekcí.
     *
     * @param values kolekce shluků
     */
    public Clusters(Collection<PointSet> values) {
        /* Inicializace seznamu z kolekce. */
        super(values);
    }

    /**
     * Metoda vracející informace o počtu shluků
     * a jejich velikostech.
     *
     * @return Vrací řetězec s informace o shlucích.
     */
    @Override
    public String toString() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();
        /* Přidání počtu shluků. */
        sb.append(size());
        /* Přidání textu. */
        sb.append(" clusters of size: ");

        /* Přidání velikosti každého shluku. */
        for (final PointSet cl : this) {
            /* Přidání velikosti shluku c1. */
            sb.append(cl.size());
            /* Přidání oddělovače. */
            sb.append(" ");
        }

        /* Navrácení řetězce. */
        return sb.toString();
    }

}

