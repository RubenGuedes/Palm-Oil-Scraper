package com.mpoc.mpoc_project.service

import spock.lang.Specification

import java.text.DateFormat
import java.text.SimpleDateFormat

class DateFormatterTest extends Specification
{
    def "FormatToDate"()
    {
        when:
            String date = "10-Sep-19"
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy")
            String answer = DateFormat
                    .getDateInstance( DateFormat.LONG, Locale.ENGLISH)
                    .format( formatter.parse(date) )

            DateFormatterID dateFormatter = new DateFormatterID()
            String result = dateFormatter.formatToDate(date)

        then:
            answer == result
    }
}
