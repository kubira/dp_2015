/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

/**
 * Třída pro uchování propojené informace FASTA záznam + vektor.
 */
public class FitClustItem {

    /** FASTA záznam sekvence. */
    protected FASTAItem fastaItem;
    /** Vektor sekvence. */
    protected Vector vector;

    /**
     * Konstruktor třídy propojení.
     *
     * @param fi FASTA záznam
     * @param v vektor
     */
    public FitClustItem(FASTAItem fi, Vector v) {
        /* Uložení FASTA záznamu. */
        this.fastaItem = fi;
        /* Uložení vektoru. */
        this.vector = v;
    }

    /**
     * Funkce vracející vzdálenost dvou propojení.
     *
     * @param fi objekt pro porovnání
     *
     * @return vrací vzdálenost vektorů propojení
     */
    public double getDistance(FitClustItem fi) {
        /* Vrácení vzdálenosti. */
        return vector.getDistance(fi.vector);
    }

    /**
     * Metoda vracející FASTA záznam objektu.
     *
     * @param Vrací FASTA záznam.
     */
    public FASTAItem getFASTAItem() {
        /* Vrácení FASTA záznamu. */
        return fastaItem;
    }

    /**
     * Metoda vracející vektor objektu.
     *
     * @return Vrací vektor.
     */
    public Vector getVector() {
        /* Vrácení vektoru. */
        return vector;
    }

}
