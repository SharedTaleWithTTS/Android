package com.example.sharedtalewithttsapp.utils

object Constants {
    const val TAG: String = "로그"
}
enum class HTTP_RESPONSE_STATE {
    OKAY,
    FAIL
}
object IDCHECK_STATE {
    const val ID_AVAILABLE : String = "ID_AVAILABLE"
    const val ID_DUPLICATE : String = "ID_DUPLICATE"
}
object NICKNAMECHECK_STATE {
    const val NICKNAME_AVAILABLE : String = "NICKNAME_AVAILABLE"
    const val NICKNAME_DUPLICATE : String = "NICKNAME_DUPLICATE"
}
object MEMBERJOIN_STATE {
    const val SUCCESS : String = "SUCCESS"
    const val FAIL : String = "FAIL"
}
object LOGIN_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "FAIL"
}
object READING_TALE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "FAIL"
}
object ADD_CHILD_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "FAIL"
}
object CHILD_PROFILE_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "FAIL"
}

object SEARCH_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "FAIL"
}
object WRITE_COMMENT_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}
object TALE_LIKE_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}
object COMMENT_LIST_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}
object FAVORITES_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}
object HOME_SCREEN_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}
object RATE_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}
object STATE_CHECK_RESPONSE_STATE {
    const val SUCCESS : String = "success"
    const val FAIL : String = "failure"
}

object API {
    const val BASE_URL : String = "http://112.152.27.80:8000"

    const val ID_CHECK : String = "/pybo/idCheck/"
    const val NICKNAME_CHECK : String = "/pybo/nickCheck/"
    const val MEMBERJOIN : String = "/pybo/signup/"
    const val LOGIN : String = "/pybo/login/"
    const val READING_TALE : String = "/pybo/requestTale/"
    const val AUDIO_REQUEST : String = "/pybo/requestAudio/"
    const val CHILD_PROFILE_REQUEST : String = "/pybo/requestChildProfile/"
    const val ADD_CHILD : String = "/pybo/addChild/"
    const val SEARCH : String = "/pybo/requestSearch/"
    const val WRITE_COMMENT : String = "/pybo/requestComment/"
    const val ADD_TALE_LIKE : String = "/pybo/requestLike/"
    const val COMMENT_LIST_REQUEST : String = "/pybo/requestCommentList/"
    const val FAVORITES_REQUEST : String = "/pybo/requestFavorite/"
    const val RATE_REQUEST : String =  "/pybo/requestRate/"
    const val HOME_SCREEN_REQUEST : String = "/pybo/requestHome/"
    const val STATE_CHECK : String = "/pybo/requestCheck/"

}