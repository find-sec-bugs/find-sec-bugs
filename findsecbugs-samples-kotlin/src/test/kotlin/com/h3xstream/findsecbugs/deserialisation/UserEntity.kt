package com.h3xstream.findsecbugs.deserialisation

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class UserEntity {
    @Id
    var id: Long? = null
    var test: String? = null
}