package com.hproject.banttang.data.file.model.mapper

import com.hproject.banttang.data.file.model.ImageData
import com.hproject.banttang.domain.file.entity.Image

fun ImageData.toDomainEntity(): Image  = Image(images = images)
