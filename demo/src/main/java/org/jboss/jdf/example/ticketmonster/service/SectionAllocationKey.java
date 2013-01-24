package org.jboss.jdf.example.ticketmonster.service;

import org.jboss.jdf.example.ticketmonster.model.Performance;
import org.jboss.jdf.example.ticketmonster.model.Section;

import java.io.Serializable;

/**
 * Utility class for storing {@link org.jboss.jdf.example.ticketmonster.model.SectionAllocation}
 * objects in the datagrid.
 *
 * @author Marius Bogoevici
 */
public class SectionAllocationKey implements Serializable {

    private final Section section;
    private final Performance performance;

    private SectionAllocationKey(Section section, Performance performance) {

        this.section = section;
        this.performance = performance;
    }

    public static SectionAllocationKey of (Section section, Performance performance) {
        return new SectionAllocationKey(section, performance);
    }


    public Section getSection() {
        return section;
    }

    public Performance getPerformance() {
        return performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectionAllocationKey that = (SectionAllocationKey) o;

        if (performance != null ? !performance.equals(that.performance) : that.performance != null) return false;
        if (section != null ? !section.equals(that.section) : that.section != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = section != null ? section.hashCode() : 0;
        result = 31 * result + (performance != null ? performance.hashCode() : 0);
        return result;
    }
}
