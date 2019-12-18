package Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class TableXY
{
    public static final char SEPERATOR = ',';

    public static void write(String name, String[] header, ArrayList<Point> points) {
        try {
            FileWriter writer = new FileWriter(name);

            writeHeader(writer, header);

            for (int i = 0; i < points.size(); i ++) {
                writeLine(writer, points.get(i).toArray());
            }

            writer.flush();
            writer.close();
        } catch(IOException e) {
            System.out.println("Error writing file " + name);
        }
    }

    public static void write(String name, String[] header, double[][] data) {
        try {
            FileWriter writer = new FileWriter(name);

            writeHeader(writer, header);

            for (int i = 0; i < data.length; i ++) {
                writeLine(writer, data[i]);
            }

            writer.flush();
            writer.close();
        } catch(IOException e) {
            System.out.println("Error writing file " + name);
        }
    }

    public static void write(String name, String[] header, String[][] data) {
        try {
            FileWriter writer = new FileWriter(name);

            writeHeader(writer, header);

            for (int i = 0; i < data.length; i ++) {
                writeHeader(writer, data[i]);
            }

            writer.flush();
            writer.close();
        } catch(IOException e) {
            System.out.println("Error writing file " + name);
        }
    }

    private static void writeLine(Writer w, double[] row) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < row.length; i ++) {
            if (!first)
                sb.append(SEPERATOR);

            sb.append(row[i]);
            first = false;
        }
        w.append(sb.toString());
        w.append('\n');
    }

    private static void writeHeader(Writer w, String[] header) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 0; i < header.length; i ++) {
            if (!first)
                sb.append(SEPERATOR);

            sb.append(header[i]);
            first = false;
        }
        w.append(sb.toString());
        w.append('\n');
    }

    
}