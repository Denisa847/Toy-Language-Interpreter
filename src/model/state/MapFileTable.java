package model.state;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements FileTable{
    private Map<String, BufferedReader> fileTable = new HashMap<>();

    @Override
    public boolean isOpen(String fileName) {
        return fileTable.containsKey(fileName);
    }

    @Override
    public void addOpenFile(String fileName, BufferedReader bufferReader) {
        fileTable.put(fileName, bufferReader);
    }

    @Override
    public BufferedReader getOpenFile(String fileName) {
        return fileTable.get(fileName);
    }

    @Override
    public void closeFile(String fileName) {
        try {
            fileTable.remove(fileName).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        if (fileTable.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{\n");
        for (String fileName : fileTable.keySet()) {
            sb.append("   ").append(fileName).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Map<String,BufferedReader> getContent(){
        return fileTable;
    }
}