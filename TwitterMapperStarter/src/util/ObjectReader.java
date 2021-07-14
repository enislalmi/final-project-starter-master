package util;

import java.io.*;

/**
 * Read objects from a file
 * changed the name from object source to object reader, since we are only using this class to read data,
 * it may be misleading to call it object source
 */
public class ObjectReader {
    private File file;
    private ObjectInputStream inputstream;

    public ObjectReader(String filename)  {
        file = new File(filename);
        try {
            inputstream = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readObject() {
        Object o = null;
        try {
            o = inputstream.readObject();
        } catch (EOFException e) {
            // Do nothing, EOF is expected to happen eventually
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }

    public void close() {
        try {
            inputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
