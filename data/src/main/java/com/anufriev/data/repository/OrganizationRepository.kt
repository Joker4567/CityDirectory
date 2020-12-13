package com.anufriev.data.repository

import com.anufriev.data.db.entities.Organization

interface OrganizationRepository {
    fun getOrganization():List<Organization>
}