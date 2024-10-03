package nl.lpp.uncenshow.dto.user

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<UserDTO?, String?> {

    fun findItemByToken(token: String): UserDTO?

    fun findItemByTokenAndId(token: String, id: String): UserDTO?

    fun findItemByUsernameAndPassword(username: String, password: String): UserDTO?

    fun findItemByUsername(username: String): UserDTO?

    override fun count(): Long
}