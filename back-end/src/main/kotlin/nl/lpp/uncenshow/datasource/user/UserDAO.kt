package nl.lpp.uncenshow.datasource.user




import nl.lpp.uncenshow.dto.user.UserDTO
import nl.lpp.uncenshow.dto.user.UserRepository
import nl.lpp.uncenshow.exceptionhandling.exceptions.NotFoundException
import nl.lpp.uncenshow.exceptionhandling.exceptions.NotSavedException
import org.apache.catalina.User
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Component

@Component
@EnableMongoRepositories("nl.llp.dto.user")
class UserDAO(
        private val userRepository: UserRepository,
) {

    fun test() {
        try {
            val user = UserDTO(null, "Test", "Test", "Test", "Test")
            userRepository.save(user);
        } catch (e: Exception) {
            throw NotSavedException()
        }
    }

    fun getUser(token: String, id: String): UserDTO {
        try {
            return userRepository.findItemByTokenAndId(token, id)!!
        } catch (e: Exception) {
            throw NotFoundException()
        }
    }

    fun getUserByUsernameAndPassword(username: String, password: String): UserDTO {
        try {
            return userRepository.findItemByUsernameAndPassword(username, password)!!
        } catch (e: Exception) {
            throw NotFoundException()
        }
    }

    fun saveUser(user: UserDTO): UserDTO {
        try {
            return userRepository.save(user)
        } catch (e: Exception) {
            throw NotSavedException()
        }
    }

    fun updateUser(user: UserDTO): UserDTO {
        try {
            return userRepository.save(user)
        } catch (e: Exception) {
            throw NotSavedException()
        }
    }

    fun getUserByUsername(username: String): String? {
        return userRepository.findItemByUsername(username)?.username
    }
}
