package com.mpoc.mpoc_project.service

import com.mpoc.mpoc_project.model.PalmOilPrice
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.stereotype.Service

@Service
class UrlPalmOil {

    final String PAGE = "http://mpoc.org.my/daily-palm-oil-price/"

    // Main method
    List<PalmOilPrice> getTableInfo() {
        Document doc = Jsoup.connect(PAGE).get()
        return docSorting(doc) }

    // Background methods
    static private List<PalmOilPrice> docSorting(Document doc)
    {
        DateFormatterID dateFormatter = new DateFormatterID()
        List<PalmOilPrice> tableOilPrice = new ArrayList<>()
        Element table = doc.select(".tableizer-table").get(0),
                singleRow

        String  dateId,
                formatDate
        Integer price

        Elements rows = table.select("tbody"),
                  trs = rows .select("tr")

        for (int index = 1; index < trs.size(); index++)
        {
            singleRow = trs.get(index)

            price  = singleRow.select("td").get(1).ownText().toInteger()
            dateId = singleRow.select("td").get(0).ownText().replace(' ', '-')
            formatDate = dateFormatter.formatToDate(dateId)

            tableOilPrice.add(new PalmOilPrice(dateId, formatDate, price))
        }
        return tableOilPrice }

}
