/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Třída pro shluk/množinu bodů.
 */
public class PointSet extends ArrayList<Point> {

    /**
     * Konstruktor prázdného shluku.
     */
    public PointSet() {
    }

    /**
     * Konstruktor shluku z kolekce bodů.
     *
     * @param points body shluku
     */
    public PointSet(Collection<Point> points) {
        /* Přidání bodů do shluku. */
        super(points);
    }

    /**
     * Funkce vracející počet dimenzí bodů ve shluku.
     *
     * @return počet dimenzí shluku
     */
    public int getDimensions() {
        /* Pokud je shluk prázdný -> nula. */
        if(isEmpty()) {
            return 0;
        } else {
            /* Jinak vrať počet dimenzí. */
            return get(0).coords.length;
        }
    }

    /**
     * Funkce pro textovou reprezentaci shluku. Používá se při zápisu shluků
     * do souboru.
     *
     * @return Vrací shluk jako jede řetězec.
     */
    public String getCls() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();

        /* Pro každý bod shluku, */
        for (Point p : this) {
            /* přidej jeho jméno, */
            sb.append(p.name);
            /* a oddělovač. */
            sb.append(";");
        }

        /* Pokud není řetězec prázdný, */
        if (sb.length() > 0) {
            /* Odeber poslední oddělovač. */
            sb.deleteCharAt(sb.length() - 1);
        }

        /* Vrať řetezec shluku. */
        return sb.toString();
    }

}

