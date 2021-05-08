package com.khs.nbbang.kakaoFriends.kakao_interface

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