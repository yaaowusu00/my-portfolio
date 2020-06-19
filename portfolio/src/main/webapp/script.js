/* determines position of the content on the top part of the about page so that it is consistent no matter what size the device is*/
const calculateAboutHeight = () => {
    let navHeight = $('.navBar').get(0).scrollHeight;
    let viewportHeight = $(window).height();
    let aboutHeight = viewportHeight - navHeight
    $('#aboutSection').css('height', aboutHeight);
};

/*displays pop up on projects page when you click project picture*/
const openTab = (tabName) => {
    if (tabName !== "EMPTY_NAME") {
         $(".content").css({"z-index":"3"});
    }
    let position = $(window).scrollTop() || (window.pageYOffset);
    const elements = Array.from(document.getElementsByClassName("descriptionContainer"));
    elements.forEach(description => ($(".content").css({"top": ((tabName === "EMPTY_NAME") ? "0": (position + 60))})));
    elements.forEach(description => ($('#' + tabName).css({"top": ((tabName === "EMPTY_NAME") ? "0": (position + 60))})));
    elements.forEach(description => (description.style.height = description.id === tabName ? "90vh" : "0"));
    elements.forEach(description => (description.style.padding = description.id === tabName ? "2% 3%" : "0"));
}; 

/*closes pop up on projects page when you click the x*/
const closeTab = (tabName) => {
    openTab(tabName, "close");  
    $(".content").css({"z-index":"-3"});
};

$(document).ready(function(){
    let navHeight = $('.navBar').height();
    $('#logo').css('height' , navHeight); //making the 2 parts of the nav bar the same height 
    $('.navOptions').css('height' , navHeight); //making the 2 parts of the nav bar the same height 
    $(window).on('load', calculateAboutHeight); 
    document.getElementsByTagName("html")[0].style.visibility = "visible";
    window.addEventListener('resize', calculateAboutHeight);
});



