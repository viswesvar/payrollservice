package dev.chiaradia.payrollservice.service.cache;

import dev.chiaradia.payrollservice.PayrollserviceApplication;
import dev.chiaradia.payrollservice.domainobject.Payslip;
import dev.chiaradia.payrollservice.service.PayslipService;
import dev.chiaradia.payrollservice.service.client.PayslipProApiClient;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
* This class was created in Java with the purpose of validating the @Cacheable annotation.
*
* */
@ContextConfiguration(classes = {PayrollserviceApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CachedPayslipServiceTest
{

    @MockBean
    private PayslipProApiClient payslipProApiClient;

    @Autowired
    private PayslipService cachedPayslipService;


    @Test
    public void givenAPayslipsRequest_whenRepeated_thenPayslipProApiClientShouldBeCalledOnce()
    {
        String payslipLine = "00000000000197084172E201812310024860005000001243012000002983200206337";

        when(payslipProApiClient.getPayslipsByMonthAndYear(anyLong(), anyLong())).thenReturn(payslipLine);

        List<Payslip> payslips = cachedPayslipService.getPayslips(12, 2018);
        List<Payslip> payslips2 = cachedPayslipService.getPayslips(12, 2018);

        verify(payslipProApiClient, times(1)).getPayslipsByMonthAndYear(anyLong(), anyLong());
        assertEquals(1, payslips.get(0).getId());
        assertEquals(1, payslips2.get(0).getId());
    }

    @Test
    public void givenAUpdatePayslipsTaxRequest_whenRepeated_thenPayslipProApiClientShouldBeCalledOnce()
    {
        String payslipLine = "00000000000197084172E201812310024860005000001243012000002983200206337";

        when(payslipProApiClient.getPayslipsByMonthAndYear(anyLong(), anyLong())).thenReturn(payslipLine);

        List<Payslip> payslips = cachedPayslipService.updateTaxRate(12, 2018, 10.0);
        List<Payslip> payslips2 = cachedPayslipService.updateTaxRate(12, 2018, 10.0);

        verify(payslipProApiClient, times(1)).getPayslipsByMonthAndYear(anyLong(), anyLong());
        assertEquals(1, payslips.get(0).getId());
        assertEquals(1, payslips2.get(0).getId());
    }


    @Configuration
    @EnableCaching
    @ComponentScan("dev.chiaradia.payrollservice.service.cache")
    static class TestCacheConfiguration
    {

        @Bean
        SimpleCacheManager cacheManager()
        {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            List<Cache> caches = new ArrayList<>();
            caches.add(cachePayslipsBean().getObject());
            caches.add(cacheSimulatedPayslipsBean().getObject());
            cacheManager.setCaches(caches);
            return cacheManager;
        }


        @Bean
        ConcurrentMapCacheFactoryBean cachePayslipsBean()
        {
            ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
            cacheFactoryBean.setName(CachedPayslipService.CACHE_PAYSLIPS);
            return cacheFactoryBean;
        }


        @Bean
        ConcurrentMapCacheFactoryBean cacheSimulatedPayslipsBean()
        {
            ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
            cacheFactoryBean.setName(CachedPayslipService.CACHE_SIMULATED_PAYSLIPS);
            return cacheFactoryBean;
        }

    }
}
