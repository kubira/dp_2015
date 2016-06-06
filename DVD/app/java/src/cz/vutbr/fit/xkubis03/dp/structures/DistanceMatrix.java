/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.HashMap;

import java.text.DecimalFormat;

/**
 * Třída pro matici vzdáleností.
 */
public class DistanceMatrix extends HashMap<DistanceMatrixKey, Double> {

    /**
     * Konstruktor prázdné matice.
     */
    public DistanceMatrix() {
    }

    /**
     * Funkce pro přidání záznamu do matice.
     *
     * @param item přidávaný záznam
     *
     * @return vrací hodnotu záznamu
     */
    public Double put(DistanceMatrixItem item) {
        /* Přidá záznam do matice. */
        put(item.key, item.value);

        /* Vrací hodnotu záznamu. */
        return item.value;
    }

    /**
     * Funkce pro textovou reprezentaci matice.
     *
     * @return Vrací řetězec matice ve formátu CSV.
     */
    @Override
    public String toString() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();
        /* Formátovač čísla. */
        DecimalFormat fmt = new DecimalFormat("0.#");

        /* Pro každý záznam matice. */
        for (DistanceMatrixKey key : keySet()) {
            /* Přidej levý klíč. */
            sb.append(key.left);
            /* Přidej oddělovač. */
            sb.append(";");
            /* Přidej pravý klíč. */
            sb.append(key.right);
            /* Přidej oddělovač. */
            sb.append(";");
            /* Přidej hodnotu. */
            sb.append(fmt.format(get(key)));
            /* Vlož nový řádek. */
            sb.append("\n");
        }

        /* Pokud není řetězec prázdný, */
        if (sb.length() > 0) {
            /* Odeber poslední konec řádku. */
            sb.deleteCharAt(sb.length() - 1);
        }

        /* Vrať řetězec s maticí. */
        return sb.toString();
    }
}

