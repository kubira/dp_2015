/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.fitclust;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

import cz.vutbr.fit.xkubis03.dp.structures.FASTAItem;

/**
 * Třída zajišťující zarovnání dvou sekvencí zadaným algoritmem.
 */
public class Alignment {

    /**
     * Funkce zarovnávající sekvence podle zadaného příkazu.
     *
     * @param i1 první sekvence pro zarovnání
     * @param i2 druhá sekvence pro zariovnání
     * @param cmd příkaz pro zarovnání
     *
     * @return Vrací podobnost dvou sekvencí.
     */
    private static double align(FASTAItem i1, FASTAItem i2, String[] cmd) throws Exception, IOException, InterruptedException {
        /* Porovnání délky sekvencí,
         * první sekvence musí být větší.
         */
        if (i1.length() < i2.length()) {
            /* Pokud není první sekvence delší,
             * sekvence se prohodí.
             */
            FASTAItem help = i1;
            i1 = i2;
            i2 = help;
        }

        /* Zápis první sekvence ve formátu FASTA do souboru f1.fa. */
        PrintWriter writer = new PrintWriter("f1.fa", "UTF-8");
        writer.print(i1.toString());
        writer.close();

        /* Zápis druhé sekvence ve formátu FASTA do souboru f2.fa. */
        writer = new PrintWriter("f2.fa", "UTF-8");
        writer.print(i2.toString());
        writer.close();

        /* Získání runtime programu. */
        Runtime rt = Runtime.getRuntime();

        /* Spuštění procesu s příkazem. */
        Process pr = rt.exec(cmd);
        /* Čekání na ukončení procesu. */
        pr.waitFor();
        /* Získání návratové hodnoty procesu. */
        int retVal = pr.exitValue();

        /* Pokud proběhl proces v pořádku. */
        if (retVal == 0) {
            /* Vytvoření vstupního streamu procesu. */
            InputStreamReader isr = new InputStreamReader(pr.getInputStream());
            /* Vytvoření readeru vstupu. */
            BufferedReader input = new BufferedReader(isr);
            try {
                /* Převod navrácené hodnoty zarovnání na double. */
                double max = Double.parseDouble(input.readLine());
                /* Uzavření všech souborů. */
                input.close();
                isr.close();
                pr.destroy();

                /* Navrácení zarovnání. */
                return max;
            } catch (NullPointerException e) {
                /* Pokud nastala chyba při čtení hodnoty,
                 * zavřou se všechny soubory.
                 */
                input.close();
                isr.close();
                pr.destroy();

                /* Zarovnání je nulové. */
                return 0.0;
            }
        } else {
            /* Pokud neproběhl proces v pořádku - výjimka. */
            throw new Exception("ERROR: Alignment command failure.");
        }
    }

    /**
     * Funkce zarovnávající sekvence algoritmem FASTA.
     *
     * @param i1 první sekvence pro zarovnání
     * @param i2 druhá sekvence pro zarovnání
     *
     * @return Vrací hodnotu zarovnání FASTA.
     */
    public static double alignFASTA(FASTAItem i1, FASTAItem i2) throws Exception, IOException, InterruptedException {
        /* Příkaz pro FASTA zarovnání. */
        String[] cmd = {
            "./fasta"
        };

        /* Navrácení hodnoty v procentech. */
        return (100.0 - (align(i1, i2, cmd) * 100.0));
    }

    /**
     * Funkce zarovnávající sekvence algoritmem BLAST.
     *
     * @param i1 první sekvence pro zarovnání
     * @param i2 druhá sekvence pro zarovnání
     *
     * @return Vrací hodnotu zarovnání BLAST.
     */
    public static double alignBLAST(FASTAItem i1, FASTAItem i2) throws Exception, IOException, InterruptedException {
        /* Příkaz pro FASTA zarovnání. */
        String[] cmd = {
            "./blast"
        };

        /* Navrácení hodnoty v procentech. */
        return (100.0 - align(i1, i2, cmd));
    }

}

