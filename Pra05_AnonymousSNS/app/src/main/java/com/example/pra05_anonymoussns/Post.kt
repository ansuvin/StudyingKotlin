package com.example.pra05_anonymoussns

class Post {
    // 글 ID
    var postId = ""

    // 글 작성자 ID
    var writeId = ""

    // 글 메세지
    var message = "d"

    // 글이 작성된 시간
    var writeTime: Any = Any()

    // 글의 배경이미지
    var bgUrl = ""

    // 댓글 개수
    var commentCount = 0

    override fun toString(): String {
        return "Post(postId='$postId', writeId='$writeId', message='$message', writeTime=$writeTime, bgUrl='$bgUrl', commentCount=$commentCount)"
    }

    fun toMap() : Map<String, Any?> {
        val result: HashMap<String, Any> = hashMapOf()
        result.put("postId",postId)
        result.put("writeId",writeId)
        result.put("message",message)
        result.put("writeTime",writeTime)
        result.put("bgUrl",bgUrl)
        result.put("commentCount", commentCount+1)
        return result
    }

}