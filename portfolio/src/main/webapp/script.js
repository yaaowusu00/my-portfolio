/* determines position of the content on the top part of the about page so that it is consistent no matter what size the device is*/
const calculateAboutHeight = () => {
    let navHeight = $('.navBar').get(0).scrollHeight;
    let viewportHeight = $(window).height();
    let aboutHeight = viewportHeight - navHeight
    $('#aboutSection').css('height', aboutHeight);
};

$(document).ready(function(){
    let navHeight = $('.navBar').height();
    $('#logo').css('height' , navHeight); //making the 2 parts of the nav bar the same height 
    $('.navOptions').css('height' , navHeight); //making the 2 parts of the nav bar the same height 
    $(window).on('load', calculateAboutHeight); 
    document.getElementsByTagName("html")[0].style.visibility = "visible";
    window.addEventListener('resize', calculateAboutHeight);
});



