package edu.utdallas.util;



import java.util.Enumeration;
import java.util.Vector;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;


public abstract class MultiColumnPrinter {

    final public static int  LEFT  = 0;
    final public static int  CENTER  = 1;

  
    final private static boolean  DEFAULT_SORT = true;

    private int numCol = 2;
    private int gap = 4;
    private int align = CENTER;
    private int titleAlign = CENTER;
    private String border = null;

    private Vector table = null;
    private Vector titleTable = null;
    private Vector titleSpanTable = null;
    private int curLength[];

    private boolean sortNeeded = DEFAULT_SORT;
    private int[] keyCriteria = null;

  
    public MultiColumnPrinter(int numCol, int gap, String border,
                              int align, boolean sort) {

        table = new Vector();
        titleTable = new Vector();
        titleSpanTable = new Vector();
        curLength = new int[numCol];

        this.numCol = numCol;
        this.gap = gap;
        this.border = border;
        this.align = align;
        this.titleAlign = LEFT;
        this.sortNeeded = sort;
    }

   
    public MultiColumnPrinter(int numCol, int gap, String border, int align) {
        this(numCol, gap, border, align, DEFAULT_SORT);
    }

   
    public MultiColumnPrinter(int numCol, int gap, String border) {
        this(numCol, gap, border, LEFT);
    }

 
    public MultiColumnPrinter(int numCol, int gap) {
        this(numCol, gap, null, LEFT);
    }


    public void addTitle(String[] row) {
        if (row == null)
            return;

        int[] span = new int [ row.length ];
        for (int i = 0; i < row.length; i++) {
            span[i] = 1;
        }

        addTitle(row, span);
    }

   
    public void addTitle(String[] row, int span[]) {
        // Need to create a new instance of it, otherwise the new values will
        // always overwrite the old values.

        String[] rowInstance = new String[(row.length)];
        for (int i = 0; i < row.length; i++) {
            rowInstance[i] = row[i];
        }
        titleTable.addElement(rowInstance);

        titleSpanTable.addElement(span);
    }

    public void setTitleAlign(int titleAlign)  {
        this.titleAlign = titleAlign;
    }

   
    public void add(String[] row) {
       
        String[] rowInstance = new String[(row.length)];
        for (int i = 0; i < row.length; i++) {
            rowInstance[i] = row[i];
        }
        table.addElement(rowInstance);
    }

  
    public void clearTitle()  {
        titleTable.clear();
        titleSpanTable.clear();
    }

   
    public void clear()  {
        table.clear();

        if (curLength != null)  {
            for (int i = 0; i < curLength.length; ++i)  {
                curLength[i] = 0;
            }
        }
    }

   
    public void print() {
        print(true);
    }

  
    public void print(boolean printTitle) {

   
        Enumeration elm = titleTable.elements();
        Enumeration spanEnum = titleSpanTable.elements();
        int rowNum = 0;
        while (elm.hasMoreElements()) {
            String[] row = (String[])elm.nextElement();
            int[] curSpan = (int[])spanEnum.nextElement();

            for (int i = 0; i < numCol; i++) {
               
                if (row[i] == null)
                    row[i] = "-";

                int len = row[i].length();

  
                int span = curSpan[i], rem = 0;
                if (span > 1)  {
                    rem = len % span;
                    len = len/span;
                }

                if (curLength[i] < len)  {
                    curLength[i] = len;

                    if ((span > 1) && ((i+span) <= numCol))  {
                        for (int j=i+1; j<(i+span); ++j)  {
                            curLength[j] = len;
                        }

   
                        curLength[(i+span)-1] += rem;
                    }
                }
            }
            ++rowNum;
        }

       
        elm = table.elements();
        while (elm.hasMoreElements()) {
            String[] row = (String[])elm.nextElement();
            for (int i = 0; i < numCol; i++) {

               
                if (row[i] == null)
                    row[i] = "-";

                if (curLength[i] < row[i].length())
                    curLength[i] = row[i].length();
            }
        }


        if (printTitle)  {
            printBorder();
            elm = titleTable.elements();
            spanEnum = titleSpanTable.elements();

            while (elm.hasMoreElements()) {
                String[] row = (String[])elm.nextElement();
                int[] curSpan = (int[])spanEnum.nextElement();

                for (int i = 0; i < numCol; i++) {
                    int availableSpace = 0, span = curSpan[i];

                    if (span == 0)
                        continue;

                    availableSpace = curLength[i];

                    if ((span > 1) && ((i+span) <= numCol))  {
                        for (int j=i+1; j<(i+span); ++j)  {
                            availableSpace += gap;
                            availableSpace += curLength[j];
                        }
                    }

                    if (titleAlign == CENTER)  {
                        int space_before, space_after;
                        space_before = (availableSpace-row[i].length())/2;
                        space_after = availableSpace-row[i].length() - space_before;

                        printSpaces(space_before);
                        doPrint(row[i]);
                        printSpaces(space_after);
                        if (i < numCol-1) printSpaces(gap);
                    } else  {
                        doPrint(row[i]);
                        if (i < numCol-1) printSpaces(availableSpace-row[i].length()+gap);
                    }

                }
                doPrintln("");
            }
            printBorder();
        }

        if (sortNeeded)
            printSortedTable();
        else
            printUnsortedTable();
    }

   
    private void printSortedTable() {
        // Sort the table entries
        TreeMap sortedTable = new TreeMap();
        Enumeration elm = table.elements();
        while (elm.hasMoreElements()) {
            String[] row = (String[])elm.nextElement();

            // If keyCriteria contains valid info use that
            // to create the key; otherwise, use the default row[0]
            // for the key.
            if (keyCriteria != null && keyCriteria.length > 0) {
                String key = getKey(row);
                if (key != null)
                    sortedTable.put(key, row);
                else
                    sortedTable.put(row[0], row);
            } else {
                sortedTable.put(row[0], row);
            }
        }

        
        Iterator iterator = sortedTable.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            String[] row = ((String[])entry.getValue());

            printRow(row);
        }
    }

    private String getKey(String[] row) {
        String key = "";

        for (int i = 0; i < keyCriteria.length; i++) {
            int content = keyCriteria[i];
            try {
                key = key + row[content];
            } catch (ArrayIndexOutOfBoundsException ae) {
                
                return null;
            }
        }
        return key;
    }

  
    private void printUnsortedTable() {
        Enumeration elm = table.elements();
        while (elm.hasMoreElements()) {
            String[] row = (String[])elm.nextElement();

            printRow(row);
        }
    }

    private void printRow(String[] row) {
        for (int i = 0; i < numCol; i++) {
            if (align == CENTER)  {
                int space1, space2;
                space1 = (curLength[i]-row[i].length())/2;
                space2 = curLength[i]-row[i].length() - space1;

                printSpaces(space1);
                doPrint(row[i]);
                printSpaces(space2);
                if (i < numCol-1) printSpaces(gap);
            } else  {
                doPrint(row[i]);
                if (i < numCol-1) printSpaces(curLength[i]-row[i].length()+gap);
            }
        }
        doPrintln("");
    }

   
    public void println() {
        print();
        doPrintln("");
    }

    private void printSpaces(int count)  {
        for (int i = 0; i < count; ++i)  {
            doPrint(" ");
        }
    }

    private void printBorder() {

        int colNum = 1;
        if (border == null) return;

        // For the value in each column
        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < curLength[i]; j++) {
                doPrint(border);
            }
        }

        // For the gap between each column
        for (int i = 0; i < numCol-1; i++) {
            for (int j = 0; j < gap; j++) {
                doPrint(border);
            }
        }
        doPrintln("");
    }

    public void setKeyCriteria(int[] criteria) {
        this.keyCriteria = criteria;
    }

    
    public abstract void doPrint(String str);

    
    public abstract void doPrintln(String str);
}