package br.ce.wcaquino.taskbackend.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class DateUtilsTests {

    @Test
    public void deveRetornarTrueParaDatasFuturas(){
        LocalDate now = LocalDate.now();
        boolean result = DateUtils.isEqualOrFutureDate(now.plusDays(1));
        assertTrue(result);
    }

    @Test
    public void deveRetornarTrueParaDataDeHoje(){
        LocalDate now = LocalDate.now();
        boolean result = DateUtils.isEqualOrFutureDate(now);
        assertTrue(result);
    }

    @Test
    public void deveRetornarFalseParaDatasPassadas(){
        LocalDate now = LocalDate.now();
        boolean result = DateUtils.isEqualOrFutureDate(now.minusDays(1));
        assertFalse(result);
    }

    
}
