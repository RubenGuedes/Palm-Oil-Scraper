package com.mpoc.mpoc_project.service

import com.mpoc.mpoc_project.model.PalmOilPrice
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import spock.lang.Specification

class UrlPalmOilTest extends Specification
{
    private final String PAGE = "http://mpoc.org.my/daily-palm-oil-price/"
    UrlPalmOil urlPalmOil
    DateFormatterID dateFormatter

    def setup() {
        urlPalmOil    = new UrlPalmOil()
        dateFormatter = new DateFormatterID()
    }

    def "GetTableInfo"() {
        when:
            List<PalmOilPrice> result1 = urlPalmOil.getTableInfo()
            List<PalmOilPrice> result2 = new ArrayList<>()

            float price
            String dateId,
                   dateFormatted

            Document doc = Jsoup.connect(PAGE).get()
            Element table = doc.select(".tableizer-table").get(0),
                    singleRow
            Elements rows = table.select("tbody"),
                     trs = rows .select("tr")

            for (int index = 1; index < trs.size(); index++)
            {
                singleRow = trs.get(index)

                dateId  = singleRow.select("td").get(0).ownText().replace(' ', '-')
                price   = singleRow.select("td").get(1).ownText().toInteger()
                dateFormatted = dateFormatter.formatToDate(dateId)

                result2.add(new PalmOilPrice(dateId, dateFormatted, price))
            }
        then:
            for (int index = 0; index<result2.size(); index++) {
                result1.get(index).getDate() == result2.get(index).getDate()
                result1.get(index).getPrice() == result2.get(index).getPrice()
                result1.get(index).getFormatDate() == result2.get(index).getFormatDate()
            }
    }
}
