/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.readers;

import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrixItem;
import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrixKey;
import cz.vutbr.fit.xkubis03.dp.structures.DistanceMatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Třída pro načítání matice vzdáleností.
 */
public class DistanceMatrixReader {

    /**
     * Funkce načítající matici.
     *
     * @param input vstupní proud souboru
     *
     * @return Vrací matici vzdáleností.
     */
    public static DistanceMatrix getMatrix(InputStream input) throws IOException {
        /* Nová matice. */
        DistanceMatrix dm = new DistanceMatrix();
        /* Most mezi bajty a znaky. */
        InputStreamReader isr = new InputStreamReader(input);
        /* Načítač řádků. */
        BufferedReader reader = new BufferedReader(isr);

        /* Řádek souboru. */
        String lin;

        /* Cyklus načítání řádků. */
        while ((lin = reader.readLine()) != null) {
            /* Rozdělovač řádku na sloupce. */
            StringTokenizer tokenizer = new StringTokenizer(lin, ";");

            /* Vložení záznamu do matice. */
            dm.put((new DistanceMatrixKey(tokenizer.nextToken(), tokenizer.nextToken())), Double.parseDouble(tokenizer.nextToken()));
        }

        /* Uzavření načítače. */
        reader.close();
        /* Uzavření mostu. */
        isr.close();

        /* Vrácení matice. */
        return dm;
    }

}

