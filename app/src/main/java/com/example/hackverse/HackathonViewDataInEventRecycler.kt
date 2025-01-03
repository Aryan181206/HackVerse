package com.example.hackverse

import com.google.firebase.Timestamp
import com.google.firebase.database.PropertyName

data class HackathonViewDataInEventRecycler(
    val HackathonDescription: String = "",
    val HackathonEndDate: String? = null,
    val HackathonMode: String = "",
    val HackathonReward: String = "",
    val HackathonStartDate: String? = null,
    val HackathonTitle: String = "",
    val HackathonType: String = "",
    val OrganisationName: String = "",
    val TeamSize: String = "",
    val HackathonId : String ="",
    val TotalCount : String="",
    val likeCount : String=""

)
