package com.mpoc.mpoc_project.repository

import com.mpoc.mpoc_project.model.PalmOilPrice
import org.springframework.data.jpa.repository.JpaRepository

interface PalmOilPriceRepository extends JpaRepository<PalmOilPrice, String> {}
