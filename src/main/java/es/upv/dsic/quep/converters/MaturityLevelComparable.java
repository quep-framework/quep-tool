/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.converters;

import es.upv.dsic.quep.model.MaturityLevel;
import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author agna8685
 */
public class MaturityLevelComparable implements Comparable<MaturityLevel> {
    MaturityLevel oMaturityLevel;    
    
    public MaturityLevelComparable(MaturityLevel ml){
        this.oMaturityLevel=new MaturityLevel();
        this.oMaturityLevel=ml;
    }

    public MaturityLevel getoMaturityLevel() {
        return oMaturityLevel;
    }

    public void setoMaturityLevel(MaturityLevel oMaturityLevel) {
        this.oMaturityLevel = oMaturityLevel;
    }    

    @Override
    public String toString() {
        return  "";
    }

    @Override
    public int compareTo(MaturityLevel o) {
        return Comparators.ABREV.compare(this.oMaturityLevel, o);
         }


    public static class Comparators {

        public static Comparator<MaturityLevel> ABREV = new Comparator<MaturityLevel>() {
            @Override
            public int compare(MaturityLevel o1, MaturityLevel o2) {
                return o1.getLevelAbbreviation().compareTo(o2.getLevelAbbreviation());
            }
        };
        public static Comparator<MaturityLevel> ID = new Comparator<MaturityLevel>() {
            @Override
            public int compare(MaturityLevel o1, MaturityLevel o2) {
                return o1.getId() - o2.getId();
            }
        };
       /* public static Comparator<Student> NAMEANDAGE = new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                int i = o1.name.compareTo(o2.name);
                if (i == 0) {
                    i = o1.age - o2.age;
                }
                return i;
            }
        };*/
    }
}
