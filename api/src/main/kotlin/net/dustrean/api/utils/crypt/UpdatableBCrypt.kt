package net.dustrean.api.utils.crypt

import org.mindrot.jbcrypt.BCrypt
import java.util.function.Function

class UpdatableBCrypt(
    private val logRounds: Int = 0
) {

    fun hash(password: String?): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds))
    }

    fun verifyHash(password: String?, hash: String?): Boolean {
        return BCrypt.checkpw(password, hash)
    }

    fun verifyAndUpdateHash(password: String?, hash: String, updateFunc: Function<String?, Boolean>): Boolean {
        if (BCrypt.checkpw(password, hash)) {
            val rounds = getRounds(hash)
            if (rounds != logRounds) {
                val newHash = hash(password)
                return updateFunc.apply(newHash)
            }
            return true
        }
        return false
    }

    private fun getRounds(salt: String): Int {
        var minor = 0.toChar()
        var off = 0
        require(!(salt[0] != '$' || salt[1] != '2')) { "Invalid salt version" }
        if (salt[2] == '$') off = 3 else {
            minor = salt[2]
            require(!(minor != 'a' || salt[3] != '$')) { "Invalid salt revision" }
            off = 4
        }
        require(salt[off + 2] <= '$') { "Missing salt rounds" }
        return salt.substring(off, off + 2).toInt()
    }

}