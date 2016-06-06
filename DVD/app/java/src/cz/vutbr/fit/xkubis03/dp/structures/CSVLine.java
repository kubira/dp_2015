/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.ArrayList;

/**
 * Třída pro jeden řádek CSV souboru.
 */
public class CSVLine extends ArrayList<String> {

    /**
     * Konstruktor prázdného řádku.
     */
    public CSVLine() {
    }

    /**
     * Metoda vracející počet položek řádku.
     *
     * @return Vrací počet položek řádku.
     */
    public int getLength() {
        /* Pokud je řádek prázdný -> nula. */
        if (isEmpty()) {
            return 0;
        } else {
        /* Jinak vrací počet položek. */
            return size();
        }
    }

    /**
     * Metoda vracející řetězec řádku.
     *
     * @return Řetězec celého řádku.
     */
    @Override
    public String toString() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();

        /* Pro každý prvek řádku. */
        for (int i = 0; i < size(); i++) {
            /* Přidej položku do řetězce. */
            sb.append(get(i));

            /* Pokud není poslední, */
            if (i < (size() - 1)) {
                /* přidej oddělovač. */
                sb.append(";");
            }
        }

        /* Vrácení CSV řádku. */
        return sb.toString();
    }

}

