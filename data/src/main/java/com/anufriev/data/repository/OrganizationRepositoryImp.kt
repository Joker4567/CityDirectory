package com.anufriev.data.repository

import com.anufriev.data.db.entities.FeedBack
import com.anufriev.data.db.entities.Organization

class OrganizationRepositoryImp : OrganizationRepository {
    override fun getOrganization(): List<Organization> {
        return listOf(
            Organization(
                title = "Abrikos Group",
                description = "IT-компания ∙ Информационная безопасность ∙ Программное обеспечение",
                rating = 5,
                phone = "+7 342 200-95-96",
                listOf(
                    FeedBack(
                        state = true,
                        description = "Прекрасная компания. Работать одно удовольствие. Профессионалы своего дела."
                    )
                )
            )
        )
    }
}