package com.udacity.pricing;

import com.google.gson.Gson;

import com.udacity.pricing.api.PricingController;
import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;

import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PricingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {PricingController.class})
public class PricingServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PricingService pricingService;

//    public void setUp(){
//
//    }

    Price price = new Price("CAD",new BigDecimal("17000"), 19L);

    String priceJSON = new Gson().toJson(price);

    @Test
    public void getPriceById() throws Exception{

        mockMvc.perform(get("/services/price?vehicleId="+19)
        .content(priceJSON)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());
    }

    @Test
    public void getPriceById_NotFound() throws Exception{
        when(pricingService.getPriceFromDB(anyLong())).thenThrow(PriceException.class);
        mockMvc.perform(get("/services/price?vehicleId="+119)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
