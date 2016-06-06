/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.text.DecimalFormat;

/**
 * Třída pro záznam v tabulce vzdáleností.
 */
public class DistanceMatrixItem {

    /** Složený klíč záznamu. */
    protected DistanceMatrixKey key;
    /** Hodnota v tabulce. */
    protected double value;

    /**
     * Konstruktor záznamu.
     *
     * @param složený klíč
     * @param hodnota záznamu
     */
    public DistanceMatrixItem(DistanceMatrixKey key, double value) {
        /* Uložení klíče. */
        this.key = key;
        /* Uložení hodnoty. */
        this.value = value;
    }

    /**
     * Funkce vracející textovou reprezentaci záznamu.
     *
     * @return Vrací jeden záznam tabulky jako CSV.
     */
    @Override
    public String toString() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();
        /* Formátovač čísla. */
        DecimalFormat fmt = new DecimalFormat("0.#");

        /* Přidání levého klíče. */
        sb.append(key.left);
        /* Přidání oddělovače. */
        sb.append(";");
        /* Přidání pravého klíče. */
        sb.append(key.right);
        /* Přidání oddělovače. */
        sb.append(";");
        /* Přidání hodnoty. */
        sb.append(fmt.format(value));

        /* Vrácení řetězce. */
        return sb.toString();
    }

}

