package fr.lirmm.agroportal.ontologymappingharvester.utils;

import fr.lirmm.agroportal.ontologymappingharvester.CurationEntity;

import java.util.*;
import java.util.Map.Entry;

public class SortMapByValue
{
    public static boolean ASC = true;
    public static boolean DESC = false;

    public static void main(String[] args)
    {

        // Creating dummy unsorted map
        Map<String, Integer> unsortMap = new HashMap<String, Integer>();
        unsortMap.put("B", 55);
        unsortMap.put("A", 80);
        unsortMap.put("D", 20);
        unsortMap.put("C", 70);

        System.out.println("Before sorting......");
        printMap(unsortMap);

        System.out.println("After sorting ascending order......");
        Map<String, Integer> sortedMapAsc = sortByComparator(unsortMap, ASC);
        printMap(sortedMapAsc);


        System.out.println("After sorting descindeng order......");
        Map<String, Integer> sortedMapDesc = sortByComparator(unsortMap, DESC);
        printMap(sortedMapDesc);

    }

    public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void printMap(Map<String, Integer> map)
    {
        for (Entry<String, Integer> entry : map.entrySet())
        {
            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
    }

    public static HashMap<String,CurationEntity> sortByValues(HashMap map, boolean order) {

        // not yet sorted
        List<CurationEntity> targetsByCounter = new ArrayList<CurationEntity>(map.values());

        Collections.sort(targetsByCounter, new Comparator<CurationEntity>() {

            public int compare(CurationEntity o1, CurationEntity o2) {

                if (order)
                {
                    return o1.getCounter() - o2.getCounter();
                }
                else
                {
                    return o2.getCounter() - o1.getCounter();

                }


            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap<String,CurationEntity>();
        for (Iterator it = targetsByCounter.iterator(); it.hasNext();) {
            CurationEntity entry = (CurationEntity) it.next();
            sortedHashMap.put(entry.getTargetFounded(), entry);
        }
        return sortedHashMap;
    }
}
