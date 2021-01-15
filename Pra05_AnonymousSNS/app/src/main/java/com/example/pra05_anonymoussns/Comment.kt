package com.example.pra05_anonymoussns

class Comment {
    // 댓글 ID
    var commentId = ""

    // 댓글의 대상인 글의 ID
    var postId = ""

    // 댓글 작성자 ID
    var writerId = ""

    // 댓글 내용
    var message = ""

    // 작성 시간
    var writeTime: Any = Any()

    // 배경 이미지
    var bgUrl = ""
}