package pl.edu.agh.pockettrainer.program.repository.program;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.edu.agh.pockettrainer.program.domain.Metadata;

public class Sorter {

    public static List<Program> sortedByName(List<Program> programs) {
        Collections.sort(programs, new Comparator<Program>() {
            @Override
            public int compare(Program a, Program b) {

                final Metadata m1 = a.getMetadata();
                final Metadata m2 = b.getMetadata();

                if (m1 != null && m2 != null) {
                    final String name1 = m1.getName();
                    final String name2 = m2.getName();
                    if (name1 != null) {
                        return name1.compareTo(name2);
                    }
                }

                return 0;
            }
        });
        return programs;
    }
}
