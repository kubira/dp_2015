/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

import java.util.Comparator;

/**
 * Třída komparátoru dvou FASTA záznamů dle jejich délky.
 */
public class FASTAItemComparator implements Comparator<FASTAItem> {

    /**
     * Implementace porovnání vychází z délky FASTA záznamu.
     *
     * @param i1 první sekvence
     * @param i2 druhá sekvence
     *
     * @return Vrací 0 při stejné délce, záporné číslo když je i2 kratší, jinak
     * kladné číslo.
     */
    @Override
    public int compare(FASTAItem i1, FASTAItem i2) {
        /* Volání porovnávací funkce třídy FASTAItem. */
        return i1.compareTo(i2);
    }

}

