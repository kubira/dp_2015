/**
 * Diplomová práce na FIT VUT Brno, 2015
 * SHLUKOVÁNÍ BIOLOGICKÝCH SEKVENCÍ
 * @author Bc. Radim KUBIŠ, xkubis03 
 */

package cz.vutbr.fit.xkubis03.dp.readers;

import cz.vutbr.fit.xkubis03.dp.structures.CSVLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Třída pro načítání CSV souboru.
 */
public class CSVReader {

    /**
     * Funkce vracející seznam načtených řádků souboru.
     *
     * @param input vstupní proud souboru
     *
     * @return Vrací seznam CSV řádků.
     */
    public static ArrayList<CSVLine> getLines(InputStream input) throws IOException {
        /* Seznam řádků - inicializace. */
        ArrayList<CSVLine> lines = new ArrayList<CSVLine>();
        /* Most mezi bajty a znaky. */
        InputStreamReader isr = new InputStreamReader(input);
        /* Načítání po řádcích. */
        BufferedReader reader = new BufferedReader(isr);

        /* Řádek souboru. */
        String lin;

        /* Cyklus načítání řádků. */
        while ((lin = reader.readLine()) != null) {
            /* Rozdělení řádku do sloupců. */
            StringTokenizer tokenizer = new StringTokenizer(lin, ";");
            /* Počet sloupců. */
            int numberOfColumns = tokenizer.countTokens();

            /* Nový CSV řádek. */
            CSVLine csvLine = new CSVLine();

            /* Cyklus přidávání hodnot na řádku. */
            for (int i = 0; i < numberOfColumns; i++) {
                /* Přidání jedné hodnoty. */
                csvLine.add(tokenizer.nextToken());
            }

            /* Přidání řádku do seznamu. */
            lines.add(csvLine);
        }

        /* Uzavření načítače. */
        reader.close();
        /* Uzavření mostu. */
        isr.close();

        /* Vrácení seznamu řádků. */
        return lines;
    }

}

