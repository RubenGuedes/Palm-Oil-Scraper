package com.mpoc.mpoc_project.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializationFeature
import com.mpoc.mpoc_project.model.PalmOilPrice
import com.mpoc.mpoc_project.repository.PalmOilPriceRepository
import com.mpoc.mpoc_project.service.DateFormatterID
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class PalmOilPriceControllerTest extends Specification {

    String requestURI = "/oil"

    DateFormatterID dateFormatterID
    PalmOilPrice oilPrice1,
                 oilPrice2
    List<PalmOilPrice> allOilPrice

    MockMvc mockMvc
    PalmOilPriceController palmOilPriceController
    PalmOilPriceRepository palmOilPriceRepository

    def setup() {
        dateFormatterID = new DateFormatterID()
        oilPrice1 = new PalmOilPrice("20-Sep-01", dateFormatterID.formatToDate("20-Sep-01"),200)
        oilPrice2 = new PalmOilPrice("10-Aug-09", dateFormatterID.formatToDate("10-Aug-09"),120)
        allOilPrice = [oilPrice1, oilPrice2]

        palmOilPriceRepository = Mock(PalmOilPriceRepository)
        palmOilPriceController = new PalmOilPriceController(palmOilPriceRepository)

        mockMvc = MockMvcBuilders.standaloneSetup(palmOilPriceController).build()
    }

    def "GetDatabaseInfo"() {
        given:
            palmOilPriceRepository.findAll() >> allOilPrice
        and:
            def resultString
            def resultList
        when:
            resultString = mockMvc.perform(get(requestURI))
                            .andExpect(status().isOk())
                            .andReturn().response.getContentAsString()

            resultList = new JsonSlurper().parseText(resultString)
        then:
            resultList.size() == allOilPrice.size()
    }

    def "GetPrice"() {
        given:
            1 * palmOilPriceRepository.findById(oilPrice1.getDate()) >> new Optional<PalmOilPrice>(oilPrice1)
            def varAux,
                result

        when:
            varAux = mockMvc.perform(get(requestURI+"/${oilPrice1.getDate()}")
                                .accept(APPLICATION_JSON_UTF8))
                            .andExpect(status().isOk())
                            .andReturn().response.getContentAsString()

            result = new JsonSlurper().parseText(varAux)

        then:
            result.date         == oilPrice1.getDate()
            result.formatDate   == oilPrice1.getFormatDate()
            result.price        == oilPrice1.getPrice()
    }

    def "GetPrice Null"() {
        given:
        1 * palmOilPriceRepository.findById(oilPrice1.getDate()) >> null
        def varAux

        when:
        varAux = mockMvc.perform(get(requestURI+"/${oilPrice1.getDate()}")
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().response.getContentAsString()


        then:
            "" == varAux
    }

    def "AddNewInfo"() {
        given:
            def resultString
            def resultJson

            PalmOilPrice instanceOil

            List<PalmOilPrice> resultList = [oilPrice1, oilPrice2]

            1 * palmOilPriceRepository.findById(oilPrice1.getDate()) >> oilPrice1
            1 * palmOilPriceRepository.findById(oilPrice2.getDate()) >> oilPrice2

            1 * palmOilPriceRepository.saveAndFlush(oilPrice1) >> oilPrice1
            1 * palmOilPriceRepository.saveAndFlush(oilPrice2) >> oilPrice2

            1 * palmOilPriceRepository.findAll() >> resultList

        when:

            resultString = mockMvc.perform(put(requestURI))
                    .andExpect(status().isOk())
                    .andReturn().response.getContentAsString()

            resultJson = new JsonSlurper().parseText(resultString)
        then:
        for (int index = 0; index < resultList.size(); index++) {
            instanceOil = resultList.get(index)

            instanceOil.getDate() == resultJson.index.date
        }
    }

    def "PostChanges"() {
        given:
            def result
            def resultJson,
                resultGoal
            def resultString

            1 * palmOilPriceRepository.saveAndFlush(_) >> oilPrice1
            1 * palmOilPriceRepository.findById(_) >> new Optional<>()

            ObjectMapper mapper = new ObjectMapper()
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter()

            resultJson = ow.writeValueAsString(oilPrice1)
            resultGoal = new JsonSlurper().parseText(resultJson)
        when:
            resultString = mockMvc.perform(
                    post(requestURI+"/${oilPrice1.getDate()}")
                            .contentType(APPLICATION_JSON)
                            .content(resultJson))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString()

            result = new JsonSlurper().parseText(resultString)

        then:
            result == resultGoal
    }

    def "PostChanges Not Null"() {
        given:
            def result
            def resultJson,
                resultGoal
            def resultString

            1 * palmOilPriceRepository.saveAndFlush(_) >> oilPrice2
            1 * palmOilPriceRepository.findById(_) >> new Optional<PalmOilPrice>(oilPrice1)

            ObjectMapper mapper = new ObjectMapper()
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter()

            resultJson = ow.writeValueAsString(oilPrice2)
            resultGoal = new JsonSlurper().parseText(resultJson)
        when:
            resultString = mockMvc.perform(
                    post(requestURI+"/${oilPrice1.getDate()}")
                            .contentType(APPLICATION_JSON)
                            .content(resultJson))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString()

            result = new JsonSlurper().parseText(resultString)

        then:
            result == resultGoal
    }

    def "DeleteOil"() {
        given:
            def resultString
            def resultGoal
            1 * palmOilPriceRepository.findById(oilPrice1.getDate()) >> new Optional<PalmOilPrice>(oilPrice1)
            1 * palmOilPriceRepository.delete(oilPrice1) >> null
        when:
            resultString = mockMvc.perform(delete(requestURI+"/${oilPrice1.getDate()}")
                    .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString()

            resultGoal = new JsonSlurper().parseText(resultString)
        then:
            resultGoal.date == oilPrice1.getDate()
    }

    def "DeleteOil Null"() {
        given:
            def resultString
            1 * palmOilPriceRepository.findById(_) >> null

        when:
            resultString = mockMvc.perform(delete(requestURI+"/${oilPrice1.getDate()}")
                    .accept(APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString()

        then:
            resultString == ""
    }
}
