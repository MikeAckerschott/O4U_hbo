package nl.lpp.uncenshow.service.user

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.random.Random
import nl.lpp.uncenshow.datasource.authentication.AuthenticationDAO
import nl.lpp.uncenshow.datasource.user.UserDAO

import nl.lpp.uncenshow.dto.user.UserDTO
import nl.lpp.uncenshow.dto.user.UserSignupDTO
import nl.lpp.uncenshow.exceptionhandling.exceptions.InvalidImageException
import nl.lpp.uncenshow.exceptionhandling.exceptions.PasswordNotValidException
import nl.lpp.uncenshow.exceptionhandling.exceptions.UsernameAlreadyExistsException
import nl.lpp.uncenshow.exceptionhandling.exceptions.UsernameNotValidException
import org.apache.tika.Tika
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(private val userDAO: UserDAO, private val authenticationDAO: AuthenticationDAO) {

    fun signUpUser(user: UserSignupDTO): UserDTO {

        if (!user.isValidUsername()) {
            throw UsernameNotValidException()
        }

        if (!user.isValidPassword()) {
            throw PasswordNotValidException()
        }

        val user = UserDTO(null, createNewToken(), user.username, user.hashPassword(), user.image)

        if (userDAO.getUserByUsername(user.username!!) != UserDTO().username) {
            throw UsernameAlreadyExistsException()
        }

        return userDAO.saveUser(user)
    }



    fun getUser(token: String, id: String): UserDTO {
        return userDAO.getUser(token, id)
    }

    fun createNewToken(): String {
        var token = generateToken()
        while (authenticationDAO.tokenExists(token)) {
            token = generateToken()
        }
        return token
    }

    fun generateToken(): String {
        var token = ""
        val min = 1000
        val max = 10000
        for (i in 0..2) {
            val r = Random
            token += r.nextInt(max - min) + min
            if (i < 2) {
                token += "-"
            }
        }
        return token
    }

    fun loginUser(userSignupDTO: UserSignupDTO): UserDTO {
        val user =
                userDAO.getUserByUsernameAndPassword(
                        userSignupDTO.username!!,
                        userSignupDTO.hashPassword()
                )

        if (user != UserDTO()) {
            user.setToken(createNewToken())
            userDAO.saveUser(user)
            return user
        }

        return UserDTO()
    }

    fun uploadUserImage(image: MultipartFile): String {
        validateImage(image)

        val startingPath = "../front-end/uncenshow/src/assets/images/profile-images/"
        val path = Paths.get(startingPath)
        val uploadDir = path.toAbsolutePath().normalize()
        Files.createDirectories(path.toAbsolutePath().normalize())

        val fileName = "${System.currentTimeMillis()}_${image.originalFilename}"
        val targetPath: Path = uploadDir.resolve(fileName)
        Files.copy(image.inputStream, targetPath)

        return fileName
    }

    fun uploadJsonQuiz(jsonData: String): String {
        val startingPath = "../front-end/uncenshow/src/assets/Quizzes/"
        val path = Paths.get(startingPath)
        val uploadDir = path.toAbsolutePath().normalize()
        Files.createDirectories(path.toAbsolutePath().normalize())

        val fileName = "${System.currentTimeMillis()}.json"
        val targetPath: Path = uploadDir.resolve(fileName)
        // paste the contents of the string in the json file
        Files.write(targetPath, jsonData.toByteArray())

        return fileName
    }

    private fun validateImage(image: MultipartFile, maxSize: Long = 1000000) {
        if (image.isEmpty) {
            throw InvalidImageException("Image is empty.")
        }

        if (image.size > maxSize) {
            throw InvalidImageException("Image is too large.")
        }

        val tika = Tika()
        val fileType = tika.detect(image.bytes)
        if (!fileType.startsWith("image")) {
            throw InvalidImageException("Invalid image type.")
        }
    }
}
