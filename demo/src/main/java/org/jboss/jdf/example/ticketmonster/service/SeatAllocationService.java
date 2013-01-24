package org.jboss.jdf.example.ticketmonster.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.JBossTransactionManagerLookup;
import org.jboss.jdf.example.ticketmonster.model.*;

/**
 *
 * Helper service for allocation seats.
 *
 * @author Marius Bogoevici
 */
public class SeatAllocationService {


    public static final String ALLOCATIONS = "TICKETMONSTER_ALLOCATIONS";

    private Cache<SectionAllocationKey, SectionAllocation> cache;

    /**
     * We inject the {@link EmbeddedCacheManager} and retri
     *
     * @param manager
     */
    @Inject
    public SeatAllocationService(EmbeddedCacheManager manager) {
        Configuration allocation = new ConfigurationBuilder()
                .transaction().transactionMode(TransactionMode.TRANSACTIONAL)
                .transactionManagerLookup(new JBossTransactionManagerLookup())
                .lockingMode(LockingMode.PESSIMISTIC)
                .loaders().addFileCacheStore().purgeOnStartup(true)
                .build();
        manager.defineConfiguration(ALLOCATIONS, allocation);
        this.cache = manager.getCache(ALLOCATIONS);
    }

    public AllocatedSeats allocateSeats(Section section, Performance performance,
                                        int seatCount, boolean contiguous) {
        SectionAllocationKey sectionAllocationKey = SectionAllocationKey.of(section, performance);
        SectionAllocation allocation = getSectionAllocation(sectionAllocationKey);
        ArrayList<Seat> seats = allocation.allocateSeats(seatCount, contiguous);
        cache.replace(sectionAllocationKey, allocation);
        return new AllocatedSeats(allocation, seats);
    }

    public void deallocateSeats(Section section, Performance performance, List<Seat> seats) {
        SectionAllocationKey sectionAllocationKey = SectionAllocationKey.of(section, performance);
        SectionAllocation sectionAllocation = getSectionAllocation(sectionAllocationKey);
        for (Seat seat : seats) {
            if (!seat.getSection().equals(section)) {
                throw new SeatAllocationException("All seats must be in the same section!");
            }
            sectionAllocation.deallocate(seat);
        }
        cache.replace(sectionAllocationKey, sectionAllocation);
    }

    public void finalizeAllocation(AllocatedSeats allocatedSeats) {
        allocatedSeats.markOccupied();
    }

    /**
     * Mark the allocat
     * @param performance
     * @param allocatedSeats
     */
    public void finalizeAllocation(Performance performance, List<Seat> allocatedSeats) {
        SectionAllocation sectionAllocation = cache.get(
                SectionAllocationKey.of(allocatedSeats.get(0).getSection(), performance));
        sectionAllocation.markOccupied(allocatedSeats);
    }

    /**
     * Retrieve a {@link SectionAllocation} instance for a given {@link Performance} and
     * {@link Section} (embedded in the {@link SectionAllocationKey}). Lock it for the scope
     * of the current transaction.
     *
     * @param sectionAllocationKey - wrapper for a {@link Performance} and {@link Section} pair
     *
     * @return the corresponding {@link SectionAllocation}
     */
    private SectionAllocation getSectionAllocation(SectionAllocationKey sectionAllocationKey) {
        SectionAllocation newAllocation = new SectionAllocation(sectionAllocationKey.getPerformance(),
                sectionAllocationKey.getSection());
        SectionAllocation sectionAllocation = cache.putIfAbsent(sectionAllocationKey,
                newAllocation);
        cache.getAdvancedCache().lock(sectionAllocationKey);
        return sectionAllocation == null?newAllocation:sectionAllocation;
    }
}
