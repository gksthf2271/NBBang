package com.khs.nbbang.kakaoFriends.kakao_interface

import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.Friends
import com.khs.nbbang.group.memberRequest
import com.khs.nbbang.kakaoFriends.KakaoRequest
import com.khs.nbbang.user.KaKaoMember
import com.khs.nbbang.user.Member
import io.reactivex.Single

abstract class KakaoParser {

//    abstract fun parseKakaoFriendList(
//        kakaoMember: Single<Friends<Friend>>
//    ): KakaoRequest = parseKakaoList(kakaoMember).map { friends ->
//        friends.map {
//            KaKaoMember(
//                id = it.id,
//                profileNickname = it.profileNickname,
//                uuId = it.uuid,
//                thumbnailImage = it.profileThumbnailImage,
//                isFavoriteByKakao = it.favorite
//            )
//        }
//    }
//
//    fun parseKakaoList(kakaoMember: Single<Friends<Friend>>) : List<Friend> {
//        var kakaoFriendList = arrayListOf<Friend>()
//        for (friend in kakaoMember.elements) {
//            kakaoFriendList.add(friend)
//        }
//        return kakaoFriendList
//    }
}