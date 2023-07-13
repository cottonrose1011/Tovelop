package com.tovelop.maphant.service

import com.sendgrid.helpers.mail.objects.Email
import com.tovelop.maphant.dto.UserDTO
import com.tovelop.maphant.mapper.UserMapper
import com.tovelop.maphant.utils.ValidationHelper
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class UserService(val mapper: UserMapper) {
    fun findPasswordByEmail(email: String) = mapper.findPasswordByEmail(email)
    fun findNickNameByEmail(email: String) = mapper.findNickNameByEmail(email)
    fun updateUserState(email: String, state: Char) {
        mapper.updateUserState(email, state, LocalDate.now())
    }

    fun updateUserPassword(email: String, newPassword: String) {
        mapper.updateUserPassword(email, newPassword, LocalDate.now())
    }

    fun signUp(user: UserDTO): Boolean {
        insertUser(user)
        return true
    }

    fun login(email: String, password: String): String? {
        // 로그인 로직
        val user = getUser(email)
        if (user != null && user.password == password) {
            return user.id.toString()
        }
        return null
    }

    fun matchEmail(email: String, universityId: Int?): Boolean {
        val universityName = this.extractFromEmail(email)
        val universityUrl = this.extractFromUrl(mapper.findUniversityUrlBy(universityId))
        return universityName == universityUrl
    }

    fun getUser(email: String): UserDTO? {
        // 사용자 조회 로직
        return mapper.readAllColumnVal(listOf(email)).firstOrNull()
    }

    fun insertUser(user: UserDTO) {
        // 사용자 추가 로직
        mapper.insertUser(user)
    }

    fun findUniversityIdBy(universityName: String): Int? {
        return mapper.findUniversityIdBy(universityName)
    }

    fun isPasswordValid(password: String): Boolean {
        return ValidationHelper.isValidPassword(password)
    }

    fun isEmailValid(email: String): Boolean {
        return ValidationHelper.isUniversityEmail(email)
    }

    fun findEmailBy(sNo: String, phoneNum: String): String? {
        return mapper.findEmailBy(sNo, phoneNum)
    }

    fun isNicknameValid(nickname: String): Boolean {
        return ValidationHelper.isAlphaNumericKorean(nickname)
    }

    fun isUniversityValid(universityId: Int?): Boolean {
        if (universityId == null) {
            return true
        }
        return mapper.isUniversityExist(universityId)
    }

    fun isDuplicateEmail(email: String): Boolean {
        return mapper.countSameEmails(email) > 0
    }

    fun isDuplicateNickname(nickname: String): Boolean {
        return mapper.countSameNickName(nickname) > 0
    }

    fun isDuplicatePhoneNum(phoneNum: String): Boolean {
        return mapper.countSamePhoneInt(phoneNum) > 0
    }

    fun extractFromEmail(email: String): String? {
        val pattern = Regex("""(?<=@)[^.]+\.(ac\.kr|edu)""")
        val matchResult = pattern.find(email)
        return matchResult?.value
    }

    fun extractFromUrl(url: String): String? {
        val pattern = """(?<=\/\/|www\.)[^\.]+\.(ac\.kr|edu)""".toRegex()
        val matchResult = pattern.find(url)
        return matchResult?.value
    }

}