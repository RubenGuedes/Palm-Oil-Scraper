package com.mpoc.mpoc_project.model

import com.mpoc.mpoc_project.service.DateFormatterID
import spock.lang.Specification
import java.text.SimpleDateFormat

class PalmOilPriceTest extends Specification {

    DateFormatterID dateFormatter = new DateFormatterID()
    String  stringDate,
            formattedDate

    PalmOilPrice data,
                 inicializedData

    def setup() {
        stringDate = "19-Dec-10"
        formattedDate = dateFormatter.formatToDate(stringDate)

        data = new PalmOilPrice()
        inicializedData = new PalmOilPrice(stringDate, formattedDate, 200.2)
    }

    def "GetPrice"()        {
        given:
        def price = 10
        when:
        data.setPrice(price)
        then:
        data.getPrice() == price
    }

    def "SetPrice"()        {
        given:
        def price = 10
        when:
        data.setPrice(price)
        then:
        data.getPrice() == price
    }

    def "GetDate"()         {
        given:
        def date = "24 Sep 09"
        when:
        data.setDate(date)
        then:
        data.getDate() == date
    }

    def "SetDate"()         {
        given:
        def date = "24 Sep 09"
        when:
        data.setDate(date)
        then:
        data.getDate() == date
    }

    def "GetFormatDate"()   {
        when:
        data.setFormatDate( formattedDate )
        then:
        data.getFormatDate() == formattedDate
    }

    def "SetFormatDate"()   {
        when:
        data.setFormatDate()
        then:
        data.getFormatDate() == null
    }

    // With constructor
    def "GetPrice with constructor"()       {
        when:
            float price = 200.2
        then:
            inicializedData.getPrice() == price
    }

    def "SetPrice with constructor"()       {
        when:
            float price = 200.2
        then:
            inicializedData.getPrice() == price
    }

    def "GetDate with constructor"()        {
        expect:
            inicializedData.getDate() == stringDate
    }

    def "SetDate with constructor"()        {
        given:
            def date = "10-Dec-10"
        when:
            inicializedData.setDate(date)
        then:
            inicializedData.getDate() == date
    }

    def "SetFormatDate with constructor"()  {
        when:
            inicializedData.setFormatDate(formattedDate)
        then:
            inicializedData.getFormatDate() == formattedDate
    }

    def "GetFormatDate with constructor"()  {
        expect:
            inicializedData.getFormatDate() == formattedDate
    }
}