package model.state;

import java.io.BufferedReader;
import java.util.Map;

public interface FileTable {
    boolean isOpen(String fileName);

    void addOpenFile(String fileName, BufferedReader bufferReader);

    BufferedReader getOpenFile(String fileName);

    void closeFile(String fileName);
    Map<String,BufferedReader> getContent();
}
