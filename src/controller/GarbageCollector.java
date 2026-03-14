package controller;

import model.value.RefValue;
import model.value.Value;

import java.util.*;
import java.util.stream.Collectors;

public final class GarbageCollector {
    //extract all heap addresses from SymbolTable value
    public static List<Integer> getAddressFromSymTable(Collection<Value> symTableValues){
        return symTableValues.stream()
                .filter(v->v instanceof RefValue)
                .map(v->((RefValue) v).getAddress())
                .collect(Collectors.toList());
    }

    // Compute all reachable addresses (symbol table + heap references)
    public static List<Integer> getAllReachableAddresses(Collection<Value> symTableValues, Map<Integer,Value> heapContent){
        // Get initial roots from symbol table
        List<Integer> workList = getAddressFromSymTable(symTableValues);
        Set<Integer> reachable = new HashSet<>(workList);
        List<Integer> heapAddresses = getAddressFromSymTable(heapContent.values());
        //BFS through heap references
        while(!workList.isEmpty()){
            int currentAddress=workList.remove(0);
            Value value=heapContent.get(currentAddress);

            if( value instanceof RefValue refValue){
                int nextAddress=refValue.getAddress();
                if(!reachable.contains(nextAddress)){
                    reachable.add(nextAddress);
                    workList.add(nextAddress);
                }


            }
        }
        return new ArrayList<>(reachable);
    }


    public static Map<Integer,Value> safeGarbageCollector(Collection<Value> symTableValues, Map<Integer, Value> heapContent){
            List<Integer> reachable=getAllReachableAddresses(symTableValues, heapContent);

            return heapContent.entrySet().stream()
                .filter(entry -> reachable.contains(entry.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }


}
