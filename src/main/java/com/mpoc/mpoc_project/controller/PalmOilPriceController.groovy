package com.mpoc.mpoc_project.controller

import com.mpoc.mpoc_project.model.PalmOilPrice
import com.mpoc.mpoc_project.service.UrlPalmOil
import com.mpoc.mpoc_project.repository.PalmOilPriceRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oil")
class PalmOilPriceController {

    @Autowired
    private UrlPalmOil urlService = new UrlPalmOil()
    private PalmOilPriceRepository palmRepo

    PalmOilPriceController( @Autowired PalmOilPriceRepository palmRepo)
    { this.palmRepo = palmRepo }

    /*
        Controllers
     */
    @GetMapping
    List<PalmOilPrice> getDatabaseInfo()
    { return palmRepo.findAll() }

    @GetMapping("/{date}")
    PalmOilPrice getPrice(@PathVariable String date)
    {
        Optional<PalmOilPrice> oilPriceOptional = palmRepo.findById(date)

        if (oilPriceOptional != null)
            return oilPriceOptional.get()

        return null    }

    @PutMapping
    List<PalmOilPrice> addNewInfo()
    {
        List<PalmOilPrice> table = urlService.getTableInfo()

        Optional<PalmOilPrice> oilPriceOptional
        PalmOilPrice oilPrice

        for (int index=0; index < table.size(); index++) {
            oilPrice = table.get(index)
            oilPriceOptional = palmRepo.findById(oilPrice.getDate())

            if (oilPriceOptional.isEmpty())
                palmRepo.saveAndFlush(oilPrice)
        }
        return palmRepo.findAll()
    }

    @PostMapping("/{date}")
    PalmOilPrice postChanges(@PathVariable String date, @RequestBody PalmOilPrice newOilPrice)
    {
        Optional<PalmOilPrice> oilPriceOptional = palmRepo.findById(date)

        // If exists
        if (!oilPriceOptional.isEmpty()) {
            PalmOilPrice oilPrice = oilPriceOptional.get()

            // Old oilPrice Object
            float  price = oilPrice.getPrice()
            String formatDate = oilPrice.getFormatDate()

            // New oilPrice Object
            float  newPrice = newOilPrice.getPrice()
            String newFormatDate = newOilPrice.getFormatDate()

            // Implement the differences
            if ( formatDate != newFormatDate )
                oilPrice.setFormatDate( newFormatDate )

            if ( price != newPrice )
                oilPrice.setPrice(newPrice)

            return palmRepo.saveAndFlush(oilPrice)}

        else { return palmRepo.saveAndFlush(newOilPrice) }
    }

    @DeleteMapping("/{date}")
    PalmOilPrice deleteOil(@PathVariable String date)
    {
        Optional<PalmOilPrice> oilPriceOptional = palmRepo.findById(date)
        PalmOilPrice oilPrice

        if (oilPriceOptional != null) {
            oilPrice = oilPriceOptional.get()
            palmRepo.delete(oilPrice)
            return oilPrice
        }
        return null }
}
