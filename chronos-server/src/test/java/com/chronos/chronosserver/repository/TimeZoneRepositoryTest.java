package com.chronos.chronosserver.repository;

import com.chronos.chronosserver.model.TimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TimeZoneRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    @Test
    public void whenFindAllByCreatedBy_thenReturnTimeZones() {
        TimeZone bengaluruTimeZone = new TimeZone();
        bengaluruTimeZone.setTimeZoneName("Bengaluru Time Zone");
        bengaluruTimeZone.setLocationName("Bengaluru, India");
        bengaluruTimeZone.setDifferenceFromGmt("+05:30");
        bengaluruTimeZone.setCreatedBy("varun");
        entityManager.persist(bengaluruTimeZone);
        entityManager.flush();

        List<TimeZone> timeZones = timeZoneRepository.findAllByCreatedBy("varun");

        assertEquals(1, timeZones.size());
        assertEquals(bengaluruTimeZone.getTimeZoneName(), timeZones.get(0).getTimeZoneName());
    }
}
