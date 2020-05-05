function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2targetId(questionId,1,content);

}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2targetId(commentId,2,content);
}
function comment2targetId(tagertId,type,content) {
    if(!content){
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId":tagertId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200){
                window.location.reload();
            }else {
                alert(response.message);
            }
        },
        dataType: "json"
    });
}
/**
 *展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
    //获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        //折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("icon-color");
    }else {
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length!=1){
            comments.addClass("in");
            //标记二级评论状态
            e.setAttribute("data-collapse","in");
            e.classList.add("icon-color");
        }else {
            $.getJSON("/comment/"+id,function (data) {
                console.log(data);

                var items = [];
                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded head-image",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.comment.gmtCreate).format('YYYY/MM/DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    var d = $("<hr/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 hr-style"
                    });
                    subCommentContainer.prepend(d);
                    subCommentContainer.prepend(commentElement);
                });

                comments.addClass("in");
                //标记二级评论状态
                e.setAttribute("data-collapse","in");
                e.classList.add("icon-color");
            });
        }

    }
}

function showSelectTag() {
    $("#selectTag").show();
}

function selectTag(e) {
    var previous = $("#tag").val();
    var value = e.getAttribute("data-tag");
    if (previous.indexOf(value)==-1){
        if(previous){
            $("#tag").val(previous+','+value)
        }else {
            $("#tag").val(value);
        }
    }
}