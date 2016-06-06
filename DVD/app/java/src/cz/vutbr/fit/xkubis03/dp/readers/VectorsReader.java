/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.readers;

import cz.vutbr.fit.xkubis03.dp.structures.Vector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Třída načítající vektory.
 */
public class VectorsReader {

    /**
     * Funkce načítání vektorů ze souboru.
     *
     * @param input vstupní proud souboru
     *
     * @return Vrací seznam vektorů.
     */
    public static ArrayList<Vector> getVectors(InputStream input) throws IOException {
        /* Nový seznam vektorů. */
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        /* Most bajt - znak. */
        InputStreamReader isr = new InputStreamReader(input);
        /* Načítač řádků. */
        BufferedReader reader = new BufferedReader(isr);

        /* Řádek souboru. */
        String lin;

        /* Načítání řádků v cyklu. */
        while ((lin = reader.readLine()) != null) {
            /* Rozdělovač řádku na sloupce. */
            StringTokenizer tokenizer = new StringTokenizer(lin, ";");
            /* Počet sloupců. */
            int numberOfColumns = tokenizer.countTokens();

            /* Nový vektor. */
            Vector vector = new Vector(tokenizer.nextToken());

            /* Převod řádku na vektor. */
            for (int i = 0; i < (numberOfColumns - 1); i++) {
                vector.add(Double.parseDouble(tokenizer.nextToken()));
            }

            /* Přidání vektoru do seznamu. */
            vectors.add(vector);
        }

        /* Uzavření načítače. */
        reader.close();
        /* Uzavření mostu. */
        isr.close();

        /* Navrácení seznamu vektorů. */
        return vectors;
    }

}

