/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.structures;

/**
 * Tříba pro jeden FASTA záznam.
 */
public class FASTAItem implements Comparable<FASTAItem> {

    /** Hlavička záznamu. */
    protected String head;
    /** Název sekvence. */
    protected String name;
    /** Sekvence aminokyselin. */
    protected String sequence;

    /**
     * Konstruktor pro FASTA záznam.
     *
     * @param head hlavička záznamu.
     * @param sequence sekvence záznamu.
     */
    public FASTAItem(String head, String sequence) {
        /* Uložení hlavičky. */
        this.head = head;
        /* Uložení sekvence. */
        this.sequence = sequence;

        /* Extrakce názvu sekvence - přibližná. */
        this.name = (new String(head)).split(" ")[0].substring(1);
    }

    /**
     * Funkce pro porovnání dvou FASTA záznamů podle délky sekvence.
     *
     * @param item záznam, se kterým porovnávám.
     *
     * @return Vrací 0, pokud jsou sekvence stejně dlouhé. Záporné číslo, pokud
     * je item kratší, jinak kladné číslo.
     */
    public int compareTo(FASTAItem item) {
        /* Navrácení rozdílu délek. */
        return (item.sequence.length() - sequence.length());
    }

    /**
     * Metoda vracející délku sekvence.
     *
     * @return Vrací délku sekvence.
     */
    public int length() {
        /* Navrácení délky sekvence. */
        return sequence.length();
    }

    /**
     * Metoda vracející řetězcovou reprezentaci záznamu - FASTA formát.
     *
     * @return Vrací FASTA záznam sekvence.
     */
    @Override
    public String toString() {
        /* Tvořič řetězce. */
        StringBuilder sb = new StringBuilder();

        /* Vložení hlavičky. */
        sb.append(head);
        /* Vložení nového řádku. */
        sb.append("\n");
        /* Vložení sekvence. */
        sb.append(sequence);

        /* Vrací FASTA záznam. */
        return sb.toString();
    }

    /**
     * Metoda vracející název záznamu.
     *
     * @return Vrací název záznamu.
     */
    public String getName() {
        /* Vrácení názvu. */
        return name;
    }

}

