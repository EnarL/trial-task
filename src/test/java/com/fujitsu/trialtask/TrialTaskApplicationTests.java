package com.fujitsu.trialtask;

import com.fujitsu.trialtask.Repository.AirTemperatureExtraFeeRepository;
import com.fujitsu.trialtask.Repository.RegionalBaseFeeRepository;
import com.fujitsu.trialtask.Repository.WeatherPhenomenonExtraFeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class TrialTaskApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RegionalBaseFeeRepository rbfRepository;

	@Autowired
	private AirTemperatureExtraFeeRepository atefRepository;

	@Autowired
	private WeatherPhenomenonExtraFeeRepository wpefRepository;

	@Test
	void testCalculateRbf() throws Exception {
		performRequestAndAssertValue("/api/calculateRbf", "Tallinn", "Car", 4.0);
	}
	@Test
	void testCalculateRbf_EmptyStrings() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/calculateRbf")
						.param("city", "")
						.param("vehicleType", ""))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testCalculateRbf_InvalidDateTimeFormat() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/calculateRbf")
						.param("city", "Tallinn")
						.param("vehicleType", "Car")
						.param("datetime", "invalidDateTime"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testCalculateRbf_NonExistingWeatherData() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/calculateRbf")
						.param("city", "Tallinn")
						.param("vehicleType", "Car")
						.param("datetime", "1900-01-01 00:00:00"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testCalculateRbfWithDateTime() throws Exception {
		performRequestAndAssertValue("/api/calculateRbf", "Tallinn", "Bike", 0.0, "2024-03-08 12:00:00");
	}

	@Test
	void testCalculateRbf_InvalidCity() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/calculateRbf")
						.param("city", "InvalidCity")
						.param("vehicleType", "Car"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testCalculateRbf_InvalidVehicle() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/calculateRbf")
						.param("city", "Tallinn")
						.param("vehicleType", "InvalidVehicle"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void testChangeBaseFeeRules() throws Exception {
		String vehicle = "Bike";
		String newFee = "4";
		String city = "Tallinn";
		mockMvc.perform(MockMvcRequestBuilders.put("/api/changeBaseFeeRules/{CityName}/{VehicleName}/{fee}", city, vehicle, newFee)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Business rules updated"));

		double feeAfter = rbfRepository.findByCityAndVehicle(city, vehicle).getFee();
		assertThat(feeAfter).isEqualTo(Double.valueOf(newFee));
	}


	@Test
	void testChangeExtraFeeRules() throws Exception {
		String table = "Atef";
		String oldValue = "-10-0";
		String newValue = "-6-0";
		String fee = "2.3";
		mockMvc.perform(MockMvcRequestBuilders.put("/api/changeExtraFeeRules/{table}/{oldValue}/{newValue}/{fee}", table, oldValue, newValue, fee)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Business rules updated"));

		double feeAfter = atefRepository.findByBorders(newValue).getFee();
		assertThat(feeAfter).isEqualTo(Double.valueOf(fee));
	}

	@Test
	void testAddExtraFeeRules() throws Exception {
		int rowsBefore = wpefRepository.findAll().size();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/addExtraFeeRules/Wpef/foggy/1.4")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Business rules updated"));

		int rowsAfter = wpefRepository.findAll().size();
		assertThat(rowsAfter).isEqualTo(rowsBefore + 1);
		assertThat(wpefRepository.findByContaining("foggy").getFee()).isEqualTo(1.4);
	}

	@Test
	void testDeleteExtraFeeRules() throws Exception {
		int rowsBefore = wpefRepository.findAll().size();

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/deleteExtraFeeRules/Wpef/rain")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Business rules updated"));

		int rowsAfter = wpefRepository.findAll().size();
		assertThat(rowsAfter).isEqualTo(rowsBefore - 1);
	}



	//helper for testing calculateRbf
	private void performRequestAndAssertValue(String url, String city, String vehicleType, double expectedValue, String... dateTime) throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
				.param("city", city)
				.param("vehicleType", vehicleType);
		if (dateTime.length > 0) {
			requestBuilder.param("datetime", dateTime[0]);
		}
		mockMvc.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(result -> {
					String content = result.getResponse().getContentAsString();
					double value;
					try {
						value = Double.parseDouble(content);
					} catch (NumberFormatException e) {
						throw new AssertionError("Response is not a valid double");
					}
					if (expectedValue == 0.0) {
						assertThat(value).isGreaterThan(expectedValue);
					} else {
						assertThat(value).isEqualTo(expectedValue);
					}
				});
	}



}