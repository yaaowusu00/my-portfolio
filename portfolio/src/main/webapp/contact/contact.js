const checkStatus = () => {
    fetch('/login').then(response => response.json()).then((data) => {
        if (data.status === "loggedIn"){
             $("#commentSection").css('display', "block");
             $(".commentForm").append(
                "<h3 class=\"logoutLink\" style=\"text-align: center;\"><a href=" +  data.url  + ">Sign out Here</a></h3>"); 
        }
        else { 
            $("body").append(
                "<h2 class=\"logoutLink\" style=\"text-align: center;\"><a href=" +  data.url  + ">Or sign in with Google to leave a comment!</a></h2>"); 
        }
    });
};

//display each comment on contact page in a "comment container"
const getContent = () => {
    fetch('/data').then(response => response.json()).then((data) => {
        for (msg in data) {
            let commentElement = $("<p></p>").text((data[msg]).text);
            $("#commentContainer").append('<div class = "commentBox">' +
            '<h1 class = "nameHeading">' +  (data[msg]).userName  + '</h1>' +
            '<h3 class = "time">' +  (data[msg]).readableDate  + '</h3>' +
            '<p class = "commentText">'+ (data[msg]).text + '</p></div>'); 
        }
    });
};

$(document).ready(function(){
    const params = new URLSearchParams(window.location.search)
    if (params.get('invalid') === "true") {
        alert('Please type a comment before submitting');
    }
    checkStatus();
    getContent();
 });