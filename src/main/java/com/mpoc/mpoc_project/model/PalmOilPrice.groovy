package com.mpoc.mpoc_project.model

import com.mpoc.mpoc_project.service.DateFormatterID
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@Table(name = "PALMOILFINAL")
class PalmOilPrice
{
    @Id
    @Column(name = "DATE")
    String date
    @Column(name = "FORMAT_DATE")
    String formatDate
    @Column(name = "PRICE")
    float price


    PalmOilPrice() {
        def defaultDate = "00-Jan-00"
        def dateFormatter = new DateFormatterID()

        date = ""
        price = 0
        formatDate = dateFormatter.formatToDate(defaultDate)
    }

    PalmOilPrice(String date, String formatDate, float price) {
        this.price  = price
        this.date = date
        this.formatDate = formatDate
    }

    float getPrice() {return this.price}

    void setPrice(float price) {this.price = price}

    String getDate() {return this.date}

    void setDate(String date) {this.date = date}

    String getFormatDate() {return this.formatDate}

    void setFormatDate(String formatDate) {this.formatDate = formatDate}
}
