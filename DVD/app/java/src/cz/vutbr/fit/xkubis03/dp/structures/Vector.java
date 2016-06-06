/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.text.DecimalFormat;

import java.util.ArrayList;

/**
 * Třída pro vektor souřadnic.
 */
public class Vector extends ArrayList<Double> {

    /** Jméno vektoru/sekvence. */
    protected String name;

    /**
     * Konstruktor prázdného vektoru s názvem.
     * 
     * @param name jméno vektoru.
     */
    public Vector(String name) {
        /* Uložení jména. */
        this.name = name;
    }

    /**
     * Metoda vracející počet dimenzí vektoru.
     *
     * @return Vrací počet dimenzí.
     */
    public int getLength() {
        /* Pokud je prázdný -> nula. */
        if (isEmpty()) {
            return 0;
        } else {
            /* Jinak délka pole. */
            return size();
        }
    }

    /**
     * Metoda pro textovou reprezentaci vektoru.
     *
     * @return Vrací textovou reprezentaci vektoru.
     */
    @Override
    public String toString() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();
        /* Nastavení formátu čísel. */
        DecimalFormat fmt = new DecimalFormat("0.#");

        /* Přidání názvu do řetězce. */
        sb.append(name);

        /* Přidávání bodů do řetězce. */
        for (Double i : this) {
            /* Nejdřív oddělovač. */
            sb.append(";");
            /* Pak číslo. */
            sb.append(fmt.format(i));
        }

        /* Vrácení řetězce. */
        return sb.toString();
    }

    /**
     * Funkce vracející vzdálenost dvou vektorů.
     * 
     * @param v vektor pro výpočet vzdálenosti.
     *
     * @return Vrací vzdálenost vektorů.
     */
    public double getDistance(Vector v) {
        /* Suma pro výpočet. */
        double sum = 0.0;

        /* Cyklus sčítání mocnin rozdílu souřadnic vektorů. */
        for (int i = 0; i < getLength(); i++) {
            /* Přičtení rozdílu jedné souřadnice. */
            sum += Math.pow((get(i) - v.get(i)), 2);
        }

        /* Navrácení vzdálenosti. */
        return Math.sqrt(sum);
    }

}
