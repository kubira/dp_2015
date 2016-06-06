/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.readers;

import cz.vutbr.fit.xkubis03.dp.structures.FASTAItem;
import cz.vutbr.fit.xkubis03.dp.structures.FASTAItemComparator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Třída načítající FASTA soubor.
 */
public class FASTAReader {

    /**
     * Funkce pro načtení FASTA souboru.
     *
     * @param input vstupní proud souboru
     *
     * @return Vrací seznam FASTA záznamů.
     */
    public static ArrayList<FASTAItem> getSequences(InputStream input) throws Exception, IOException {
        /* Seznam záznamů. */
        ArrayList<FASTAItem> seqs = new ArrayList<FASTAItem>();
        /* Most mezi bajty a znaky. */
        InputStreamReader isr = new InputStreamReader(input);
        /* Načítač řádků. */
        BufferedReader reader = new BufferedReader(isr);

        /* Řádek souboru. */
        String lin;
        /* FASTA hlavička. */
        String head = null;
        /* Sekvence záznamu. */
        String seq = "";
        /* Délka aktuální sekvence - pro řazení dle délky. */
        int actualLength = Integer.MAX_VALUE;
        /* Logicka hodnota, zda je vstup seřazen. */
        boolean isSorted = true;

        System.out.print("\rLoaded: "+seqs.size());

        /* Cyklus načítání řádků. */
        while ((lin = reader.readLine()) != null) {
            /* Přeskočení prázdných řádků. */
            if (lin.trim().equals("")) {
                continue;
            }

            /* Pokud narazím na hlavičku. */
            if (lin.charAt(0) == '>') {
                /* Kontrola pořadí hlavička - sekvence. */
                if (head != null && !seq.equals("")) {
                    /* Kontrola délky sekvence. */
                    if (isSorted && seq.length() <= actualLength) {
                        /* Nastavení aktuální délky. */
                        actualLength = seq.length();
                    } else {
                        /* Pokud nejsou seřazeny v jednom místě - bude se řadit */
                        isSorted = false;
                    }
                    /* Přidání FASTA záznamu do seznamu. */
                    seqs.add(new FASTAItem(head, seq));
                    System.out.print("\rLoaded: "+seqs.size());

                    /* Vyprázdnění sekvence. */
                    seq = "";
                } else if (head != null && seq.equals("")) {
                    /* Dvě hlavičky za sebou - chyba! */
                    throw new Exception("ERROR:Bad FASTA file format.");
                }
                /* Uložení nové hlavičky. */
                head = lin.trim();
            } else {
                /* Pokud není řádek hlavička, */
                if (head != null) {
                    /* přidám sekvenci do aktuální sekvence. */
                    seq += lin.trim();
                } else {
                    /* Hlavička nepředcházela sekvenci - chyba! */
                    throw new Exception("ERROR:Bad FASTA file format.");
                }
            }
        }

        /* Kontrola poslední sekvence v souboru */
        if (head != null && !seq.equals("")) {
            /* Kontrola délky. */
            if (seq.length() > actualLength) {
                isSorted = false;
            }
            /* Přidání sekvence. */
            seqs.add(new FASTAItem(head, seq));
        } else {
            /* Poslední záznam chybný! */
            throw new Exception("ERROR:Bad FASTA file format.");
        }

        System.out.println("\rLoaded: "+seqs.size());

        /* Pokud nejsou řazeny dle délky, */
        if (!isSorted) {
            System.out.println("Sorting "+seqs.size()+" sequences...");
            /* seřadím - vyžaduje to algoritmus. */
            Collections.sort(seqs, (new FASTAItemComparator()));
            System.out.println(seqs.size()+" sequences sorted.");
        } else {
            System.out.println("Sequences already sorted.");
        }

        /* Uzavřu načítač. */
        reader.close();
        /* Uzavřu most. */
        isr.close();

        /* Vracím seznam sekvencí. */
        return seqs;
    }

}

