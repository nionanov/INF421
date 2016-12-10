import java.util.*;

class Main {

    public static void test1(String s1,String s2) {
        EditDistance ed = new EditDistance(3,2,1);
        int[][] d = ed.editDistance(s1,s2);
        System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,1)");
        System.out.println("Cost = "+d[s1.length()][s2.length()]);

        ed = new EditDistance(3,2,6);
        d = ed.editDistance(s1,s2);
        System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,6)");
        System.out.println("Cost = "+d[s1.length()][s2.length()]);
    }


    public static void test2(String s1,String s2) {
        EditDistance ed = new EditDistance(3,2,1);
        int[][] d = ed.editDistance(s1,s2);
        System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,1)");
        System.out.println("Cost = "+d[s1.length()][s2.length()]);
        int tot = 0;
        for (int i = 0; i <= s1.length(); i++)
            for (int j = 0; j <= s2.length(); j++)
                if (d[i][j] != -1) tot++;
        System.out.println("Values Computed = "+tot);

        ed = new EditDistance(3,2,6);
        d = ed.editDistance(s1,s2);
        System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,6)");
        System.out.println("Cost = "+d[s1.length()][s2.length()]);
        tot = 0;
        for (int i = 0; i <= s1.length(); i++)
            for (int j = 0; j <= s2.length(); j++)
                if (d[i][j] != -1) tot++;
        System.out.println("Values Computed = "+tot);
    }
    
    public static void test3(String s1,String s2) {
        EditDistance ed = new EditDistance(3,2,1);
        List<String> m = ed.minimalEdits(s1,s2);
        System.out.println("Minimal edits from "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,1):");
        for (String s: m) System.out.println(s);

        ed = new EditDistance(3,2,6);
        m = ed.minimalEdits(s1,s2);
        System.out.println("Minimal edits from "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,6):");
        for (String s: m) System.out.println(s);
    }

    public static void test4() {
        EditDistance ed = new EditDistance(3,2,1);
        String s1 = "Mr. Sherlock Holmes, who was usually very late in the mornings, save upon those not infrequent occasions when he was up all night, was seated at the breakfast table. I stood upon the hearth-rug and picked up the stick which our visitor had left behind him the night before. It was a fine, thick piece of wood, bulbous-headed, of the sort which is known as a \"Penang lawyer.\" Just under the head was a broad silver band nearly an inch across. \"To James Mortimer, M.R.C.S., from his friends of the C.C.H.,\" was engraved upon it, with the date \"1884.\" It was just such a stick as the old-fashioned family practitioner used to carry -- dignified, solid, and reassuring.";
        String s2 = "Chapter I. Mr. Sherlock Holmes, who was usually very late in the mornings, save upon those not infrequent occasions when he was up all night, was seated at the breakfast table. I stood upon the hearth-rug and picked up the walking stick which our visitor had left behind him the night before. It was a fine, thick piece of wood, of the sort which is known as a \"Penang lawyer.\" Just under the head was a broad silver band nearly an inch across. \"To James Mortimer, M.R.C.S., from his friends of the C.C.H.,\" was engraved upon it, with the date \"1984.\" It was just such a stick as the old-fashioned family practitioner used to carry -- dignified, solid, and reassuring. \"Well, Watson, what do you make of it?\"";

        int[][] d = ed.editDistance(s1,s2);
        System.out.println("Transforming "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,6)");
        System.out.println("Cost = "+d[s1.length()][s2.length()]);
        
        List<String> m = ed.minimalEdits(s1,s2);
        System.out.println("Minimal edits from "+s1+" to "+s2+" with (c_i,c_d,c_r) = (3,2,1):");
        for (String s: m) System.out.println(s);
    }
    
    public static void main(String[] args) {
        String s1 = "abcd";
        String s2 = "adcb";
//        test1(s1,s2);
//
//        test2(s1,s2);
//        test3(s1,s2);
        test4();
        
    }

}
